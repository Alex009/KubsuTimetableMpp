package com.kubsu.timetable.fragments.bottomnav.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kubsu.timetable.R
import com.kubsu.timetable.presentation.timetable.model.ClassModel
import com.kubsu.timetable.presentation.timetable.model.TimetableInfoToDisplay

class TimetableAdapter(
    private val wasDisplayed: (ClassModel) -> Unit
) : RecyclerView.Adapter<TimetableViewHolder>() {
    private var timetableList = listOf<TimetableInfoToDisplay>()

    private val classViewHolderId = 0
    private val workingDayViewHolderId = 1

    fun setData(list: List<TimetableInfoToDisplay>) {
        val diffResult = DiffUtil.calculateDiff(
            TimetableDiffUtilCallback(
                oldList = timetableList,
                newList = list
            ),
            false
        )
        timetableList = list
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            classViewHolderId ->
                TimetableViewHolder.Class(
                    wasDisplayed,
                    inflater.inflate(R.layout.item_class, parent, false)
                )

            workingDayViewHolderId ->
                TimetableViewHolder.WorkingDay(
                    inflater.inflate(R.layout.item_working_day, parent, false)
                )

            else ->
                throw IllegalArgumentException("Unknown Holder Type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (timetableList[position]) {
            is TimetableInfoToDisplay.Class ->
                classViewHolderId

            is TimetableInfoToDisplay.Day ->
                workingDayViewHolderId
        }

    override fun getItemCount(): Int =
        timetableList.size

    override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
        val item = timetableList[position]

        when (holder) {
            is TimetableViewHolder.Class ->
                holder.bind(item as TimetableInfoToDisplay.Class)

            is TimetableViewHolder.WorkingDay ->
                holder.bind(item as TimetableInfoToDisplay.Day)
        }
    }
}