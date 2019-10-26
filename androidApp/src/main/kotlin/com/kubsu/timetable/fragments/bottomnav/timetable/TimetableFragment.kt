package com.kubsu.timetable.fragments.bottomnav.timetable

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.egroden.teaco.TeaFeature
import com.egroden.teaco.androidConnectors
import com.egroden.teaco.bindAction
import com.egroden.teaco.connect
import com.google.android.material.appbar.MaterialToolbar
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.data.storage.displayed.subscription.DisplayedSubscriptionStorage
import com.kubsu.timetable.extensions.checkWhenAllHandled
import com.kubsu.timetable.extensions.getCurrentDayOfWeek
import com.kubsu.timetable.fragments.bottomnav.BottomNavFragmentDirections
import com.kubsu.timetable.fragments.bottomnav.timetable.adapter.TimetableAdapter
import com.kubsu.timetable.presentation.timetable.*
import com.kubsu.timetable.presentation.timetable.model.TimetableInfoToDisplay
import com.kubsu.timetable.presentation.timetable.model.TimetableModel
import com.kubsu.timetable.presentation.timetable.model.TypeOfWeekModel
import com.kubsu.timetable.presentation.timetable.model.UniversityInfoModel
import com.kubsu.timetable.utils.*
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.timetable_fragment.view.*

class TimetableFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>,
    private val displayedSubscriptionStorage: DisplayedSubscriptionStorage
) : BaseFragment(R.layout.timetable_fragment) {
    private val connector by androidConnectors(teaFeature) {
        bindAction(Action.UpdateData(displayedSubscriptionStorage.get()))
    }

    private val titleEffect = UiEffect("")
    private val subtitleEffect = UiEffect("")
    private val progressEffect = UiEffect(Visibility.INVISIBLE)
    private val universityInfoEffect = UiEffect<UniversityInfoModel?>(null)
    private val currentTimetableEffect = UiEffect<TimetableModel?>(null)
    private val nextWeekTimetableEffect = UiEffect<TimetableModel?>(null)

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
        subtitleEffect bind view.toolbar::setSubtitle
        progressEffect bind { view.progress_bar.visibility(it) }
        universityInfoEffect bind { universityInfo ->
            universityInfo?.typeOfWeek?.let {
                showTimetableMenu(
                    toolbar = view.toolbar,
                    typeOfWeek = it,
                    timetable = nextWeekTimetableEffect.value
                )
            }
        }
        currentTimetableEffect bind { timetableModel ->
            val timetableInfoList = timetableModel?.infoList ?: emptyList()
            val listIsEmpty = timetableInfoList.isEmpty()
            view.empty_list_layout.isVisible =
                listIsEmpty && progressEffect.value == Visibility.INVISIBLE
            view.timetable_recycler_view.isVisible = !listIsEmpty

            timetableAdapter.setData(timetableInfoList)
            timetableInfoList
                .indexOfCurrentDayOrNull()
                ?.let(view.timetable_recycler_view::smoothScrollToPosition)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        titleEffect.unbind()
        subtitleEffect.unbind()
        progressEffect.unbind()
        universityInfoEffect.unbind()
        currentTimetableEffect.unbind()
        nextWeekTimetableEffect.unbind()
    }

    private fun render(state: State) {
        titleEffect.value = state.currentSubscription?.title ?: ""

        progressEffect.value = if (state.progress) Visibility.VISIBLE else Visibility.INVISIBLE

        when (state.universityInfoModel?.typeOfWeek) {
            TypeOfWeekModel.Numerator -> {
                subtitleEffect.value = getString(R.string.numerator)
                currentTimetableEffect.value = state.numeratorTimetable
                nextWeekTimetableEffect.value = state.denominatorTimetable
            }
            TypeOfWeekModel.Denominator -> {
                subtitleEffect.value = getString(R.string.denominator)
                currentTimetableEffect.value = state.denominatorTimetable
                nextWeekTimetableEffect.value = state.numeratorTimetable
            }
            null -> null
        }.checkWhenAllHandled()
    }

    private fun render(subscription: Subscription) =
        when (subscription) {
            is Subscription.Navigate -> navigate(subscription.screen)
            is Subscription.ShowFailure -> notifyUserOfFailure(subscription.failure)
        }

    private fun showTimetableMenu(
        toolbar: MaterialToolbar,
        typeOfWeek: TypeOfWeekModel,
        timetable: TimetableModel?
    ) {
        toolbar.inflateMenu(R.menu.timetable_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.next_week_timetable -> {
                    navigate(Screen.NextWeekTimetable(typeOfWeek, timetable))
                    true
                }

                else -> false
            }
        }
    }

    private fun navigate(screen: Screen) =
        when (screen) {
            is Screen.NextWeekTimetable ->
                safeNavigate(
                    BottomNavFragmentDirections
                        .actionBottomNavFragmentToNextWeekTimetableFragment(
                            typeOfWeek = screen.typeOfWeek,
                            timetable = screen.timetable
                        )
                )
        }

    private fun List<TimetableInfoToDisplay>.indexOfCurrentDayOrNull(): Int? {
        val currentDay = getCurrentDayOfWeek()
        return indexOfFirst {
            (it as? TimetableInfoToDisplay.Day)?.dayOfWeek == currentDay
        }.takeIf { it > -1 }
    }
}
