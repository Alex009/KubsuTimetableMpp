package com.kubsu.timetable.presentation.subscription.create

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.presentation.subscription.model.FacultyModel
import com.kubsu.timetable.presentation.subscription.model.GroupModel
import com.kubsu.timetable.presentation.subscription.model.OccupationModel
import com.kubsu.timetable.presentation.subscription.model.SubgroupModel
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel
import platform.SerializableModel
import platform.SerializeModel

sealed class Action {
    object LoadFacultyList : Action()
    class FacultyWasSelected(val id: Int?) : Action()
    class OccupationWasSelected(val id: Int?) : Action()
    class GroupWasSelected(val id: Int?) : Action()
    class SubgroupWasSelected(val id: Int?) : Action()
    class CreateSubscription(
        val subscriptionName: String,
        val isMain: Boolean
    ) : Action()

    internal class SubscriptionWasCreated(val subscription: SubscriptionModel) : Action()
    internal class ShowSubscriptionFailure(val failureList: List<SubscriptionFail>) : Action()
    internal class ShowDataFailure(val failureList: List<DataFailure>) : Action()

    internal class FacultyListUploaded(
        val facultyList: List<FacultyModel>
    ) : Action()

    internal class OccupationListUploaded(
        val occupationList: List<OccupationModel>
    ) : Action()

    internal class GroupListUploaded(
        val groupList: List<GroupModel>
    ) : Action()

    internal class SubgroupListUploaded(
        val subgroupList: List<SubgroupModel>
    ) : Action()
}

@SerializeModel
data class State(
    val progress: Boolean,
    val facultyList: List<FacultyModel>,
    val occupationList: List<OccupationModel>,
    val groupList: List<GroupModel>,
    val subgroupList: List<SubgroupModel>,
    val nameHint: String?,
    internal val selectedFaculty: FacultyModel?,
    internal val selectedOccupation: OccupationModel?,
    internal val selectedGroup: GroupModel?,
    internal val selectedSubgroup: SubgroupModel?
) : SerializableModel

sealed class SideEffect {
    object SelectFacultyList : SideEffect()
    class SelectOccupationList(val faculty: FacultyModel) : SideEffect()
    class SelectGroupList(val occupation: OccupationModel) : SideEffect()
    class SelectSubgroupList(val group: GroupModel) : SideEffect()
    class CreateSubscription(
        val subgroup: SubgroupModel,
        val subscriptionName: String,
        val isMain: Boolean
    ) : SideEffect()

    class DisplayedSubscription(val subscription: SubscriptionModel) : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()

    class ShowSubscriptionFailure(val failureList: List<SubscriptionFail>) : Subscription()
    class ShowFailure(val failureList: List<DataFailure>) : Subscription()

    object ChooseFaculty : Subscription()
    object ChooseOccupation : Subscription()
    object ChooseGroup : Subscription()
    object ChooseSubgroup : Subscription()
}

sealed class Screen {
    object TimetableScreen : Screen()
}