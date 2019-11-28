package com.kubsu.timetable.fragments.signin

import android.os.Bundle
import android.view.View
import com.egroden.teaco.*
import com.kubsu.timetable.R
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.signin.*
import com.kubsu.timetable.utils.*
import com.kubsu.timetable.utils.logics.Keyboard
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.sign_in_fragment.view.*

class SignInFragment(
    featureFactory: (
        oldState: SignInState?
    ) -> Feature<SignInAction, SignInSideEffect, SignInState, SignInSubscription>
) : BaseFragment(R.layout.sign_in_fragment),
    Render<SignInState, SignInSubscription> {
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
                        connector bindAction SignInAction.SignIn(
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
            connector bindAction SignInAction.Registration
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressEffect.unbind()
        emailErrorEffect.unbind()
        passwordErrorEffect.unbind()
    }

    override fun renderState(state: SignInState) {
        progressEffect.value = state.progress
    }

    override fun renderSubscription(subscription: SignInSubscription) =
        when (subscription) {
            is SignInSubscription.Navigate ->
                navigation(subscription.screen)
            is SignInSubscription.ShowSignInFailure ->
                subscription.failureList.forEach(::handleSignInFail)
            is SignInSubscription.ShowDataFailure ->
                subscription.failureList.forEach(::notifyUserOfFailure)
        }

    private fun navigation(screen: SignInScreen) =
        safeNavigate(
            when (screen) {
                SignInScreen.Registration ->
                    SignInFragmentDirections.actionSignInFragmentToRegistrationFragment()
                SignInScreen.Timetable ->
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
