package com.kubsu.timetable.fragments.bottomnav.subscription.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel

class SubscriptionDiffUtilCallback(
    private val oldList: List<SubscriptionModel>,
    private val newList: List<SubscriptionModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun getOldListSize(): Int =
        oldList.size

    override fun getNewListSize(): Int =
        newList.size
}
