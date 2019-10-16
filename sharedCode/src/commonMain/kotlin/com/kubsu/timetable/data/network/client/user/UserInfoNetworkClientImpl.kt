package com.kubsu.timetable.data.network.client.user

import com.kubsu.timetable.*
import com.kubsu.timetable.data.network.client.user.incorrectdata.RegistrationIncorrectData
import com.kubsu.timetable.data.network.client.user.incorrectdata.SignInIncorrectData
import com.kubsu.timetable.data.network.client.user.incorrectdata.UserIncorrectData
import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
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
    ): Either<RequestFailure<List<RegistrationFail>>, Unit> =
        with(networkSender) {
            handle {
                post<Unit>("$baseUrl/api/$apiVersion/users/registration/") {
                    body = jsonContent("email" to email.toJson(), "password" to password.toJson())
                }
            }.mapLeft {
                if (it is ServerFailure.Response && it.code == 400)
                    parseRegistrationFail(it)
                else
                    RequestFailure(toNetworkFail(it))
            }
        }

    private fun parseRegistrationFail(response: ServerFailure.Response): RequestFailure<List<RegistrationFail>> {
        val incorrectData = json.parse(RegistrationIncorrectData.serializer(), response.body)
        return handleRegistrationFail(
            responseCode = response.code,
            responseBody = response.body,
            emailFailList = incorrectData.email,
            passwordFailList = incorrectData.password
        )
    }

    private fun handleRegistrationFail(
        responseCode: Int,
        responseBody: String,
        emailFailList: List<String>,
        passwordFailList: List<String>
    ): RequestFailure<List<RegistrationFail>> {
        val failList = emailFailList.map {
            when (it) {
                "invalid" -> RegistrationFail.Email.Invalid
                "unique" -> RegistrationFail.Email.NotUnique
                "blank" -> RegistrationFail.Email.Required
                else -> DataFailure.UnknownResponse(responseCode, responseBody, "Unknown param: $it")
            }
        }.plus(
            passwordFailList.map {
                when (it) {
                    "password_too_short" -> RegistrationFail.Password.TooShort
                    "password_too_common" -> RegistrationFail.Password.TooCommon
                    "password_entirely_numeric" -> RegistrationFail.Password.EntirelyNumeric
                    "blank" -> RegistrationFail.Password.Required
                    else -> DataFailure.UnknownResponse(responseCode, responseBody, "Unknown param: $it")
                }
            }
        )
        return RequestFailure(
            domain = failList.filterIsInstance<RegistrationFail>(),
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
        return handleSignInFail(
            responseCode = response.code,
            responseBody = response.body,
            allFailList = incorrectData.all,
            emailFailList = incorrectData.email,
            passwordFailList = incorrectData.password
        )
    }

    private fun handleSignInFail(
        responseCode: Int,
        responseBody: String,
        allFailList: List<String>,
        emailFailList: List<String>,
        passwordFailList: List<String>
    ): RequestFailure<List<SignInFail>> {
        val failList = allFailList.map {
            when (it) {
                "incorrect_email_or_password" -> SignInFail.IncorrectEmailOrPassword
                "account_inactivate" -> SignInFail.AccountInactivate
                else -> DataFailure.UnknownResponse(responseCode, responseBody)
            }
        }.plus(
            emailFailList.map {
                when (it) {
                    "invalid" -> SignInFail.InvalidEmail
                    "required" -> SignInFail.RequiredEmail
                    else -> DataFailure.UnknownResponse(responseCode, responseBody)
                }
            }
        ).plus(
            passwordFailList.map {
                when (it) {
                    "required" -> SignInFail.RequiredPassword
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
            handleRegistrationFail(responseCode, responseBody, emailFailList, passwordFailList)
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

        return userInfoRequestFail.plus(
            requestFailure = RequestFailure(
                domain = failList.filterIsInstance<UserUpdateFail>(),
                data = failList.filterIsInstance<DataFailure>()
            ),
            domainPlus = { first, second ->
                first?.plus(second ?: emptyList())?.toList()
            }
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