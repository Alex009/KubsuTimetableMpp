package com.kubsu.timetable.fragments.registration

import android.os.Bundle
import android.view.View
import com.egroden.teaco.*
import com.kubsu.timetable.R
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.RegistrationFail
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.registration.*
import com.kubsu.timetable.utils.*
import com.kubsu.timetable.utils.logics.Keyboard
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.registration_fragment.view.*

class RegistrationFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>,
    stateParser: StateParser<State>
) : BaseFragment(R.layout.registration_fragment) {
    private val connector by androidConnectors(teaFeature, stateParser)

    private val progressEffect = UiEffect(Visibility.INVISIBLE)
    private val emailErrorEffect = UiEffect<Int?>(null)
    private val passwordErrorEffect = UiEffect<Int?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        info("onViewCreated")
        progressEffect bind { view.progress_bar.visibility(it) }
        emailErrorEffect bind { it?.let(view.email_edit_text::showError) }
        passwordErrorEffect bind { it?.let(view.password_edit_text::showError) }

        view.registration_button.setOnClickListener {
            info("registration_button: hide keyboard")
            Keyboard.hide(view)
            info("registration_button: bindAction")
            connector bindAction Action.Registration(
                email = view.email_edit_text.text.toString(),
                password = view.password_edit_text.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        info("onDestroyView")
        progressEffect.unbind()
        emailErrorEffect.unbind()
        passwordErrorEffect.unbind()
    }

    private fun render(state: State) {
        info("Render: $state")
        progressEffect.value = if (state.progress) Visibility.VISIBLE else Visibility.INVISIBLE
    }

    private fun render(subscription: Subscription) {
        info("Render: $subscription")
        when (subscription) {
            is Subscription.Navigate -> navigation(subscription.screen)
            is Subscription.ShowFailure -> showFailure(subscription.failure)
        }
    }

    private fun navigation(screen: Screen) {
        getNavControllerOrNull()?.navigate(
            when (screen) {
                Screen.SignIn ->
                    RegistrationFragmentDirections.actionRegistrationFragmentToSignInFragment()
            }
        )
    }

    private fun showFailure(failure: RequestFailure<List<RegistrationFail>>) =
        failure.handle(
            ifDomain = { it.forEach(::handleRegistrationFail) },
            ifData = { it.forEach(::notifyUserOfFailure)}
        )

    private fun handleRegistrationFail(fail: RegistrationFail) =
        when (fail) {
            is RegistrationFail.Email ->
                emailErrorEffect.value = when (fail) {
                    RegistrationFail.Email.Invalid -> R.string.invalid_email
                    RegistrationFail.Email.NotUnique -> R.string.non_unique_email
                }
            is RegistrationFail.Password ->
                passwordErrorEffect.value = when (fail) {
                    RegistrationFail.Password.TooCommon -> R.string.password_is_too_common
                    RegistrationFail.Password.EntirelyNumeric -> R.string.password_entirely_numeric
                    RegistrationFail.Password.TooShort -> R.string.password_is_too_short
                }
        }
}