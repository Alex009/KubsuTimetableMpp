package com.kubsu.timetable.fragments.subscription.create

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.egroden.teaco.Feature
import com.egroden.teaco.androidConnectors
import com.egroden.teaco.bindAction
import com.egroden.teaco.connect
import com.kubsu.timetable.R
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.subscription.create.*
import com.kubsu.timetable.presentation.subscription.model.FacultyModel
import com.kubsu.timetable.presentation.subscription.model.GroupModel
import com.kubsu.timetable.presentation.subscription.model.OccupationModel
import com.kubsu.timetable.presentation.subscription.model.SubgroupModel
import com.kubsu.timetable.utils.*
import com.kubsu.timetable.utils.ui.materialAlert
import kotlinx.android.synthetic.main.create_subscription_fragment.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*

class CreateSubscriptionFragment(
    featureFactory: (oldState: State?) -> Feature<Action, SideEffect, State, Subscription>
) : BaseFragment(R.layout.create_subscription_fragment) {
    private val connector by androidConnectors(featureFactory) { bindAction(Action.LoadFacultyList) }

    private val progressEffect = UiEffect(Visibility.INVISIBLE)

    private val facultyListEffect = UiEffect<List<FacultyModel>>(emptyList())
    private val occupationListEffect = UiEffect<List<OccupationModel>>(emptyList())
    private val groupListEffect = UiEffect<List<GroupModel>>(emptyList())
    private val subgroupListEffect = UiEffect<List<SubgroupModel>>(emptyList())

    private val chooseFacultyEffect = UiEffect(Unit)
    private val chooseOccupationEffect = UiEffect(Unit)
    private val chooseGroupEffect = UiEffect(Unit)
    private val chooseSubgroupEffect = UiEffect(Unit)

    private val titleTextEffect = UiEffect("")
    private val titleErrorEffect = UiEffect(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.toolbar.setNavigationOnClickListener {
            popBackStack()
        }

        progressEffect bind { view.progress_bar.visibility(it) }
        titleTextEffect bind view.subscription_title::setHint
        titleErrorEffect bind (view.subscription_title as TextView)::showErrorMessage

        view.faculty_spinner.setData(
            listEffect = facultyListEffect,
            convert = { it.title },
            chooseEffect = chooseFacultyEffect,
            errorRes = R.string.choose_faculty,
            onItemSelected = { connector bindAction Action.FacultyWasSelected(it) }
        )
        view.occupation_spinner.setData(
            listEffect = occupationListEffect,
            convert = { it.title },
            chooseEffect = chooseOccupationEffect,
            errorRes = R.string.choose_occupation,
            onItemSelected = { connector bindAction Action.OccupationWasSelected(it) }
        )
        view.group_spinner.setData(
            listEffect = groupListEffect,
            convert = { it.number.toString() },
            chooseEffect = chooseGroupEffect,
            errorRes = R.string.choose_group,
            onItemSelected = { connector bindAction Action.GroupWasSelected(it) }
        )
        view.subgroup_spinner.setData(
            listEffect = subgroupListEffect,
            convert = { it.number.toString() },
            chooseEffect = chooseSubgroupEffect,
            errorRes = R.string.choose_subgroup,
            onItemSelected = { connector bindAction Action.SubgroupWasSelected(it) }
        )
        view.create_button.setOnClickListener {
            connector bindAction Action.CreateSubscription(
                subscriptionName = view.subscription_title
                    .text.toString()
                    .takeIf { it.isNotBlank() }
                    ?: view.subscription_title.hint.toString(),
                isMain = view.is_main_switch.isChecked
            )
        }
    }

    private fun <T> Spinner.setData(
        listEffect: UiEffect<List<T>>,
        convert: (T) -> String,
        chooseEffect: UiEffect<Unit>,
        errorRes: Int,
        onItemSelected: (Int?) -> Unit
    ) {
        val arrayAdapter = createArrayAdapter()
        adapter = arrayAdapter
        listEffect bind { list ->
            arrayAdapter.run {
                clear()
                add(getString(R.string.not_selected))
                addAll(list.map(convert))
            }
        }
        chooseEffect bind { showErrorMessage(errorRes) }
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) =
                onItemSelected(null)

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) =
                onItemSelected(if (position != 0) position - 1 else null)
        }
    }

    private fun View.createArrayAdapter(): ArrayAdapter<String> =
        ArrayAdapter(context, android.R.layout.simple_spinner_item, mutableListOf<String>()).also {
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

        titleTextEffect.unbind()
        titleErrorEffect.unbind()
    }

    private fun render(state: State) {
        progressEffect.value = if (state.progress) Visibility.VISIBLE else Visibility.INVISIBLE
        facultyListEffect.value = state.facultyList
        occupationListEffect.value = state.occupationList
        groupListEffect.value = state.groupList
        subgroupListEffect.value = state.subgroupList
        titleTextEffect.value = state.nameHint ?: getString(R.string.subscription_title)
    }

    private fun render(subscription: Subscription) =
        when (subscription) {
            is Subscription.Navigate ->
                navigation(subscription.screen)
            is Subscription.ShowFailure ->
                subscription.failureList.forEach(::notifyUserOfFailure)
            is Subscription.ShowSubscriptionFailure ->
                subscription.failureList.forEach(::handleSubscriptionFail)
            is Subscription.ChooseFaculty ->
                chooseFacultyEffect.value = Unit
            is Subscription.ChooseOccupation ->
                chooseOccupationEffect.value = Unit
            is Subscription.ChooseGroup ->
                chooseGroupEffect.value = Unit
            is Subscription.ChooseSubgroup ->
                chooseSubgroupEffect.value = Unit
        }

    private fun navigation(screen: Screen) =
        safeNavigate(
            when (screen) {
                Screen.TimetableScreen ->
                    CreateSubscriptionFragmentDirections
                        .actionCreateSubscriptionFragmentToBottomNavFragment()
            }
        )

    private fun handleSubscriptionFail(failure: SubscriptionFail) =
        when (failure) {
            SubscriptionFail.TooLongTitle ->
                titleErrorEffect.value = R.string.title_too_long
            SubscriptionFail.RequiredTitle ->
                titleErrorEffect.value = R.string.required
            SubscriptionFail.SubscriptionAlreadyExists -> {
                requireActivity().materialAlert(
                    title = getString(R.string.error),
                    message = getString(R.string.subscription_already_exists),
                    onOkButtonClick = {}
                )
                Unit
            }
        }
}