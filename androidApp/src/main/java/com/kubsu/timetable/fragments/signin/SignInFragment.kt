package com.kubsu.timetable.fragments.signin

import android.os.Bundle
import android.view.View
import com.egroden.teaco.TeaFeature
import com.egroden.teaco.androidConnectors
import com.egroden.teaco.bindAction
import com.egroden.teaco.connect
import com.kubsu.timetable.R
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.signin.*
import com.kubsu.timetable.utils.*
import com.kubsu.timetable.utils.logics.Keyboard
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.sign_in_fragment.view.*

class SignInFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>
) : BaseFragment(R.layout.sign_in_fragment) {
    private val connector by androidConnectors(teaFeature)

    private val progressEffect = UiEffect(Visibility.INVISIBLE)
    private val emailErrorEffect = UiEffect<Int?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressEffect bind { view.progress_bar.visibility(it) }
        emailErrorEffect bind { it?.let(view.email_edit_text::showError) }

        view.sign_in_button.setOnClickListener {
            Keyboard.hide(view)
            connector bindAction Action.SignIn(
                email = view.email_edit_text.text.toString(),
                password = view.password_edit_text.text.toString()
            )
        }
        view.registration_button.setOnClickListener {
            Keyboard.hide(view)
            connector bindAction Action.Registration
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressEffect.unbind()
        emailErrorEffect.unbind()
    }

    private fun render(state: State) {
        progressEffect.value = if (state.progress) Visibility.VISIBLE else Visibility.INVISIBLE
    }

    private fun render(subscription: Subscription) =
        when (subscription) {
            is Subscription.Navigate -> navigation(subscription.screen)
            is Subscription.ShowFailure -> showFailure(subscription.failure)
        }

    private fun navigation(screen: Screen) {
        getNavControllerOrNull()?.navigate(
            when (screen) {
                Screen.Registration ->
                    SignInFragmentDirections.actionSignInFragmentToRegistrationFragment()
                Screen.Timetable ->
                    SignInFragmentDirections.actionSignInFragmentToBottomNavGraph()
            }
        )
    }

    private fun showFailure(failure: RequestFailure<List<SignInFail>>) =
        failure.handle(
            ifDomain = { it.forEach(::handleSignInFail) },
            ifData = { it.forEach(::notifyUserOfFailure) }
        )

    private fun handleSignInFail(fail: SignInFail) {
        emailErrorEffect.value = when (fail) {
            SignInFail.AccountInactivate -> R.string.account_is_blocked
            SignInFail.IncorrectEmailOrPassword -> R.string.incorrect_emai_or_password
            SignInFail.InvalidEmail -> R.string.invalid_email
        }
    }
}
