package com.kubsu.timetable.fragments.registration

import android.os.Bundle
import android.view.View
import com.egroden.teaco.TeaFeature
import com.egroden.teaco.androidConnectors
import com.egroden.teaco.bindAction
import com.egroden.teaco.connect
import com.kubsu.timetable.R
import com.kubsu.timetable.RegistrationFail
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.registration.*
import com.kubsu.timetable.utils.*
import com.kubsu.timetable.utils.logics.Keyboard
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.registration_fragment.view.*

class RegistrationFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>
) : BaseFragment(R.layout.registration_fragment) {
    private val connector by androidConnectors(teaFeature)

    private val progressEffect = UiEffect(Visibility.INVISIBLE)
    private val emailErrorEffect = UiEffect(0)
    private val passwordErrorEffect = UiEffect(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressEffect bind { view.progress_bar.visibility(it) }
        emailErrorEffect bind view.email_edit_text::showErrorMessage
        passwordErrorEffect bind view.password_edit_text::showErrorMessage

        view.registration_button.setOnClickListener {
            Keyboard.hide(view)
            connector bindAction Action.Registration(
                email = view.email_edit_text.text.toString(),
                password = view.password_edit_text.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressEffect.unbind()
        emailErrorEffect.unbind()
        passwordErrorEffect.unbind()
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
        safeNavigate(
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
                    RegistrationFail.Email.Required -> R.string.required
                }
            is RegistrationFail.Password ->
                passwordErrorEffect.value = when (fail) {
                    RegistrationFail.Password.TooCommon -> R.string.password_is_too_common
                    RegistrationFail.Password.EntirelyNumeric -> R.string.password_entirely_numeric
                    RegistrationFail.Password.TooShort -> R.string.password_is_too_short
                    RegistrationFail.Password.Required -> R.string.required
                }
        }
}