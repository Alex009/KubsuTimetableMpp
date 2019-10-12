package com.kubsu.timetable.presentation.subscription

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.presentation.subscription.model.FacultyModel
import com.kubsu.timetable.presentation.subscription.model.GroupModel
import com.kubsu.timetable.presentation.subscription.model.OccupationModel
import com.kubsu.timetable.presentation.subscription.model.SubgroupModel
import kotlinx.serialization.Serializable

sealed class Action {
    object LoadFacultyList : Action()
    class FacultyWasSelected(val faculty: FacultyModel) : Action()
    class OccupationWasSelected(val occupation: OccupationModel) : Action()
    class GroupWasSelected(val group: GroupModel) : Action()
    class SubgroupWasSelected(val subgroup: SubgroupModel) : Action()
    class CreateSubscription(
        val subscriptionName: String,
        val isMain: Boolean
    ) : Action()

    internal object SubscriptionWasCreated : Action()
    internal class ShowDataError(val failure: DataFailure) : Action()
    internal class ShowRequestError(
        val failure: RequestFailure<List<SubscriptionFail>>
    ) : Action()

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

@Serializable
data class State(
    val progress: Boolean,
    val facultyList: List<FacultyModel>,
    val occupationList: List<OccupationModel>,
    val groupList: List<GroupModel>,
    val subgroupList: List<SubgroupModel>,
    val selectedFaculty: FacultyModel? = null,
    val selectedOccupation: OccupationModel? = null,
    val selectedGroup: GroupModel? = null,
    val selectedSubgroup: SubgroupModel? = null
)

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
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()

    class ShowError(val failure: DataFailure) : Subscription()
    class ShowRequestError(
        val failure: RequestFailure<List<SubscriptionFail>>
    ) : Subscription()

    object ChooseFaculty : Subscription()
    object ChooseOccupation : Subscription()
    object ChooseGroup : Subscription()
    object ChooseSubgroup : Subscription()
}

sealed class Screen {
    object TimetableScreen : Screen()
}