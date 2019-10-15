package com.kubsu.timetable.fragments.bottomnav.timetable

import com.egroden.teaco.TeaFeature
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.timetable.Action
import com.kubsu.timetable.presentation.timetable.SideEffect
import com.kubsu.timetable.presentation.timetable.State
import com.kubsu.timetable.presentation.timetable.Subscription

class TimetableFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>
) : BaseFragment(R.layout.timetable_fragment)
