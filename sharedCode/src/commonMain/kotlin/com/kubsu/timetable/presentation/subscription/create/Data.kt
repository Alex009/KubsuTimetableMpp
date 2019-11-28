package com.kubsu.timetable.presentation.subscription.create

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize
import com.kubsu.timetable.presentation.subscription.model.FacultyModel
import com.kubsu.timetable.presentation.subscription.model.GroupModel
import com.kubsu.timetable.presentation.subscription.model.OccupationModel
import com.kubsu.timetable.presentation.subscription.model.SubgroupModel
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel

sealed class SubCreateAction {
    object LoadFacultyList : SubCreateAction()
    class FacultyWasSelected(val id: Int?) : SubCreateAction()
    class OccupationWasSelected(val id: Int?) : SubCreateAction()
    class GroupWasSelected(val id: Int?) : SubCreateAction()
    class SubgroupWasSelected(val id: Int?) : SubCreateAction()
    class CreateSubscription(
        val subscriptionName: String,
        val isMain: Boolean
    ) : SubCreateAction()

    internal class SubscriptionWasCreated(val subscription: SubscriptionModel) : SubCreateAction()
    internal class ShowSubscriptionFailure(val failureList: List<SubscriptionFail>) :
        SubCreateAction()

    internal class ShowDataFailure(val failureList: List<DataFailure>) : SubCreateAction()

    internal class FacultyListUploaded(
        val facultyList: List<FacultyModel>
    ) : SubCreateAction()

    internal class OccupationListUploaded(
        val occupationList: List<OccupationModel>
    ) : SubCreateAction()

    internal class GroupListUploaded(
        val groupList: List<GroupModel>
    ) : SubCreateAction()

    internal class SubgroupListUploaded(
        val subgroupList: List<SubgroupModel>
    ) : SubCreateAction()
}

@Parcelize
data class SubCreateState(
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
) : Parcelable

sealed class SubCreateSideEffect {
    object SelectFacultyList : SubCreateSideEffect()
    class SelectOccupationList(val faculty: FacultyModel) : SubCreateSideEffect()
    class SelectGroupList(val occupation: OccupationModel) : SubCreateSideEffect()
    class SelectSubgroupList(val group: GroupModel) : SubCreateSideEffect()
    class CreateSubscription(
        val subgroup: SubgroupModel,
        val subscriptionName: String,
        val isMain: Boolean
    ) : SubCreateSideEffect()

    class DisplayedSubscription(val subscription: SubscriptionModel) : SubCreateSideEffect()
}

sealed class SubCreateSubscription {
    class Navigate(val screen: SubCreateScreen) : SubCreateSubscription()

    class ShowSubscriptionFailure(val failureList: List<SubscriptionFail>) : SubCreateSubscription()
    class ShowFailure(val failureList: List<DataFailure>) : SubCreateSubscription()

    object ChooseFaculty : SubCreateSubscription()
    object ChooseOccupation : SubCreateSubscription()
    object ChooseGroup : SubCreateSubscription()
    object ChooseSubgroup : SubCreateSubscription()
}

sealed class SubCreateScreen {
    object TimetableScreen : SubCreateScreen()
}
