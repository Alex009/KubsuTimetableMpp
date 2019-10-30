package com.kubsu.timetable.fragments.signin

import android.os.Bundle
import android.view.View
import com.egroden.teaco.Feature
import com.egroden.teaco.androidConnectors
import com.egroden.teaco.bindAction
import com.egroden.teaco.connect
import com.kubsu.timetable.R
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.signin.*
import com.kubsu.timetable.utils.*
import com.kubsu.timetable.utils.logics.Keyboard
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.sign_in_fragment.view.*

class SignInFragment(
    featureFactory: (oldState: State?) -> Feature<Action, SideEffect, State, Subscription>
) : BaseFragment(R.layout.sign_in_fragment) {
    private val connector by androidConnectors(featureFactory)

    private val progressEffect = UiEffect(false)
    private val emailErrorEffect = UiEffect(0)
    private val passwordErrorEffect = UiEffect(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view.toolbar) {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_sign_in -> {
                        Keyboard.hide(view)
                        connector bindAction Action.SignIn(
                            email = view.email_edit_text.text.toString(),
                            password = view.password_edit_text.text.toString()
                        )
                        true
                    }

                    else -> false
                }
            }
        }

        progressEffect bind {
            with(view.progress_bar) {
                if (it) show() else hide()
            }
        }
        emailErrorEffect bind view.email_edit_text::showErrorMessage
        passwordErrorEffect bind view.password_edit_text::showErrorMessage

        view.create_account_button.setOnClickListener {
            Keyboard.hide(view)
            connector bindAction Action.Registration
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressEffect.unbind()
        emailErrorEffect.unbind()
        passwordErrorEffect.unbind()
    }

    private fun render(state: State) {
        progressEffect.value = state.progress
    }

    private fun render(subscription: Subscription) =
        when (subscription) {
            is Subscription.Navigate ->
                navigation(subscription.screen)
            is Subscription.ShowSignInFailure ->
                subscription.failureList.forEach(::handleSignInFail)
            is Subscription.ShowDataFailure ->
                subscription.failureList.forEach(::notifyUserOfFailure)
        }

    private fun navigation(screen: Screen) =
        safeNavigate(
            when (screen) {
                Screen.Registration ->
                    SignInFragmentDirections.actionSignInFragmentToRegistrationFragment()
                Screen.Timetable ->
                    SignInFragmentDirections.actionSignInFragmentToBottomNavFragment()
            }
        )

    private fun handleSignInFail(fail: SignInFail) =
        when (fail) {
            SignInFail.AccountInactivate ->
                emailErrorEffect.value = R.string.account_is_blocked
            SignInFail.IncorrectEmailOrPassword ->
                emailErrorEffect.value = R.string.incorrect_emai_or_password
            SignInFail.InvalidEmail ->
                emailErrorEffect.value = R.string.invalid_email
            SignInFail.RequiredEmail ->
                emailErrorEffect.value = R.string.required
            SignInFail.RequiredPassword ->
                passwordErrorEffect.value = R.string.required
        }
}
