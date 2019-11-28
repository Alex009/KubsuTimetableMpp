package com.kubsu.timetable.fragments.subscription.create

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.egroden.teaco.*
import com.kubsu.timetable.R
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.subscription.create.*
import com.kubsu.timetable.presentation.subscription.model.FacultyModel
import com.kubsu.timetable.presentation.subscription.model.GroupModel
import com.kubsu.timetable.presentation.subscription.model.OccupationModel
import com.kubsu.timetable.presentation.subscription.model.SubgroupModel
import com.kubsu.timetable.utils.*
import com.kubsu.timetable.utils.logics.Keyboard
import kotlinx.android.synthetic.main.create_subscription_fragment.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*

class CreateSubscriptionFragment(
    featureFactory: (
        oldState: SubCreateState?
    ) -> Feature<SubCreateAction, SubCreateSideEffect, SubCreateState, SubCreateSubscription>
) : BaseFragment(R.layout.create_subscription_fragment),
    Render<SubCreateState, SubCreateSubscription> {
    private val connector by androidConnectors(featureFactory) {
        bindAction(SubCreateAction.LoadFacultyList)
    }

    private val progressEffect = UiEffect(false)

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
        connector.connect(this, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subscriptionTitle = view.subscription_title_input_layout.apply {
            removeErrorAfterNewText()
            removeFocusAfterEmptyText()
        }
        with(view.toolbar) {
            setNavigationOnClickListener {
                Keyboard.hide(view)
                popBackStack()
            }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_confirm -> {
                        connector bindAction SubCreateAction.CreateSubscription(
                            subscriptionName = subscriptionTitle
                                .text
                                .takeIf { it.isNotBlank() }
                                ?: view.subscription_title_input_layout.hint.toString(),
                            isMain = view.is_main_switch.isChecked
                        )
                        true
                    }

                    else -> false
                }
            }
        }

        progressEffect bind view.progress_bar::setVisibleStatus
        titleTextEffect bind subscriptionTitle::setHint
        titleErrorEffect bind subscriptionTitle::showErrorMessage

        view.faculty_spinner.setData(
            parentView = view,
            listEffect = facultyListEffect,
            convert = { it.title },
            chooseEffect = chooseFacultyEffect,
            errorRes = R.string.choose_faculty,
            onItemSelected = { connector bindAction SubCreateAction.FacultyWasSelected(it) }
        )
        view.occupation_spinner.setData(
            parentView = view,
            listEffect = occupationListEffect,
            convert = { it.title },
            chooseEffect = chooseOccupationEffect,
            errorRes = R.string.choose_occupation,
            onItemSelected = { connector bindAction SubCreateAction.OccupationWasSelected(it) }
        )
        view.group_spinner.setData(
            parentView = view,
            listEffect = groupListEffect,
            convert = { it.number.toString() },
            chooseEffect = chooseGroupEffect,
            errorRes = R.string.choose_group,
            onItemSelected = { connector bindAction SubCreateAction.GroupWasSelected(it) }
        )
        view.subgroup_spinner.setData(
            parentView = view,
            listEffect = subgroupListEffect,
            convert = { it.number.toString() },
            chooseEffect = chooseSubgroupEffect,
            errorRes = R.string.choose_subgroup,
            onItemSelected = { connector bindAction SubCreateAction.SubgroupWasSelected(it) }
        )
    }

    private fun <T> Spinner.setData(
        parentView: View,
        listEffect: UiEffect<List<T>>,
        convert: (T) -> String,
        chooseEffect: UiEffect<Unit>,
        errorRes: Int,
        onItemSelected: (Int?) -> Unit
    ) {
        val arrayAdapter = createArrayAdapter()
        adapter = arrayAdapter
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            private var check = 0 // fix spinner onItemSelected called automatically

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (++check > 1) {
                    parentView.subscription_title_input_layout.clearFocus()
                    Keyboard.hide(this@setData)
                    onItemSelected(if (position != 0) position - 1 else null)
                }
            }
        }
        listEffect bind { list ->
            arrayAdapter.run {
                clear()
                add(getString(R.string.not_selected))
                addAll(list.map(convert))
            }
        }
        chooseEffect bind { showErrorMessage(errorRes) }
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

    override fun renderState(state: SubCreateState) {
        progressEffect.value = state.progress
        facultyListEffect.value = state.facultyList
        occupationListEffect.value = state.occupationList
        groupListEffect.value = state.groupList
        subgroupListEffect.value = state.subgroupList
        titleTextEffect.value = state.nameHint ?: getString(R.string.subscription_title)
    }

    override fun renderSubscription(subscription: SubCreateSubscription) =
        when (subscription) {
            is SubCreateSubscription.Navigate ->
                navigation(subscription.screen)
            is SubCreateSubscription.ShowFailure ->
                subscription.failureList.forEach(::notifyUserOfFailure)
            is SubCreateSubscription.ShowSubscriptionFailure ->
                subscription.failureList.forEach(::handleSubscriptionFail)
            is SubCreateSubscription.ChooseFaculty ->
                chooseFacultyEffect.value = Unit
            is SubCreateSubscription.ChooseOccupation ->
                chooseOccupationEffect.value = Unit
            is SubCreateSubscription.ChooseGroup ->
                chooseGroupEffect.value = Unit
            is SubCreateSubscription.ChooseSubgroup ->
                chooseSubgroupEffect.value = Unit
        }

    private fun navigation(screen: SubCreateScreen) =
        safeNavigate(
            when (screen) {
                SubCreateScreen.TimetableScreen ->
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
        }
}