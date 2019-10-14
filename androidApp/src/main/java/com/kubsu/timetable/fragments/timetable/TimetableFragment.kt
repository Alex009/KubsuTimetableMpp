package com.kubsu.timetable.fragments.timetable

import com.egroden.teaco.StateParser
import com.egroden.teaco.TeaFeature
import com.egroden.teaco.androidConnectors
import com.egroden.teaco.bindAction
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.timetable.Action
import com.kubsu.timetable.presentation.timetable.SideEffect
import com.kubsu.timetable.presentation.timetable.State
import com.kubsu.timetable.presentation.timetable.Subscription

class TimetableFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>,
    stateParser: StateParser<State>
) : BaseFragment(R.layout.timetable_fragment) {
    private val connector by androidConnectors(teaFeature, stateParser) {
        bindAction(Action.UpdateData())
    }
}
