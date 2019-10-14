package com.kubsu.timetable.fragments.signin

import android.os.Bundle
import android.view.View
import com.egroden.teaco.*
import com.kubsu.timetable.R
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.signin.*
import com.kubsu.timetable.utils.getNavControllerOrNull
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.sign_in_fragment.view.*

class SignInFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>,
    stateParser: StateParser<State>
) : BaseFragment(R.layout.sign_in_fragment) {
    private val connector by androidConnectors(teaFeature, stateParser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.sign_in_button.setOnClickListener {
            connector bindAction Action.SignIn(
                email = view.email_edit_text.text.toString(),
                password = view.password_edit_text.text.toString()
            )
        }
        view.registration_button.setOnClickListener {
            connector bindAction Action.Registration
        }
    }

    private fun render(state: State) {
        view?.run {
            progress_bar.visibility = if (state.progress) View.VISIBLE else View.INVISIBLE
        }
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
                    SignInFragmentDirections.actionSignInFragmentToRegistrationFragment()
            }
        )
    }

    private fun showFailure(failure: RequestFailure<List<SignInFail>>) =
        failure.handle(
            ifDomain = {}, //TODO
            ifData = {} //TODO
        )
}
