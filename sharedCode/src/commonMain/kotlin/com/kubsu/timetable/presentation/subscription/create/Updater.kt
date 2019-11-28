package com.kubsu.timetable.presentation.subscription.create

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater
import com.kubsu.timetable.presentation.subscription.model.FacultyModel
import com.kubsu.timetable.presentation.subscription.model.GroupModel
import com.kubsu.timetable.presentation.subscription.model.OccupationModel
import com.kubsu.timetable.presentation.subscription.model.SubgroupModel

val createSubscriptionUpdater =
    object : Updater<SubCreateState, SubCreateAction, SubCreateSubscription, SubCreateSideEffect> {
    override fun invoke(
        state: SubCreateState,
        action: SubCreateAction
    ): UpdateResponse<SubCreateState, SubCreateSubscription, SubCreateSideEffect> =
        when (action) {
            SubCreateAction.LoadFacultyList -> {
                UpdateResponse(
                    state = state.copy(progress = true),
                    sideEffects = setOf(SubCreateSideEffect.SelectFacultyList)
                )
            }

            is SubCreateAction.FacultyWasSelected -> {
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
                        setOf(SubCreateSideEffect.SelectOccupationList(faculty))
                    else
                        emptySet()
                )
            }

            is SubCreateAction.OccupationWasSelected -> {
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
                        setOf(SubCreateSideEffect.SelectGroupList(occupation))
                    else
                        emptySet()
                )
            }

            is SubCreateAction.GroupWasSelected -> {
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
                        setOf(SubCreateSideEffect.SelectSubgroupList(group))
                    else
                        emptySet()
                )
            }

            is SubCreateAction.SubgroupWasSelected -> {
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

            is SubCreateAction.CreateSubscription -> {
                when (null) {
                    state.selectedFaculty ->
                        UpdateResponse<SubCreateState, SubCreateSubscription, SubCreateSideEffect>(
                            state,
                            subscription = SubCreateSubscription.ChooseFaculty
                        )

                    state.selectedOccupation ->
                        UpdateResponse<SubCreateState, SubCreateSubscription, SubCreateSideEffect>(
                            state,
                            subscription = SubCreateSubscription.ChooseOccupation
                        )

                    state.selectedGroup ->
                        UpdateResponse<SubCreateState, SubCreateSubscription, SubCreateSideEffect>(
                            state,
                            subscription = SubCreateSubscription.ChooseGroup
                        )

                    state.selectedSubgroup ->
                        UpdateResponse<SubCreateState, SubCreateSubscription, SubCreateSideEffect>(
                            state,
                            subscription = SubCreateSubscription.ChooseSubgroup
                        )

                    else ->
                        UpdateResponse<SubCreateState, SubCreateSubscription, SubCreateSideEffect>(
                            state = state.copy(progress = true),
                            sideEffects = setOf(
                                SubCreateSideEffect.CreateSubscription(
                                    subgroup = state.selectedSubgroup,
                                    subscriptionName = action.subscriptionName,
                                    isMain = action.isMain
                                )
                            )
                        )
                }
            }

            is SubCreateAction.SubscriptionWasCreated ->
                UpdateResponse(
                    state = state.copy(progress = false),
                    subscription = SubCreateSubscription.Navigate(SubCreateScreen.TimetableScreen),
                    sideEffects = setOf(SubCreateSideEffect.DisplayedSubscription(action.subscription))
                )

            is SubCreateAction.ShowDataFailure ->
                UpdateResponse(
                    state = state.copy(progress = false),
                    subscription = SubCreateSubscription.ShowFailure(action.failureList)
                )

            is SubCreateAction.ShowSubscriptionFailure ->
                UpdateResponse(
                    state = state.copy(progress = false),
                    subscription = SubCreateSubscription.ShowSubscriptionFailure(action.failureList)
                )

            is SubCreateAction.FacultyListUploaded ->
                UpdateResponse(
                    state = state.copy(
                        facultyList = action.facultyList,
                        progress = false
                    )
                )

            is SubCreateAction.OccupationListUploaded ->
                UpdateResponse(
                    state = state.copy(
                        occupationList = action.occupationList,
                        progress = false
                    )
                )

            is SubCreateAction.GroupListUploaded ->
                UpdateResponse(
                    state = state.copy(
                        groupList = action.groupList,
                        progress = false
                    )
                )

            is SubCreateAction.SubgroupListUploaded ->
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