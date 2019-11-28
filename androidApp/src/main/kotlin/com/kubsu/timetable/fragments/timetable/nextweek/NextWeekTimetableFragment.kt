package com.kubsu.timetable.fragments.timetable.nextweek

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.egroden.teaco.*
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.fragments.bottomnav.timetable.adapter.TimetableAdapter
import com.kubsu.timetable.presentation.timetable.TimetableAction
import com.kubsu.timetable.presentation.timetable.TimetableSideEffect
import com.kubsu.timetable.presentation.timetable.TimetableState
import com.kubsu.timetable.presentation.timetable.TimetableSubscription
import com.kubsu.timetable.presentation.timetable.model.TypeOfWeekModel
import kotlinx.android.synthetic.main.timetable_fragment.view.*

class NextWeekTimetableFragment(
    featureFactory: (
        oldState: TimetableState?
    ) -> Feature<TimetableAction, TimetableSideEffect, TimetableState, TimetableSubscription>
) : BaseFragment(R.layout.timetable_fragment),
    Render<TimetableState, TimetableSubscription> {
    private val connector by androidConnectors(featureFactory)
    private val args: NextWeekTimetableFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(this, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view.toolbar) {
            setNavigationIcon(R.drawable.ic_arrow_back_24dp)
            setNavigationOnClickListener {
                popBackStack()
            }
            args.universityInfo?.let { universityInfo ->
                val weekNumber = (universityInfo.weekNumber + 1).toString()
                subtitle = when (universityInfo.typeOfWeek) {
                    // inverse
                    TypeOfWeekModel.Numerator -> getString(
                        R.string.denominator_subtitle,
                        weekNumber
                    )
                    TypeOfWeekModel.Denominator -> getString(
                        R.string.numerator_subtitle,
                        weekNumber
                    )
                }
            }
        }

        val timetableAdapter = TimetableAdapter(
            wasDisplayed = { connector bindAction TimetableAction.WasDisplayed(it) }
        )
        with(view.timetable_recycler_view) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context)
            adapter = timetableAdapter
        }

        val timetableInfoList = args.timetable?.infoList ?: emptyList()
        val listIsEmpty = timetableInfoList.isEmpty()

        view.empty_list_layout.isVisible = listIsEmpty
        view.timetable_recycler_view.isVisible = !listIsEmpty

        timetableAdapter.setData(
            newList = timetableInfoList,
            onFirst = {}
        )
    }

    override fun renderState(state: TimetableState) = Unit
    override fun renderSubscription(subscription: TimetableSubscription) = Unit
}
