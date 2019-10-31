package com.kubsu.timetable.fragments.bottomnav.timetable.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kubsu.timetable.R
import com.kubsu.timetable.presentation.timetable.model.TimetableInfoToDisplay
import com.kubsu.timetable.presentation.timetable.model.TypeOfClassModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_class.*
import kotlinx.android.synthetic.main.item_working_day.*

sealed class TimetableViewHolder(
    final override val containerView: View
) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    val dayOfWeekArray: Array<String> =
        containerView.context.resources.getStringArray(R.array.day_of_week)

    class Class(itemView: View) : TimetableViewHolder(itemView) {
        fun bind(classModel: TimetableInfoToDisplay.Class) {
            val `class` = classModel.classModel
            class_name_text_view.text = `class`.title

            classroom_text_view.text = String.format(
                itemView.context.getString(R.string.classroom_with_parameter),
                `class`.classroom
            )

            class_type_text_view.text = itemView.context.getString(
                if (`class`.typeOfClass is TypeOfClassModel.Lecture)
                    R.string.lecture
                else
                    R.string.seminar
            )

            val lecturer = `class`.lecturer
            lecturer_text_view.text = String.format(
                itemView.context.getString(R.string.lecturer_full_name),
                lecturer.surname,
                lecturer.name,
                lecturer.patronymic
            )

            val classTime = `class`.classTime
            start_class_time_text_view.text = classTime.start
            finish_class_time_text_view.text = classTime.end
        }
    }

    class WorkingDay(itemView: View) : TimetableViewHolder(itemView) {
        fun bind(dayModel: TimetableInfoToDisplay.Day) {
            working_day_text_view.text = dayOfWeekArray[dayModel.index]
        }
    }
}