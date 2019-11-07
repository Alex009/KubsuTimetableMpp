package com.kubsu.timetable.fragments.bottomnav.subscription.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kubsu.timetable.R
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel
import com.kubsu.timetable.utils.setColoredBackground
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_subscription.view.*

class SubscriptionAdapter(
    private val onClick: (SubscriptionModel) -> Unit,
    private val onLongClick: (SubscriptionModel) -> Unit,
    private val changeSubscriptionStatus: (SubscriptionModel) -> Unit
) : RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder>() {
    private var subscriptionList = emptyList<SubscriptionModel>()

    fun setData(list: List<SubscriptionModel>) {
        val diffResult = DiffUtil.calculateDiff(
            SubscriptionDiffUtilCallback(
                oldList = subscriptionList,
                newList = list
            ),
            false
        )
        subscriptionList = list
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int =
        subscriptionList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder =
        SubscriptionViewHolder(
            containerView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_subscription, parent, false),
            onClick = onClick,
            onLongClick = onLongClick,
            changeSubscriptionStatus = changeSubscriptionStatus
        )

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) =
        holder.bind(subscriptionList[position])

    class SubscriptionViewHolder(
        override val containerView: View,
        private val onClick: (SubscriptionModel) -> Unit,
        private val onLongClick: (SubscriptionModel) -> Unit,
        private val changeSubscriptionStatus: (SubscriptionModel) -> Unit
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(subscription: SubscriptionModel) = with(containerView) {
            name_text_view.text = subscription.title
            name_text_view.setOnClickListener { onClick(subscription) }
            name_text_view.setOnLongClickListener {
                onLongClick(subscription)
                true
            }

            val (color, text) = if (subscription.numberOfUpdatedClasses > 0)
                getColorAccent(subscription) to subscription.numberOfUpdatedClasses.toString()
            else
                android.R.color.transparent to ""

            count_text_view.setColoredBackground(color)
            count_text_view.text = text

            image_button.setImageResource(
                if (subscription.isMain)
                    R.drawable.ic_notifications_active_24dp
                else
                    R.drawable.ic_notifications_off_24dp
            )
            image_button.setOnClickListener {
                changeSubscriptionStatus(subscription)
            }
        }

        private fun getColorAccent(subscription: SubscriptionModel) =
            if (subscription.isMain)
                R.color.colorAccentPrimary
            else
                R.color.colorAccentSecondary
    }
}