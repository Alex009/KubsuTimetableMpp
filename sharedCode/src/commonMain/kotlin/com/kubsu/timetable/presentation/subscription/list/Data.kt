package com.kubsu.timetable.presentation.subscription.list

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel

sealed class SubListAction {
    object UpdateData : SubListAction()
    object CreateSubscription : SubListAction()
    class SubscriptionWasSelected(
        val subscription: SubscriptionModel
    ) : SubListAction()

    class DeleteSubscription(
        val subscription: SubscriptionModel
    ) : SubListAction()

    class ChangeSubscriptionStatus(
        val subscription: SubscriptionModel
    ) : SubListAction()

    internal class SubscriptionListUploaded(
        val subscriptionList: List<SubscriptionModel>
    ) : SubListAction()

    internal class ShowSubscriptionFailure(
        val failureList: List<SubscriptionFail>
    ) : SubListAction()

    internal class ShowDataFailure(
        val failureList: List<DataFailure>
    ) : SubListAction()
}

@Parcelize
data class SubListState(
    val progress: Boolean,
    val subscriptionList: List<SubscriptionModel>
) : Parcelable

sealed class SubListSideEffect {
    object LoadSubscriptionList : SubListSideEffect()
    class UpdateSubscription(
        val subscription: SubscriptionModel
    ) : SubListSideEffect()

    class DeleteSubscription(
        val subscription: SubscriptionModel
    ) : SubListSideEffect()

    class DisplayedSubscription(val subscription: SubscriptionModel) : SubListSideEffect()
}

sealed class SubListSubscription {
    class Navigate(val screen: SubListScreen) : SubListSubscription()

    class ShowSubscriptionFailure(val failureList: List<SubscriptionFail>) : SubListSubscription()
    class ShowDataFailure(val failureList: List<DataFailure>) : SubListSubscription()
}

sealed class SubListScreen {
    object CreateSubscription : SubListScreen()
    object ShowTimetable : SubListScreen()
}