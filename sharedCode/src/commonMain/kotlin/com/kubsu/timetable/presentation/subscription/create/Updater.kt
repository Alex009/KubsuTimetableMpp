package com.kubsu.timetable.presentation.subscription.create

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater
import com.kubsu.timetable.presentation.subscription.model.FacultyModel
import com.kubsu.timetable.presentation.subscription.model.GroupModel
import com.kubsu.timetable.presentation.subscription.model.OccupationModel
import com.kubsu.timetable.presentation.subscription.model.SubgroupModel

val createSubscriptionUpdater = object :
    Updater<CreateSub.State, CreateSub.Action, CreateSub.Subscription, CreateSub.SideEffect> {
    override fun invoke(
        state: CreateSub.State,
        action: CreateSub.Action
    ): UpdateResponse<CreateSub.State, CreateSub.Subscription, CreateSub.SideEffect> =
        when (action) {
            CreateSub.Action.LoadFacultyList -> {
                UpdateResponse(
                    state = state.copy(progress = true),
                    sideEffects = setOf(CreateSub.SideEffect.SelectFacultyList)
                )
            }

            is CreateSub.Action.FacultyWasSelected -> {
                val faculty = action.id?.let(state.facultyList::get)
                UpdateResponse(
                    state = state.copy(
                        occupationList = emptyList(),
                        groupList = emptyList(),
                        subgroupList = emptyList(),
                        nameHint = getHintOrNull(
                            selectedFaculty = faculty,
                            selectedOccupation = null,
                            selectedGroup = null,
                            selectedSubgroup = null
                        ),
                        selectedFaculty = faculty,
                        selectedOccupation = null,
                        selectedGroup = null,
                        selectedSubgroup = null,
                        progress = faculty != null
                    ),
                    sideEffects = if (faculty != null)
                        setOf(CreateSub.SideEffect.SelectOccupationList(faculty))
                    else
                        emptySet()
                )
            }

            is CreateSub.Action.OccupationWasSelected -> {
                val occupation = action.id?.let(state.occupationList::get)
                UpdateResponse(
                    state = state.copy(
                        groupList = emptyList(),
                        subgroupList = emptyList(),
                        nameHint = getHintOrNull(
                            selectedFaculty = state.selectedFaculty,
                            selectedOccupation = occupation,
                            selectedGroup = null,
                            selectedSubgroup = null
                        ),
                        selectedOccupation = occupation,
                        selectedGroup = null,
                        selectedSubgroup = null,
                        progress = occupation != null
                    ),
                    sideEffects = if (occupation != null)
                        setOf(CreateSub.SideEffect.SelectGroupList(occupation))
                    else
                        emptySet()
                )
            }

            is CreateSub.Action.GroupWasSelected -> {
                val group = action.id?.let(state.groupList::get)
                UpdateResponse(
                    state = state.copy(
                        subgroupList = emptyList(),
                        nameHint = getHintOrNull(
                            selectedFaculty = state.selectedFaculty,
                            selectedOccupation = state.selectedOccupation,
                            selectedGroup = group,
                            selectedSubgroup = null
                        ),
                        selectedGroup = group,
                        selectedSubgroup = null,
                        progress = group != null
                    ),
                    sideEffects = if (group != null)
                        setOf(CreateSub.SideEffect.SelectSubgroupList(group))
                    else
                        emptySet()
                )
            }

            is CreateSub.Action.SubgroupWasSelected -> {
                val subgroup = action.id?.let(state.subgroupList::get)
                UpdateResponse(
                    state = state.copy(
                        selectedSubgroup = subgroup,
                        nameHint = getHintOrNull(
                            selectedFaculty = state.selectedFaculty,
                            selectedOccupation = state.selectedOccupation,
                            selectedGroup = state.selectedGroup,
                            selectedSubgroup = subgroup
                        )
                    )
                )
            }

            is CreateSub.Action.CreateSubscription -> {
                when (null) {
                    state.selectedFaculty ->
                        UpdateResponse<CreateSub.State, CreateSub.Subscription, CreateSub.SideEffect>(
                            state,
                            subscription = CreateSub.Subscription.ChooseFaculty
                        )

                    state.selectedOccupation ->
                        UpdateResponse<CreateSub.State, CreateSub.Subscription, CreateSub.SideEffect>(
                            state,
                            subscription = CreateSub.Subscription.ChooseOccupation
                        )

                    state.selectedGroup ->
                        UpdateResponse<CreateSub.State, CreateSub.Subscription, CreateSub.SideEffect>(
                            state,
                            subscription = CreateSub.Subscription.ChooseGroup
                        )

                    state.selectedSubgroup ->
                        UpdateResponse<CreateSub.State, CreateSub.Subscription, CreateSub.SideEffect>(
                            state,
                            subscription = CreateSub.Subscription.ChooseSubgroup
                        )

                    else ->
                        UpdateResponse<CreateSub.State, CreateSub.Subscription, CreateSub.SideEffect>(
                            state = state.copy(progress = true),
                            sideEffects = setOf(
                                CreateSub.SideEffect.CreateSubscription(
                                    subgroup = state.selectedSubgroup,
                                    subscriptionName = action.subscriptionName,
                                    isMain = action.isMain
                                )
                            )
                        )
                }
            }

            is CreateSub.Action.SubscriptionWasCreated ->
                UpdateResponse(
                    state = state.copy(progress = false),
                    subscription = CreateSub.Subscription.Navigate(CreateSub.Screen.TimetableScreen),
                    sideEffects = setOf(CreateSub.SideEffect.DisplayedSubscription(action.subscription))
                )

            is CreateSub.Action.ShowDataFailure ->
                UpdateResponse(
                    state = state.copy(progress = false),
                    subscription = CreateSub.Subscription.ShowFailure(action.failureList)
                )

            is CreateSub.Action.ShowSubscriptionFailure ->
                UpdateResponse(
                    state = state.copy(progress = false),
                    subscription = CreateSub.Subscription.ShowSubscriptionFailure(action.failureList)
                )

            is CreateSub.Action.FacultyListUploaded ->
                UpdateResponse(
                    state = state.copy(
                        facultyList = action.facultyList,
                        progress = false
                    )
                )

            is CreateSub.Action.OccupationListUploaded ->
                UpdateResponse(
                    state = state.copy(
                        occupationList = action.occupationList,
                        progress = false
                    )
                )

            is CreateSub.Action.GroupListUploaded ->
                UpdateResponse(
                    state = state.copy(
                        groupList = action.groupList,
                        progress = false
                    )
                )

            is CreateSub.Action.SubgroupListUploaded ->
                UpdateResponse(
                    state = state.copy(
                        subgroupList = action.subgroupList,
                        progress = false
                    )
                )
        }
}

private fun getHintOrNull(
    selectedFaculty: FacultyModel?,
    selectedOccupation: OccupationModel?,
    selectedGroup: GroupModel?,
    selectedSubgroup: SubgroupModel?
): String? =
    selectedFaculty?.title?.short()
        ?.plus(" ")
        ?.plusIfNotNull(selectedOccupation?.title?.short())
        ?.plus(" ")
        ?.plusIfNotNull(selectedGroup?.number?.toString()?.plus("/"))
        ?.plusIfNotNull(selectedSubgroup?.number?.toString())

private fun String?.plusIfNotNull(str: String?): String? =
    if (str != null) this?.plus(str) else this

private fun String.short(): String =
    trim()
        .split(" ")
        .filter { it != "" }
        .map { it.first() }
        .joinToString(separator = "")