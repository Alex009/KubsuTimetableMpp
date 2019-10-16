package com.kubsu.timetable.presentation.subscription.create

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val createSubscriptionUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        Action.LoadFacultyList -> {
            UpdateResponse(
                state = state.copy(progress = true),
                sideEffects = setOf(SideEffect.SelectFacultyList)
            )
        }

        is Action.FacultyWasSelected -> {
            val faculty = action.id?.let(state.facultyList::get)
            UpdateResponse(
                state = state.copy(
                    selectedFaculty = faculty,
                    occupationList = emptyList(),
                    groupList = emptyList(),
                    subgroupList = emptyList(),
                    selectedOccupation = null,
                    selectedGroup = null,
                    selectedSubgroup = null,
                    progress = true
                ),
                sideEffects = if (faculty != null)
                    setOf(SideEffect.SelectOccupationList(faculty))
                else
                    emptySet()
            )
        }

        is Action.OccupationWasSelected -> {
            val occupation = action.id?.let(state.occupationList::get)
            UpdateResponse(
                state = state.copy(
                    selectedOccupation = occupation,
                    groupList = emptyList(),
                    subgroupList = emptyList(),
                    selectedGroup = null,
                    selectedSubgroup = null,
                    progress = true
                ),
                sideEffects = if (occupation != null)
                    setOf(SideEffect.SelectGroupList(occupation))
                else
                    emptySet()
            )
        }

        is Action.GroupWasSelected -> {
            val group = action.id?.let(state.groupList::get)
            UpdateResponse(
                state = state.copy(
                    selectedGroup = group,
                    subgroupList = emptyList(),
                    selectedSubgroup = null,
                    progress = true
                ),
                sideEffects = if (group != null)
                    setOf(SideEffect.SelectSubgroupList(group))
                else
                    emptySet()
            )
        }

        is Action.SubgroupWasSelected -> {
            val subgroup = action.id?.let(state.subgroupList::get)
            UpdateResponse(
                state = state.copy(
                    selectedSubgroup = subgroup
                )
            )
        }

        is Action.CreateSubscription -> {
            when (null) {
                state.selectedFaculty ->
                    UpdateResponse<State, Subscription, SideEffect>(
                        state,
                        subscription = Subscription.ChooseFaculty
                    )

                state.selectedOccupation ->
                    UpdateResponse<State, Subscription, SideEffect>(
                        state,
                        subscription = Subscription.ChooseOccupation
                    )

                state.selectedGroup ->
                    UpdateResponse<State, Subscription, SideEffect>(
                        state,
                        subscription = Subscription.ChooseGroup
                    )

                state.selectedSubgroup ->
                    UpdateResponse<State, Subscription, SideEffect>(
                        state,
                        subscription = Subscription.ChooseSubgroup
                    )

                else ->
                    UpdateResponse<State, Subscription, SideEffect>(
                        state = state.copy(progress = true),
                        sideEffects = setOf(
                            SideEffect.CreateSubscription(
                                subgroup = state.selectedSubgroup,
                                subscriptionName = action.subscriptionName,
                                isMain = action.isMain
                            )
                        )
                    )
            }
        }

        is Action.SubscriptionWasCreated ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.Navigate(Screen.TimetableScreen(action.subscription))
            )

        is Action.ShowDataFailure ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowFailure(action.failure)
            )

        is Action.ShowRequestFailure ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowRequestFailure(action.failure)
            )

        is Action.FacultyListUploaded ->
            UpdateResponse(
                state = state.copy(
                    facultyList = action.facultyList,
                    progress = false
                )
            )

        is Action.OccupationListUploaded ->
            UpdateResponse(
                state = state.copy(
                    occupationList = action.occupationList,
                    progress = false
                )
            )

        is Action.GroupListUploaded ->
            UpdateResponse(
                state = state.copy(
                    groupList = action.groupList,
                    progress = false
                )
            )

        is Action.SubgroupListUploaded ->
            UpdateResponse(
                state = state.copy(
                    subgroupList = action.subgroupList,
                    progress = false
                )
            )
    }
}