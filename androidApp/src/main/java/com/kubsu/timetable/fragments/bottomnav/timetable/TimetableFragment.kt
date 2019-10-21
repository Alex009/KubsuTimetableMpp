package com.kubsu.timetable.fragments.bottomnav.timetable

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.egroden.teaco.TeaFeature
import com.egroden.teaco.androidConnectors
import com.egroden.teaco.bindAction
import com.egroden.teaco.connect
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.fragments.bottomnav.timetable.adapter.TimetableAdapter
import com.kubsu.timetable.presentation.timetable.*
import com.kubsu.timetable.presentation.timetable.model.TimetableInfoToDisplay
import com.kubsu.timetable.presentation.timetable.model.TypeOfWeekModel
import com.kubsu.timetable.utils.*
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.timetable_fragment.view.*

class TimetableFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>
) : BaseFragment(R.layout.timetable_fragment) {
    private val args: TimetableFragmentArgs by navArgs()
    private val connector by androidConnectors(teaFeature) {
        bindAction(Action.UpdateData(args.subscription))
    }

    private val titleEffect = UiEffect("")
    private val progressEffect = UiEffect(Visibility.INVISIBLE)
    private val timetableListEffect = UiEffect(emptyList<TimetableInfoToDisplay>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timetableAdapter = TimetableAdapter()
        with(view.timetable_recycler_view) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context)
            adapter = timetableAdapter
        }

        titleEffect bind view.toolbar::setTitle
        progressEffect bind { view.progress_bar.visibility(it) }
        timetableListEffect bind timetableAdapter::setData
    }

    override fun onDestroyView() {
        super.onDestroyView()
        titleEffect.unbind()
        progressEffect.unbind()
        timetableListEffect.unbind()
    }

    private fun render(state: State) {
        titleEffect.value = state.currentSubscription?.title ?: ""

        progressEffect.value = if (state.progress) Visibility.VISIBLE else Visibility.INVISIBLE

        val universityInfoModel = state.universityInfoModel
        val timetable = when (universityInfoModel?.typeOfWeek) {
            TypeOfWeekModel.Numerator -> state.numeratorTimetable
            TypeOfWeekModel.Denominator -> state.denominatorTimetable
            null -> null
        }
        if (timetable != null)
            timetableListEffect.value = timetable.infoList
        //TODO show message "list is empty"
    }

    private fun render(subscription: Subscription) =
        when (subscription) {
            is Subscription.Navigate -> navigate(subscription.screen)
            is Subscription.ShowFailure -> notifyUserOfFailure(subscription.failure)
        }

    private fun navigate(screen: Screen) = Unit
}
