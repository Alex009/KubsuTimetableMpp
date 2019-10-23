package com.kubsu.timetable.fragments.timetable.nextweek

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.fragments.bottomnav.timetable.adapter.TimetableAdapter
import kotlinx.android.synthetic.main.timetable_fragment.view.*

class NextWeekTimetableFragment : BaseFragment(R.layout.timetable_fragment) {
    private val args: NextWeekTimetableFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view.toolbar) {
            setNavigationIcon(R.drawable.ic_arrow_back_24dp)
            setNavigationOnClickListener {
                popBackStack()
            }
        }

        val timetableAdapter = TimetableAdapter()
        with(view.timetable_recycler_view) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context)
            adapter = timetableAdapter
        }

        val timetableInfoList = args.timetable.infoList
        val listIsNotEmpty = timetableInfoList.isNotEmpty()

        view.empty_list_layout.isVisible = !listIsNotEmpty
        view.timetable_recycler_view.isVisible = listIsNotEmpty

        timetableAdapter.setData(timetableInfoList)
    }
}
