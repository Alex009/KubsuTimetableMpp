package com.kubsu.timetable.fragments.registration

import android.os.Bundle
import android.view.View
import com.egroden.teaco.*
import com.kubsu.timetable.R
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.registration.*
import com.kubsu.timetable.utils.*
import com.kubsu.timetable.utils.logics.Keyboard
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.registration_fragment.view.*

class RegistrationFragment(
    featureFactory: (
        oldState: RegistrationState?
    ) -> Feature<RegistrationAction, RegistrationSideEffect, RegistrationState, RegistrationSubscription>
) : BaseFragment(R.layout.registration_fragment),
    Render<RegistrationState, RegistrationSubscription> {
    private val connector by androidConnectors(featureFactory)

    private val progressEffect = UiEffect(false)
    private val emailErrorEffect = UiEffect(0)
    private val passwordErrorEffect = UiEffect(0)
    private val passwordsVaryEffects = UiEffect(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(this, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view.toolbar) {
            setNavigationOnClickListener {
                Keyboard.hide(view)
                popBackStack()
            }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_confirm -> {
                        Keyboard.hide(view)
                        connector bindAction RegistrationAction.Registration(
                            email = view.email_input_layout.text,
                            password = view.password_input_layout.text,
                            repeatedPassword = view.repeat_password_input_layout.text
                        )
                        true
                    }

                    else -> false
                }
            }
        }

        view.email_input_layout.removeErrorAfterNewText()
        view.password_input_layout.removeErrorAfterNewText()

        progressEffect bind view.progress_bar::setVisibleStatus
        emailErrorEffect bind view.email_input_layout::showErrorMessage
        passwordErrorEffect bind view.password_input_layout::showErrorMessage
        passwordsVaryEffects bind view.repeat_password_input_layout::showErrorMessage
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressEffect.unbind()
        emailErrorEffect.unbind()
        passwordErrorEffect.unbind()
        passwordsVaryEffects.unbind()
    }

    override fun renderState(state: RegistrationState) {
        progressEffect.value = state.progress
    }

    override fun renderSubscription(subscription: RegistrationSubscription) =
        when (subscription) {
            is RegistrationSubscription.Navigate ->
                navigation(subscription.screen)
            is RegistrationSubscription.ShowRegistrationFailure ->
                subscription.failureList.forEach(::handleRegistrationFail)
            is RegistrationSubscription.ShowDataFailure ->
                subscription.failureList.forEach(::notifyUserOfFailure)
            RegistrationSubscription.PasswordsVary ->
                passwordsVaryEffects.value = R.string.passwords_vary
        }

    private fun navigation(screen: RegistrationScreen) =
        safeNavigate(
            when (screen) {
                RegistrationScreen.CreateSubscription ->
                    RegistrationFragmentDirections
                        .actionRegistrationFragmentToCreateSubscriptionFragment()
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