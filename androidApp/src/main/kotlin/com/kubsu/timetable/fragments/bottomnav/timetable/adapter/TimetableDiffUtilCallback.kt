package com.kubsu.timetable.fragments.bottomnav.timetable.adapter

import androidx.recyclerview.widget.DiffUtil
import com.kubsu.timetable.presentation.timetable.model.TimetableInfoToDisplay

class TimetableDiffUtilCallback(
    private val oldList: List<TimetableInfoToDisplay>,
    private val newList: List<TimetableInfoToDisplay>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return when (old) {
            is TimetableInfoToDisplay.Class -> when (new) {
                is TimetableInfoToDisplay.Class -> old.classModel.id == new.classModel.id
                is TimetableInfoToDisplay.Day -> false
            }

            is TimetableInfoToDisplay.Day -> when (new) {
                is TimetableInfoToDisplay.Class -> false
                is TimetableInfoToDisplay.Day -> old.dayOfWeek == new.dayOfWeek
            }
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun getOldListSize(): Int =
        oldList.size

    override fun getNewListSize(): Int =
        newList.size
}
