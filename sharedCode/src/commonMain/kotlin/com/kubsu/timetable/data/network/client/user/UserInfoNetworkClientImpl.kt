package com.kubsu.timetable.data.network.client.user

import com.kubsu.timetable.*
import com.kubsu.timetable.data.network.client.user.incorrectdata.RegistrationIncorrectData
import com.kubsu.timetable.data.network.client.user.incorrectdata.SignInIncorrectData
import com.kubsu.timetable.data.network.client.user.incorrectdata.UserIncorrectData
import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.http.Parameters
import io.ktor.http.Url
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
                    body = FormDataContent(
                        Parameters.build {
                            append("email", email)
                            append("password", password)
                        }
                    )
                }
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 400)
                    parseRegistrationFail(it)
                else
                    RequestFailure(toNetworkFail(it))
            }
        }

    private fun parseRegistrationFail(response: ServerFailure.Response): RequestFailure<List<UserInfoFail>> {
        val incorrectData = json.parse(RegistrationIncorrectData.serializer(), response.body)
        return handleUserInfoFail(
            responseCode = response.code,
            responseBody = response.body,
            emailFailList = incorrectData.email,
            passwordFailList = incorrectData.password
        )
    }

    private fun handleUserInfoFail(
        responseCode: Int,
        responseBody: String,
        emailFailList: List<String>,
        passwordFailList: List<String>
    ): RequestFailure<List<UserInfoFail>> {
        val failList = emailFailList.map {
            when (it) {
                "invalid" -> UserInfoFail.InvalidEmail
                "unique" -> UserInfoFail.NotUniqueEmail
                else -> DataFailure.UnknownResponse(
                    responseCode,
                    responseBody,
                    "Unknown param: $it"
                )
            }
        }.plus(
            passwordFailList.map {
                when (it) {
                    "password_too_short" -> UserInfoFail.ShortPassword
                    "password_too_common" -> UserInfoFail.CommonPassword
                    "password_entirely_numeric" -> UserInfoFail.EntirelyNumericPassword
                    else -> DataFailure.UnknownResponse(
                        responseCode,
                        responseBody,
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

    override suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<List<SignInFail>>, UserNetworkDto> =
        with(networkSender) {
            handle {
                post<UserNetworkDto>("$baseUrl/api/$apiVersion/users/login/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("email", email)
                            append("password", password)
                        }
                    )
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
        return handleSignInFail(
            responseCode = response.code,
            responseBody = response.body,
            allFailList = incorrectData.all,
            emailFailList = incorrectData.email
        )
    }

    private fun handleSignInFail(
        responseCode: Int,
        responseBody: String,
        allFailList: List<String>,
        emailFailList: List<String>
    ): RequestFailure<List<SignInFail>> {
        val failList = allFailList.map {
            when (it) {
                "incorrect_email_or_password" -> SignInFail.AccountInactivate
                "account_inactivate" -> SignInFail.IncorrectEmailOrPassword
                else -> DataFailure.UnknownResponse(responseCode, responseBody)
            }
        }.plus(
            emailFailList.map {
                when (it) {
                    "invalid" -> SignInFail.InvalidEmail
                    else -> DataFailure.UnknownResponse(responseCode, responseBody)
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
    ): Either<RequestFailure<List<UserUpdateFail>>, Unit> =
        with(networkSender) {
            handle {
                val url = Url("$baseUrl/api/$apiVersion/users/info/")
                patch<Unit>(url) {
                    body = FormDataContent(
                        Parameters.build {
                            append("first_name", user.firstName)
                            append("last_name", user.lastName)
                        }
                    )
                }
            }.mapLeft {
                if (it is ServerFailure.Response && (it.code == 400 || it.code == 403))
                    if (it.code == 403)
                        RequestFailure(DataFailure.NotAuthenticated(it.body))
                    else
                        parseUpdateFail(it)
                else
                    RequestFailure(toNetworkFail(it))
            }
        }

    private fun parseUpdateFail(response: ServerFailure.Response): RequestFailure<List<UserUpdateFail>> {
        val incorrectData = json.parse(UserIncorrectData.serializer(), response.body)
        return handleUpdateFail(
            responseCode = response.code,
            responseBody = response.body,
            emailFailList = incorrectData.email,
            passwordFailList = incorrectData.password,
            firstNameFailList = incorrectData.firstName,
            lastNameFailList = incorrectData.lastName
        )
    }

    private fun handleUpdateFail(
        responseCode: Int,
        responseBody: String,
        emailFailList: List<String>,
        passwordFailList: List<String>,
        firstNameFailList: List<String>,
        lastNameFailList: List<String>
    ): RequestFailure<List<UserUpdateFail>> {
        val userInfoRequestFail: RequestFailure<List<UserUpdateFail>> =
            handleUserInfoFail(responseCode, responseBody, emailFailList, passwordFailList)
                .mapDomain { it?.map(UserUpdateFail::Info) as? List<UserUpdateFail> ?: emptyList() }

        val failList = firstNameFailList.map {
            when (it) {
                "max_length" -> UserUpdateFail.TooLongFirstName
                else -> DataFailure.UnknownResponse(responseCode, responseBody)
            }
        }.plus(
            lastNameFailList.map {
                when (it) {
                    "max_length" -> UserUpdateFail.TooLongLastName
                    else -> DataFailure.UnknownResponse(responseCode, responseBody)
                }
            }
        )

        val maxLengthRequestFail = RequestFailure(
            domain = failList.filterIsInstance<UserUpdateFail>(),
            data = failList.filterIsInstance<DataFailure>()
        )

        return userInfoRequestFail.plus(
            maxLengthRequestFail,
            domainPlus = { first, second ->
                first
                    ?.plus(second ?: emptyList())
                    ?.toList()
            }
        )
    }

    override suspend fun logout(): Either<DataFailure, Unit> =
        with(networkSender) {
            handle {
                get<Unit>("$baseUrl/api/$apiVersion/users/logout/")
            }.mapLeft(::toNetworkFail)
        }
}