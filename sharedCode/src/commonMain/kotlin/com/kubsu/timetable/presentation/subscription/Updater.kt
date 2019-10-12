package com.kubsu.timetable.presentation.subscription

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val subscriptionUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        Action.LoadFacultyList ->
            UpdateResponse(
                state = state.copy(progress = true),
                sideEffects = setOf(SideEffect.SelectFacultyList)
            )

        is Action.FacultyWasSelected ->
            UpdateResponse(
                state = state.copy(
                    selectedFaculty = action.faculty,
                    occupationList = emptyList(),
                    groupList = emptyList(),
                    subgroupList = emptyList(),
                    selectedOccupation = null,
                    selectedGroup = null,
                    selectedSubgroup = null,
                    progress = true
                ),
                sideEffects = setOf(SideEffect.SelectOccupationList(action.faculty))
            )

        is Action.OccupationWasSelected ->
            UpdateResponse(
                state = state.copy(
                    selectedOccupation = action.occupation,
                    groupList = emptyList(),
                    subgroupList = emptyList(),
                    selectedGroup = null,
                    selectedSubgroup = null,
                    progress = true
                ),
                sideEffects = setOf(SideEffect.SelectGroupList(action.occupation))
            )

        is Action.GroupWasSelected ->
            UpdateResponse(
                state = state.copy(
                    selectedGroup = action.group,
                    subgroupList = emptyList(),
                    selectedSubgroup = null,
                    progress = true
                ),
                sideEffects = setOf(SideEffect.SelectSubgroupList(action.group))
            )

        is Action.SubgroupWasSelected ->
            UpdateResponse(
                state = state.copy(
                    selectedSubgroup = action.subgroup
                )
            )

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

        Action.SubscriptionWasCreated ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.Navigate(Screen.TimetableScreen)
            )

        is Action.ShowDataError ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowError(action.failure)
            )

        is Action.ShowRequestError ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowRequestError(action.failure)
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