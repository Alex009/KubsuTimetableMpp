package com.kubsu.timetable.data.storage.displayed.subscription

import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel

interface DisplayedSubscriptionStorage {
    fun set(subscription: SubscriptionModel?)
    fun get(): SubscriptionModel?
    fun deleteIfEqual(subscription: SubscriptionModel)
}