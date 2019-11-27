package com.kubsu.timetable.presentation.registration

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.fold
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

class RegistrationEffectHandler(
    private val authInteractor: AuthInteractor
) : EffectHandler<Registration.SideEffect, Registration.Action> {
    override fun invoke(sideEffect: Registration.SideEffect): Flow<Registration.Action> = flow {
        when (sideEffect) {
            is Registration.SideEffect.Registration ->
                authInteractor
                    .registrationUser(
                        email = sideEffect.email,
                        password = sideEffect.password
                    )
                    .fold(
                        ifLeft = { requestFailure ->
                            requestFailure.handle(
                                ifDomain = { emit(Registration.Action.ShowRegistrationFailure(it)) },
                                ifData = { emit(Registration.Action.ShowDataFailure(it)) }
                            )
                        },
                        ifRight = { startSignIn(sideEffect.email, sideEffect.password) }
                    )
        }.checkWhenAllHandled()
    }

    private suspend fun FlowCollector<Registration.Action>.startSignIn(
        email: String,
        password: String
    ) =
        authInteractor
            .signInTransaction(email, password)
            .fold(
                ifLeft = { emit(Registration.Action.ShowDataFailure(it.data ?: emptyList())) },
                ifRight = {
                    emit(Registration.Action.ShowResult)
                }
            )
}