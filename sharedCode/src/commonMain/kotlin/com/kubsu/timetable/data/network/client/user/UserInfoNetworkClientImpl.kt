package com.kubsu.timetable.data.network.client.user

import com.kubsu.timetable.*
import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import com.kubsu.timetable.data.network.sender.failure.toNetworkFail
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.http.Parameters
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

class UserInfoNetworkClientImpl(
    private val networkSender: NetworkSender
) : UserInfoNetworkClient {
    override suspend fun registration(
        email: String,
        password: String
    ): Either<RequestFailure<Set<RegistrationFail>>, Unit> =
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
                    RequestFailure(handleRegistrationFail(it))
                else
                    RequestFailure(toNetworkFail(it))
            }
        }

    @UseExperimental(UnstableDefault::class)
    private fun handleRegistrationFail(response: ServerFailure.Response): Set<RegistrationFail> {
        val incorrectData = Json.parse(RegistrationIncorrectData.serializer(), response.body)
        return incorrectData.email.mapNotNull {
            when (it) {
                "invalid" -> RegistrationFail.InvalidEmail
                "unique" -> RegistrationFail.NotUniqueEmail
                else -> null
            }
        }.plus(
            incorrectData.password.mapNotNull {
                when (it) {
                    "password_to_short" -> RegistrationFail.ShortPassword
                    "password_to_common" -> RegistrationFail.CommonPassword
                    "password_entirely_numeric" -> RegistrationFail.EntirelyNumericPassword
                    else -> null
                }
            }
        ).toSet()
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<SignInFail>, UserNetworkDto> =
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
                    RequestFailure(handleSignInFail(it))
                else
                    RequestFailure(toNetworkFail(it))
            }
        }

    @UseExperimental(UnstableDefault::class)
    private fun handleSignInFail(response: ServerFailure.Response): SignInFail {
        val incorrectData = Json.parse(SignInIncorrectData.serializer(), response.body)
        return when {
            incorrectData.accountDeleted != null -> SignInFail.AccountDeleted
            else -> SignInFail.IncorrectEmailOrPassword
        }
    }

    override suspend fun update(
        user: UserNetworkDto
    ): Either<NetworkFailure, Unit> =
        with(networkSender) {
            handle {
                patch<Unit>("$baseUrl/api/$apiVersion/users/info/") {
                    body = FormDataContent(
                        Parameters.build {
                            append("first_name", user.firstName)
                            append("last_name", user.lastName)
                        }
                    )
                }
            }.mapLeft(::toNetworkFail)
        }
}
