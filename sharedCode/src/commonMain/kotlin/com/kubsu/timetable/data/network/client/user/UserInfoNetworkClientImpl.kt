package com.kubsu.timetable.data.network.client.user

import com.kubsu.timetable.*
import com.kubsu.timetable.data.network.client.user.incorrectdata.SignInIncorrectData
import com.kubsu.timetable.data.network.client.user.incorrectdata.UserIncorrectData
import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import com.kubsu.timetable.extensions.addSessionKey
import com.kubsu.timetable.extensions.jsonContent
import com.kubsu.timetable.extensions.toJson
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import kotlinx.serialization.json.Json

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


    override suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<List<SignInFail>>, UserNetworkDto> =
        with(networkSender) {
            handle {
                post<UserNetworkDto>("$baseUrl/api/$apiVersion/users/login/") {
                    body = jsonContent("email" to email.toJson(), "password" to password.toJson())
                }
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 400)
                    parseSignInFail(it)
                else
                    RequestFailure(toNetworkFail(it))
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
        user: UserNetworkDto
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> =
        with(networkSender) {
            handle {
                patch<Unit>("$baseUrl/api/$apiVersion/users/info/") {
                    addSessionKey(user)
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

    override suspend fun logout(user: UserNetworkDto): Either<DataFailure, Unit> =
        with(networkSender) {
            handle {
                get<Unit>("$baseUrl/api/$apiVersion/users/logout/") {
                    addSessionKey(user)
                }
            }.mapLeft(::toNetworkFail)
        }
}