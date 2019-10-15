package com.kubsu.timetable.fragments.bottomnav.subscription.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kubsu.timetable.R
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_subscription.view.*

interface TouchProvider {
    fun subscriptionWasSelected(subscription: SubscriptionModel)
}

class SubscriptionAdapter(
    private val touchProvider: TouchProvider
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
            touchProvider = touchProvider
        )

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) =
        holder.bind(subscriptionList[position])

    class SubscriptionViewHolder(
        override val containerView: View,
        private val touchProvider: TouchProvider
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(subscription: SubscriptionModel) = with(containerView) {
            text_view.text = subscription.title

            image_button.setImageResource(
                if (subscription.isMain)
                    R.drawable.ic_notifications_active_black_24dp
                else
                    R.drawable.ic_notifications_off_black_24dp
            )

            setOnClickListener { touchProvider.subscriptionWasSelected(subscription) }
        }
    }
}