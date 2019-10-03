package com.kubsu.timetable.data.network.client.user

import com.kubsu.timetable.AuthFail
import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.WrapperFailure
import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.network.sender.NetworkSender
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.http.Parameters

class UserInfoNetworkClientImpl(
    private val networkSender: NetworkSender
) : UserInfoNetworkClient {
    override suspend fun registration(
        email: String,
        password: String
    ): Either<WrapperFailure<AuthFail>, Unit> = with(networkSender) {
        handle {
            post<Unit>("$baseUrl/api/$apiVersion/users/registration/") {
                body = FormDataContent(
                    Parameters.build {
                        append("email", email)
                        append("password", password)
                    }
                )
            }
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Either<WrapperFailure<AuthFail>, UserNetworkDto> = with(networkSender) {
        handle {
            post<UserNetworkDto>("$baseUrl/api/$apiVersion/users/login/") {
                body = FormDataContent(
                    Parameters.build {
                        append("email", email)
                        append("password", password)
                    }
                )
            }
        }
    }

    override suspend fun update(user: UserNetworkDto): Either<NetworkFailure, Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
