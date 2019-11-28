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
) : EffectHandler<RegistrationSideEffect, RegistrationAction> {
    override fun invoke(sideEffect: RegistrationSideEffect): Flow<RegistrationAction> = flow {
        when (sideEffect) {
            is RegistrationSideEffect.Registration ->
                authInteractor
                    .registrationUser(
                        email = sideEffect.email,
                        password = sideEffect.password
                    )
                    .fold(
                        ifLeft = { requestFailure ->
                            requestFailure.handle(
                                ifDomain = { emit(RegistrationAction.ShowRegistrationFailure(it)) },
                                ifData = { emit(RegistrationAction.ShowDataFailure(it)) }
                            )
                        },
                        ifRight = { startSignIn(sideEffect.email, sideEffect.password) }
                    )
        }.checkWhenAllHandled()
    }

    private suspend fun FlowCollector<RegistrationAction>.startSignIn(
        email: String,
        password: String
    ) =
        authInteractor
            .signInTransaction(email, password)
            .fold(
                ifLeft = { emit(RegistrationAction.ShowDataFailure(it.data ?: emptyList())) },
                ifRight = {
                    emit(RegistrationAction.ShowResult)
                }
            )
}