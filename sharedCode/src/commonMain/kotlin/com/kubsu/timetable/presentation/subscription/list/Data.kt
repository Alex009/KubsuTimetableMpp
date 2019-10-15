package com.kubsu.timetable.presentation.subscription.list

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel
import platform.SerializableModel
import platform.SerializeModel

sealed class Action {
    object UpdateData : Action()
    object CreateSubscription : Action()
    class SubscriptionWasSelected(
        val subscription: SubscriptionModel
    ) : Action()

    internal class SubscriptionListUploaded(
        val subscriptionList: List<SubscriptionModel>
    ) : Action()

    internal class ShowFailure(
        val failure: DataFailure
    ) : Action()
}

@SerializeModel
data class State(
    val progress: Boolean,
    val subscriptionList: List<SubscriptionModel>
) : SerializableModel

sealed class SideEffect {
    object LoadSubscriptionList : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()

    class ShowFailure(val failure: DataFailure) : Subscription()
}

sealed class Screen {
    object CreateSubscription : Screen()
    class ShowTimetableForSubscription(
        val subscription: SubscriptionModel
    ) : Screen()
}