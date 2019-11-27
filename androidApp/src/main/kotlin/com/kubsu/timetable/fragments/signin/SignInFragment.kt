package com.kubsu.timetable.fragments.signin

import android.os.Bundle
import android.view.View
import com.egroden.teaco.*
import com.kubsu.timetable.R
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.signin.SignIn
import com.kubsu.timetable.utils.*
import com.kubsu.timetable.utils.logics.Keyboard
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.sign_in_fragment.view.*

class SignInFragment(
    featureFactory: (
        oldState: SignIn.State?
    ) -> Feature<SignIn.Action, SignIn.SideEffect, SignIn.State, SignIn.Subscription>
) : BaseFragment(R.layout.sign_in_fragment),
    Render<SignIn.State, SignIn.Subscription> {
    private val connector by androidConnectors(featureFactory)

    private val progressEffect = UiEffect(false)
    private val emailErrorEffect = UiEffect(0)
    private val passwordErrorEffect = UiEffect(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(this, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view.toolbar) {
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_sign_in -> {
                        Keyboard.hide(view)
                        connector bindAction SignIn.Action.SignIn(
                            email = view.email_input_layout.text,
                            password = view.password_input_layout.text
                        )
                        true
                    }

                    else -> false
                }
            }
        }

        progressEffect bind view.progress_bar::setVisibleStatus
        view.email_input_layout.removeErrorAfterNewText()
        view.password_input_layout.removeErrorAfterNewText()
        emailErrorEffect bind view.email_input_layout::showErrorMessage
        passwordErrorEffect bind view.password_input_layout::showErrorMessage

        view.registration_clickable_text_view.setOnClickListener {
            Keyboard.hide(view)
            connector bindAction SignIn.Action.Registration
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressEffect.unbind()
        emailErrorEffect.unbind()
        passwordErrorEffect.unbind()
    }

    override fun renderState(state: SignIn.State) {
        progressEffect.value = state.progress
    }

    override fun renderSubscription(subscription: SignIn.Subscription) =
        when (subscription) {
            is SignIn.Subscription.Navigate ->
                navigation(subscription.screen)
            is SignIn.Subscription.ShowSignInFailure ->
                subscription.failureList.forEach(::handleSignInFail)
            is SignIn.Subscription.ShowDataFailure ->
                subscription.failureList.forEach(::notifyUserOfFailure)
        }

    private fun navigation(screen: SignIn.Screen) =
        safeNavigate(
            when (screen) {
                SignIn.Screen.Registration ->
                    SignInFragmentDirections.actionSignInFragmentToRegistrationFragment()
                SignIn.Screen.Timetable ->
                    SignInFragmentDirections.actionSignInFragmentToBottomNavFragment()
            }
        )

    private fun handleSignInFail(fail: SignInFail) =
        when (fail) {
            SignInFail.AccountInactivate ->
                emailErrorEffect.value = R.string.account_is_blocked
            SignInFail.InvalidEmail ->
                emailErrorEffect.value = R.string.invalid_email
            SignInFail.RequiredEmail ->
                emailErrorEffect.value = R.string.required
            SignInFail.RequiredPassword ->
                passwordErrorEffect.value = R.string.required
            SignInFail.IncorrectEmailOrPassword -> {
                emailErrorEffect.value = R.string.incorrect_emai_or_password
                passwordErrorEffect.value = R.string.incorrect_emai_or_password
            }
        }
}
