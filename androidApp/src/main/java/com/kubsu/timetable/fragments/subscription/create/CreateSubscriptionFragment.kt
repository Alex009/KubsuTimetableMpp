package com.kubsu.timetable.fragments.subscription.create

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.egroden.teaco.TeaFeature
import com.egroden.teaco.androidConnectors
import com.egroden.teaco.bindAction
import com.egroden.teaco.connect
import com.kubsu.timetable.R
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.subscription.create.*
import com.kubsu.timetable.presentation.subscription.model.FacultyModel
import com.kubsu.timetable.presentation.subscription.model.GroupModel
import com.kubsu.timetable.presentation.subscription.model.OccupationModel
import com.kubsu.timetable.presentation.subscription.model.SubgroupModel
import com.kubsu.timetable.utils.*
import kotlinx.android.synthetic.main.create_subscription_fragment.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*

class CreateSubscriptionFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>
) : BaseFragment(R.layout.create_subscription_fragment) {
    private val connector by androidConnectors(teaFeature) { bindAction(Action.LoadFacultyList) }

    private val progressEffect = UiEffect(Visibility.INVISIBLE)

    private val facultyListEffect = UiEffect<List<FacultyModel>>(emptyList())
    private val occupationListEffect = UiEffect<List<OccupationModel>>(emptyList())
    private val groupListEffect = UiEffect<List<GroupModel>>(emptyList())
    private val subgroupListEffect = UiEffect<List<SubgroupModel>>(emptyList())

    private val chooseFacultyEffect = UiEffect(Unit)
    private val chooseOccupationEffect = UiEffect(Unit)
    private val chooseGroupEffect = UiEffect(Unit)
    private val chooseSubgroupEffect = UiEffect(Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressEffect bind { view.progress_bar.visibility(it) }

        view.faculty_spinner.setData(
            listEffect = facultyListEffect,
            convert = { it.title },
            chooseEffect = chooseFacultyEffect,
            errorRes = R.string.choose_faculty
        )
        view.occupation_spinner.setData(
            listEffect = occupationListEffect,
            convert = { it.title },
            chooseEffect = chooseOccupationEffect,
            errorRes = R.string.choose_occupation
        )
        view.group_spinner.setData(
            listEffect = groupListEffect,
            convert = { it.number.toString() },
            chooseEffect = chooseGroupEffect,
            errorRes = R.string.choose_group
        )
        view.subgroup_spinner.setData(
            listEffect = subgroupListEffect,
            convert = { it.number.toString() },
            chooseEffect = chooseSubgroupEffect,
            errorRes = R.string.choose_subgroup
        )
        view.create_button.setOnClickListener {
            connector bindAction Action.CreateSubscription(
                subscriptionName =,
                isMain =
            )
        }
    }

    private fun <T> Spinner.setData(
        listEffect: UiEffect<List<T>>,
        convert: (T) -> String,
        chooseEffect: UiEffect<Unit>,
        errorRes: Int
    ) {
        val arrayAdapter = createArrayAdapter()
        adapter = arrayAdapter
        listEffect bind { list -> arrayAdapter.addAll(list.map(convert)) }
        chooseEffect bind { showError(errorRes) }
    }

    private fun View.createArrayAdapter(): ArrayAdapter<String> =
        ArrayAdapter(context, android.R.layout.simple_spinner_item, emptyList<String>()).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        progressEffect.unbind()

        facultyListEffect.unbind()
        occupationListEffect.unbind()
        groupListEffect.unbind()
        subgroupListEffect.unbind()

        chooseFacultyEffect.unbind()
        chooseOccupationEffect.unbind()
        chooseGroupEffect.unbind()
        chooseSubgroupEffect.unbind()
    }

    private fun render(state: State) {
        progressEffect.value = if (state.progress) Visibility.VISIBLE else Visibility.INVISIBLE
        facultyListEffect.value = state.facultyList
        occupationListEffect.value = state.occupationList
        groupListEffect.value = state.groupList
        subgroupListEffect.value = state.subgroupList
    }

    private fun render(subscription: Subscription) =
        when (subscription) {
            is Subscription.Navigate ->
                navigation(subscription.screen)

            is Subscription.ShowFailure ->
                notifyUserOfFailure(subscription.failure)
            is Subscription.ShowRequestFailure ->
                showRequestFailure(subscription.failure)

            is Subscription.ChooseFaculty ->
                chooseFacultyEffect.value = Unit
            is Subscription.ChooseOccupation ->
                chooseOccupationEffect.value = Unit
            is Subscription.ChooseGroup ->
                chooseGroupEffect.value = Unit
            is Subscription.ChooseSubgroup ->
                chooseSubgroupEffect.value = Unit
        }

    private fun navigation(screen: Screen) {
        getNavControllerOrNull()?.navigate(
            when (screen) {
                is Screen.TimetableScreen ->
                    CreateSubscriptionFragmentDirections
                        .actionCreateSubscriptionFragmentToTimetableFragment(screen.subscription)
            }
        )
    }

    private fun showRequestFailure(failure: RequestFailure<List<SubscriptionFail>>) =
        failure.handle(
            ifDomain = { it.forEach(::handleSubscriptionFail) },
            ifData = { it.forEach(::notifyUserOfFailure) }
        )

    private fun handleSubscriptionFail(failure: SubscriptionFail) =
        when (failure) {
            SubscriptionFail.TooLongTitle ->

        }
}