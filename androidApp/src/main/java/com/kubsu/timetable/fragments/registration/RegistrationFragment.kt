package com.kubsu.timetable.fragments.registration

import android.os.Bundle
import android.view.View
import com.egroden.teaco.*
import com.kubsu.timetable.R
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.registration.*
import com.kubsu.timetable.utils.getNavControllerOrNull
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.registration_fragment.view.*

class RegistrationFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>,
    stateParser: StateParser<State>
) : BaseFragment(R.layout.registration_fragment) {
    private val connector by androidConnectors(teaFeature, stateParser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.registration_button.setOnClickListener {
            connector bindAction Action.Registration(
                email = view.email_edit_text.text.toString(),
                password = view.password_edit_text.text.toString()
            )
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
                Screen.SignIn ->
                    RegistrationFragmentDirections.actionRegistrationFragmentToSignInFragment()
            }
        )
    }

    private fun showFailure(failure: RequestFailure<List<UserInfoFail>>) =
        failure.handle(
            ifDomain = {}, //TODO
            ifData = {} //TODO
        )
}
