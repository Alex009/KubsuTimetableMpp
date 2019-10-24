package com.kubsu.timetable.presentation.subscription.list

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.SubscriptionFail
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel
import platform.SerializableModel
import platform.SerializeModel

sealed class Action {
    object UpdateData : Action()
    object CreateSubscription : Action()
    class SubscriptionWasSelected(
        val subscription: SubscriptionModel
    ) : Action()

    class DeleteSubscription(
        val subscription: SubscriptionModel
    ) : Action()

    class ChangeSubscriptionStatus(
        val subscription: SubscriptionModel
    ) : Action()

    internal class SubscriptionListUploaded(
        val subscriptionList: List<SubscriptionModel>
    ) : Action()

    internal class ShowSubscriptionFailure(
        val failureList: List<SubscriptionFail>
    ) : Action()

    internal class ShowDataFailure(
        val failureList: List<DataFailure>
    ) : Action()
}

@SerializeModel
data class State(
    val progress: Boolean,
    val subscriptionList: List<SubscriptionModel>
) : SerializableModel

sealed class SideEffect {
    object LoadSubscriptionList : SideEffect()
    class UpdateSubscription(
        val subscription: SubscriptionModel
    ) : SideEffect()

    class DeleteSubscription(
        val subscription: SubscriptionModel
    ) : SideEffect()

    class DisplayedSubscription(val subscription: SubscriptionModel) : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()

    class ShowSubscriptionFailure(val failureList: List<SubscriptionFail>) : Subscription()
    class ShowDataFailure(val failureList: List<DataFailure>) : Subscription()
}

sealed class Screen {
    object CreateSubscription : Screen()
    object ShowTimetable : Screen()
}