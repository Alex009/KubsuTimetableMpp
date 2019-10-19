package com.kubsu.timetable.data.storage.displayed.subscription

import com.kubsu.timetable.data.storage.BaseStorage
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel
import com.russhwolf.settings.Settings

class DisplayedSubscriptionStorageImpl(
    settingsFactory: Settings.Factory
) : DisplayedSubscriptionStorage,
    BaseStorage(settingsFactory.create("displayed_subscription")) {
    override fun set(subscription: SubscriptionModel?) {
        set(idPropName, subscription?.id)
        set(titlePropName, subscription?.title)
        set(userIdPropName, subscription?.userId)
        set(subgroupIdPropName, subscription?.subgroupId)
        set(isMainPropName, subscription?.isMain)
    }

    override fun get(): SubscriptionModel? {
        val id: Int? = getInt(idPropName)
        val title: String? = getString(titlePropName)
        val userId: Int? = getInt(userIdPropName)
        val subgroupId: Int? = getInt(subgroupIdPropName)
        val isMain: Boolean = getBoolean(isMainPropName, false)
        return if (id != null
            && title != null
            && userId != null
            && subgroupId != null
        )
            SubscriptionModel(id, title, userId, subgroupId, isMain)
        else
            null
    }

    override fun deleteIfEqual(subscription: SubscriptionModel) {
        if (subscription.id == getInt(idPropName))
            set(null)
    }

    private companion object {
        private const val idPropName = "id"
        private const val titlePropName = "title"
        private const val userIdPropName = "user_id"
        private const val subgroupIdPropName = "subgroup_id"
        private const val isMainPropName = "is_main"
    }
}