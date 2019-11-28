package com.kubsu.timetable.presentation.signin

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.fold
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInEffectHandler(
    private val authInteractor: AuthInteractor
) : EffectHandler<SignInSideEffect, SignInAction> {
    override fun invoke(sideEffect: SignInSideEffect): Flow<SignInAction> = flow {
        when (sideEffect) {
            is SignInSideEffect.Authenticate ->
                authInteractor
                    .signInTransaction(sideEffect.email, sideEffect.password)
                    .fold(
                        ifLeft = { requestFailure ->
                            requestFailure.handle(
                                ifDomain = { emit(SignInAction.ShowSignInFailure(it)) },
                                ifData = { emit(SignInAction.ShowDataFailure(it)) }
                            )
                        },
                        ifRight = { emit(SignInAction.ShowResult) }
                    )
        }.checkWhenAllHandled()
    }
}