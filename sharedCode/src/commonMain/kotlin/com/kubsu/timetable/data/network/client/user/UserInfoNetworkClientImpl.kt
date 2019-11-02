package com.kubsu.timetable.data.network.client.user

import com.egroden.teaco.*
import com.kubsu.timetable.*
import com.kubsu.timetable.data.network.client.user.incorrectdata.SignInIncorrectData
import com.kubsu.timetable.data.network.client.user.incorrectdata.UserIncorrectData
import com.kubsu.timetable.data.network.dto.UserData
import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import com.kubsu.timetable.data.storage.user.session.Session
import com.kubsu.timetable.data.storage.user.token.Token
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.extensions.*
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse
import kotlinx.serialization.ImplicitReflectionSerializer
import platform.currentPlatformName

class UserInfoNetworkClientImpl(
    private val networkSender: NetworkSender
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
        token: Token
    ): Either<RequestFailure<List<SignInFail>>, UserData> =
        with(networkSender) {
            handle {
                post<HttpResponse>("$baseUrl/api/$apiVersion/users/login/") {
                    body = jsonContent(
                        "email" to email.toJson(),
                        "password" to password.toJson(),
                        "token" to token.value.toJson(),
                        "platform" to currentPlatformName.toJson()
                    )
                }.also(::validate)
            }
                .mapLeft {
                    if (it is ServerFailure.Response && it.code == 400)
                        parseSignInFail(it)
                    else
                        RequestFailure(toNetworkFail(it))
                }
                .flatMap { parseSignInResponse(it) }
        }

    private suspend fun parseSignInResponse(
        response: HttpResponse
    ): Either<RequestFailure<List<SignInFail>>, UserData> =
        createSession(response)
            .flatMap { session ->
                parseUser(response).map {
                    UserData(user = it, session = session)
                }
            }
            .mapLeft { RequestFailure<List<SignInFail>>(it) }

    private fun createSession(response: HttpResponse): Either<DataFailure, Session> =
        response
            .headers[sessionId]
            ?.let { Either.right(Session(id = it, timestamp = Timestamp.create())) }
            ?: Either.left(DataFailure.ParsingError(response.toString()))

    private suspend fun parseUser(response: HttpResponse): Either<DataFailure, UserNetworkDto> {
        val body = response.readContent()
        return try {
            Either.right(networkSender.json.parse(UserNetworkDto.serializer(), body))
        } catch (e: Exception) {
            Either.left(DataFailure.ParsingError(e.toString()))
        }
    }

    private fun parseSignInFail(response: ServerFailure.Response): RequestFailure<List<SignInFail>> {
        val incorrectData =
            networkSender.json.parse(SignInIncorrectData.serializer(), response.body)

        val allFailureList = mapBaseFailureList(incorrectData, response)
        val emailFailureList = mapEmailFailureList(incorrectData, response)
        val passwordFailureList = mapPasswordFailureList(incorrectData, response)

        val failList = allFailureList + emailFailureList + passwordFailureList
        return RequestFailure(
            domain = failList.filterIsInstance<SignInFail>(),
            data = failList.filterIsInstance<DataFailure>()
        )
    }

    private fun mapBaseFailureList(
        incorrectData: SignInIncorrectData,
        response: ServerFailure.Response
    ): List<Failure> =
        incorrectData.all.map { failure ->
            when (failure) {
                "incorrect_email_or_password" -> SignInFail.IncorrectEmailOrPassword
                "account_inactivate" -> SignInFail.AccountInactivate
                else -> DataFailure.UnknownResponse(response.code, response.body)
            }
        }

    private fun mapEmailFailureList(
        incorrectData: SignInIncorrectData,
        response: ServerFailure.Response
    ): List<Failure> =
        incorrectData.email.map { failure ->
            when (failure) {
                "invalid" -> SignInFail.InvalidEmail
                "required" -> SignInFail.RequiredEmail
                else -> DataFailure.UnknownResponse(response.code, response.body)
            }
        }

    private fun mapPasswordFailureList(
        incorrectData: SignInIncorrectData,
        response: ServerFailure.Response
    ): List<Failure> =
        incorrectData.password.map { failure ->
            when (failure) {
                "required" -> SignInFail.RequiredPassword
                else -> DataFailure.UnknownResponse(response.code, response.body)
            }
        }

    override suspend fun update(
        session: Session,
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
        val incorrectData = networkSender.json.parse(UserIncorrectData.serializer(), response.body)

        val emailFailureList = mapEmailFailureList(incorrectData, response)
        val passwordFailureList = mapPasswordFailureList(incorrectData, response)
        val firstNameFailureList = mapFirstNameFailureList(incorrectData, response)
        val lastNameFailureList = mapLastNameFailureList(incorrectData, response)

        val failList =
            emailFailureList + passwordFailureList + firstNameFailureList + lastNameFailureList
        return RequestFailure(
            domain = failList.filterIsInstance<UserInfoFail>(),
            data = failList.filterIsInstance<DataFailure>()
        )
    }

    private fun mapEmailFailureList(
        incorrectData: UserIncorrectData,
        response: ServerFailure.Response
    ): List<Failure> =
        incorrectData.email.map { failure ->
            when (failure) {
                "invalid" -> UserInfoFail.Email.Invalid
                "unique" -> UserInfoFail.Email.NotUnique
                "blank" -> UserInfoFail.Email.Required
                else -> DataFailure.UnknownResponse(
                    response.code,
                    response.body,
                    "Unknown param: $failure"
                )
            }
        }

    private fun mapPasswordFailureList(
        incorrectData: UserIncorrectData,
        response: ServerFailure.Response
    ): List<Failure> =
        incorrectData.password.map { failure ->
            when (failure) {
                "password_too_short" -> UserInfoFail.Password.TooShort
                "password_too_common" -> UserInfoFail.Password.TooCommon
                "password_entirely_numeric" -> UserInfoFail.Password.EntirelyNumeric
                "blank" -> UserInfoFail.Password.Required
                else -> DataFailure.UnknownResponse(
                    response.code,
                    response.body,
                    "Unknown param: $failure"
                )
            }
        }

    private fun mapFirstNameFailureList(
        incorrectData: UserIncorrectData,
        response: ServerFailure.Response
    ): List<Failure> =
        incorrectData.firstName.map { failure ->
            when (failure) {
                "max_length" -> UserInfoFail.FirstName.TooLong
                else -> DataFailure.UnknownResponse(
                    response.code,
                    response.body,
                    "Unknown param: $failure"
                )
            }
        }

    private fun mapLastNameFailureList(
        incorrectData: UserIncorrectData,
        response: ServerFailure.Response
    ): List<Failure> =
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

    override suspend fun updateToken(
        session: Session,
        token: Token
    ): Either<DataFailure, Unit> =
        with(networkSender) {
            handle {
                get<Unit>("$baseUrl/api/$apiVersion/users/device/") {
                    addSessionKey(session)
                    body = jsonContent(
                        "token" to token.value.toJson(),
                        "platform" to currentPlatformName.toJson()
                    )
                }
            }.mapLeft(::toNetworkFail)
        }

    override suspend fun logout(session: Session): Either<DataFailure, Unit> =
        with(networkSender) {
            handle {
                get<Unit>("$baseUrl/api/$apiVersion/users/logout/") {
                    addSessionKey(session)
                }
            }.mapLeft(::toNetworkFail)
        }
}