package com.kubsu.timetable.fragments.registration

import android.os.Bundle
import android.view.View
import com.egroden.teaco.Feature
import com.egroden.teaco.androidConnectors
import com.egroden.teaco.bindAction
import com.egroden.teaco.connect
import com.kubsu.timetable.BaseNavGraphDirections
import com.kubsu.timetable.R
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.registration.*
import com.kubsu.timetable.utils.*
import com.kubsu.timetable.utils.logics.Keyboard
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.registration_fragment.view.*

class RegistrationFragment(
    featureFactory: (oldState: State?) -> Feature<Action, SideEffect, State, Subscription>
) : BaseFragment(R.layout.registration_fragment) {
    private val connector by androidConnectors(featureFactory)

    private val progressEffect = UiEffect(Visibility.INVISIBLE)
    private val emailErrorEffect = UiEffect(0)
    private val passwordErrorEffect = UiEffect(0)
    private val passwordsVaryEffects = UiEffect(Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view.toolbar) {
            setNavigationOnClickListener { popBackStack() }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_registration -> {
                        Keyboard.hide(view)
                        connector bindAction Action.Registration(
                            email = view.email_edit_text.text.toString(),
                            password = view.password_edit_text.text.toString(),
                            repeatedPassword = view.repeat_password_edit_text.text.toString()
                        )
                        true
                    }

                    else -> false
                }
            }
        }

        progressEffect bind { view.progress_bar.visibility(it) }
        emailErrorEffect bind view.email_edit_text::showErrorMessage
        passwordErrorEffect bind view.password_edit_text::showErrorMessage
        passwordsVaryEffects bind {
            view.password_edit_text.showErrorMessage(R.string.passwords_vary)
            view.repeat_password_edit_text.showErrorMessage(R.string.passwords_vary)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressEffect.unbind()
        emailErrorEffect.unbind()
        passwordErrorEffect.unbind()
        passwordsVaryEffects.unbind()
    }

    private fun render(state: State) {
        progressEffect.value = if (state.progress) Visibility.VISIBLE else Visibility.INVISIBLE
    }

    private fun render(subscription: Subscription) =
        when (subscription) {
            is Subscription.Navigate ->
                navigation(subscription.screen)
            is Subscription.ShowRegistrationFailure ->
                subscription.failureList.forEach(::handleRegistrationFail)
            is Subscription.ShowDataFailure ->
                subscription.failureList.forEach(::notifyUserOfFailure)
            Subscription.PasswordsVary ->
                passwordsVaryEffects.value = Unit
        }

    private fun navigation(screen: Screen) =
        safeNavigate(
            when (screen) {
                Screen.SignIn ->
                    BaseNavGraphDirections.actionGlobalSignInFragment()
            }
        )

    private fun handleRegistrationFail(fail: UserInfoFail) =
        when (fail) {
            is UserInfoFail.Email ->
                emailErrorEffect.value = when (fail) {
                    UserInfoFail.Email.Invalid -> R.string.invalid_email
                    UserInfoFail.Email.NotUnique -> R.string.non_unique_email
                    UserInfoFail.Email.Required -> R.string.required
                }
            is UserInfoFail.Password ->
                passwordErrorEffect.value = when (fail) {
                    UserInfoFail.Password.TooCommon -> R.string.password_is_too_common
                    UserInfoFail.Password.EntirelyNumeric -> R.string.password_entirely_numeric
                    UserInfoFail.Password.TooShort -> R.string.password_is_too_short
                    UserInfoFail.Password.Required -> R.string.required
                }
            is UserInfoFail.FirstName -> Unit
            is UserInfoFail.LastName -> Unit
        }
}