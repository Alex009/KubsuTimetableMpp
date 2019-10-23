package com.kubsu.timetable.data.network.client.user

import com.egroden.teaco.*
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.data.network.client.user.incorrectdata.SignInIncorrectData
import com.kubsu.timetable.data.network.client.user.incorrectdata.UserIncorrectData
import com.kubsu.timetable.data.network.dto.UserData
import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import com.kubsu.timetable.data.storage.user.session.SessionDto
import com.kubsu.timetable.data.storage.user.token.TokenDto
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.extensions.*
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import platform.currentPlatform

class UserInfoNetworkClientImpl(
    private val networkSender: NetworkSender,
    private val json: Json
) : UserInfoNetworkClient {
    override suspend fun registration(
        email: String,
        password: String
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> =
        with(networkSender) {
            handle {
                post<Unit>("$baseUrl/api/$apiVersion/users/registration/") {
                    body = jsonContent(
                        "email" to email.toJson(),
                        "password" to password.toJson()
                    )
                }
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 400)
                    parseUserInfoFail(it)
                else
                    RequestFailure(toNetworkFail(it))
            }
        }


    @UseExperimental(ImplicitReflectionSerializer::class)
    override suspend fun signIn(
        email: String,
        password: String,
        token: TokenDto?
    ): Either<RequestFailure<List<SignInFail>>, UserData> =
        with(networkSender) {
            handle {
                post<HttpResponse>("$baseUrl/api/$apiVersion/users/login/") {
                    body = jsonContent(
                        "email" to email.toJson(),
                        "password" to password.toJson(),
                        "token" to (token?.value ?: "").toJson(),
                        "platform" to currentPlatform.toJson()
                    )
                }.also(::validate)
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 400)
                    parseSignInFail(it)
                else
                    RequestFailure(toNetworkFail(it))
            }.flatMap { response: HttpResponse ->
                createSession(response).flatMap { session ->
                    parseUser(response).map { user ->
                        UserData(user, session)
                    }
                }.mapLeft {
                    RequestFailure<List<SignInFail>>(it)
                }
            }
        }

    private fun createSession(response: HttpResponse): Either<DataFailure, SessionDto> {
        val sessionId = response.headers[sessionId]
        return if (sessionId != null)
            Either.right(
                SessionDto(
                    id = sessionId,
                    timestamp = Timestamp.create()
                )
            )
        else
            Either.left(DataFailure.ParsingError(response.toString()))
    }

    private suspend fun parseUser(response: HttpResponse): Either<DataFailure, UserNetworkDto> {
        val body = response.readContent()
        return try {
            Either.right(json.parse(UserNetworkDto.serializer(), body))
        } catch (e: Exception) {
            Either.left(DataFailure.ParsingError(e.toString()))
        }
    }

    private fun parseSignInFail(response: ServerFailure.Response): RequestFailure<List<SignInFail>> {
        val incorrectData = json.parse(SignInIncorrectData.serializer(), response.body)
        val failList = incorrectData.all.map {
            when (it) {
                "incorrect_email_or_password" -> SignInFail.IncorrectEmailOrPassword
                "account_inactivate" -> SignInFail.AccountInactivate
                else -> DataFailure.UnknownResponse(response.code, response.body)
            }
        }.plus(
            incorrectData.email.map {
                when (it) {
                    "invalid" -> SignInFail.InvalidEmail
                    "required" -> SignInFail.RequiredEmail
                    else -> DataFailure.UnknownResponse(response.code, response.body)
                }
            }
        ).plus(
            incorrectData.password.map {
                when (it) {
                    "required" -> SignInFail.RequiredPassword
                    else -> DataFailure.UnknownResponse(response.code, response.body)
                }
            }
        )
        return RequestFailure(
            domain = failList.filterIsInstance<SignInFail>(),
            data = failList.filterIsInstance<DataFailure>()
        )
    }

    override suspend fun update(
        session: SessionDto,
        user: UserNetworkDto
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> =
        with(networkSender) {
            handle {
                patch<Unit>("$baseUrl/api/$apiVersion/users/info/") {
                    addSessionKey(session)
                    body = jsonContent(
                        "first_name" to user.firstName.toJson(),
                        "last_name" to user.lastName.toJson()
                    )
                }
            }.mapLeft {
                if (it is ServerFailure.Response && (it.code == 400 || it.code == 403))
                    if (it.code == 403)
                        RequestFailure(DataFailure.NotAuthenticated(it.body))
                    else
                        parseUserInfoFail(it)
                else
                    RequestFailure(toNetworkFail(it))
            }
        }

    private fun parseUserInfoFail(response: ServerFailure.Response): RequestFailure<List<UserInfoFail>> {
        val incorrectData = json.parse(UserIncorrectData.serializer(), response.body)
        val failList = incorrectData.email.map {
            when (it) {
                "invalid" -> UserInfoFail.Email.Invalid
                "unique" -> UserInfoFail.Email.NotUnique
                "blank" -> UserInfoFail.Email.Required
                else -> DataFailure.UnknownResponse(
                    response.code,
                    response.body,
                    "Unknown param: $it"
                )
            }
        }.plus(
            incorrectData.password.map {
                when (it) {
                    "password_too_short" -> UserInfoFail.Password.TooShort
                    "password_too_common" -> UserInfoFail.Password.TooCommon
                    "password_entirely_numeric" -> UserInfoFail.Password.EntirelyNumeric
                    "blank" -> UserInfoFail.Password.Required
                    else -> DataFailure.UnknownResponse(
                        response.code,
                        response.body,
                        "Unknown param: $it"
                    )
                }
            }
        ).plus(
            incorrectData.firstName.map {
                when (it) {
                    "max_length" -> UserInfoFail.FirstName.TooLong
                    else -> DataFailure.UnknownResponse(
                        response.code,
                        response.body,
                        "Unknown param: $it"
                    )
                }
            }
        ).plus(
            incorrectData.lastName.map {
                when (it) {
                    "max_length" -> UserInfoFail.LastName.TooLong
                    else -> DataFailure.UnknownResponse(
                        response.code,
                        response.body,
                        "Unknown param: $it"
                    )
                }
            }
        )
        return RequestFailure(
            domain = failList.filterIsInstance<UserInfoFail>(),
            data = failList.filterIsInstance<DataFailure>()
        )
    }

    override suspend fun updateToken(
        session: SessionDto,
        token: TokenDto
    ): Either<DataFailure, Unit> =
        with(networkSender) {
            handle {
                get<Unit>("$baseUrl/api/$apiVersion/users/device/") {
                    addSessionKey(session)
                    body = jsonContent(
                        "token" to token.value.toJson(),
                        "platform" to currentPlatform.toJson()
                    )
                }
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun logout(session: SessionDto): Either<DataFailure, Unit> =
        with(networkSender) {
            handle {
                get<Unit>("$baseUrl/api/$apiVersion/users/logout/") {
                    addSessionKey(session)
                }
            }.mapLeft(::toNetworkFail)
        }
}