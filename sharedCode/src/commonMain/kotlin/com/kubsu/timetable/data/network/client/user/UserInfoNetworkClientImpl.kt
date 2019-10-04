package com.kubsu.timetable.data.network.client.user

import com.kubsu.timetable.AuthFail
import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.RequestFailure
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
    ): Either<RequestFailure<List<AuthFail>>, Unit> =
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
                    RequestFailure(handleResponseFail(it))
                else
                    RequestFailure(toNetworkFail(it))
            }
        }

    override suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<List<AuthFail>>, UserNetworkDto> =
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
                    RequestFailure(handleResponseFail(it))
                else
                    RequestFailure(toNetworkFail(it))
            }
        }

    @UseExperimental(UnstableDefault::class)
    private fun handleResponseFail(response: ServerFailure.Response): List<AuthFail> {
        val message = Json.parse(RegistrationIncorrectData.serializer(), response.body)
        println(message)
        return emptyList()
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
