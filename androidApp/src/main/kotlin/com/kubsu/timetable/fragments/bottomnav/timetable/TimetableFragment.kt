package com.kubsu.timetable.fragments.bottomnav.timetable

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.egroden.teaco.*
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.data.storage.displayed.subscription.DisplayedSubscriptionStorage
import com.kubsu.timetable.extensions.checkWhenAllHandled
import com.kubsu.timetable.extensions.indexOfNearestDayOrNull
import com.kubsu.timetable.fragments.bottomnav.BottomNavFragmentDirections
import com.kubsu.timetable.fragments.bottomnav.timetable.adapter.TimetableAdapter
import com.kubsu.timetable.presentation.timetable.*
import com.kubsu.timetable.presentation.timetable.model.TimetableModel
import com.kubsu.timetable.presentation.timetable.model.TypeOfWeekModel
import com.kubsu.timetable.presentation.timetable.model.UniversityInfoModel
import com.kubsu.timetable.utils.*
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.timetable_fragment.view.*

class TimetableFragment(
    featureFactory: (
        oldState: TimetableState?
    ) -> Feature<TimetableAction, TimetableSideEffect, TimetableState, TimetableSubscription>,
    private val displayedSubscriptionStorage: DisplayedSubscriptionStorage
) : BaseFragment(R.layout.timetable_fragment),
    Render<TimetableState, TimetableSubscription> {
    private val connector by androidConnectors(featureFactory) {
        bindAction(TimetableAction.LoadData(displayedSubscriptionStorage.get()))
    }

    private val titleEffect = UiEffect("")
    private val subtitleEffect = UiEffect("")
    private val progressEffect = UiEffect(false)
    private val universityInfoEffect = UiEffect<UniversityInfoModel?>(null)
    private val currentTimetableEffect = UiEffect<TimetableModel?>(null)
    private val nextWeekTimetableEffect = UiEffect<TimetableModel?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(this, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view.toolbar) {
            inflateMenu(R.menu.timetable_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.next_week_timetable -> {
                        navigate(
                            TimetableScreen.NextWeekTimetable(
                                universityInfo = universityInfoEffect.value,
                                timetable = nextWeekTimetableEffect.value
                            )
                        )
                        true
                    }

                    else -> false
                }
            }
        }
        val timetableAdapter = TimetableAdapter(
            wasDisplayed = { connector bindAction TimetableAction.WasDisplayed(it) }
        )
        val linearLayoutManager = LinearLayoutManager(view.context)
        with(view.timetable_recycler_view) {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = timetableAdapter
        }

        titleEffect bind view.toolbar::setTitle
        subtitleEffect bind view.toolbar::setSubtitle
        progressEffect bind view.progress_bar::setVisibleStatus
        currentTimetableEffect bind { timetableModel ->
            val timetableInfoList = timetableModel?.infoList ?: emptyList()
            val listIsEmpty = timetableInfoList.isEmpty()
            view.empty_list_layout.isVisible = listIsEmpty && !progressEffect.value
            view.timetable_recycler_view.isVisible = !listIsEmpty

            timetableAdapter.setData(
                newList = timetableInfoList,
                onFirst = {
                    timetableInfoList
                        .indexOfNearestDayOrNull()
                        ?.let(linearLayoutManager::scrollToPosition)
                }
            )
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

    override fun renderState(state: TimetableState) {
        titleEffect.value = state.currentSubscription?.title ?: ""

        progressEffect.value = state.progress

        val universityInfo = state.universityInfoModel
        universityInfoEffect.value = universityInfo
        when (universityInfo?.typeOfWeek) {
            TypeOfWeekModel.Numerator -> {
                subtitleEffect.value = getString(
                    R.string.numerator_subtitle,
                    universityInfo.weekNumber.toString()
                )
                currentTimetableEffect.value = state.numeratorTimetable
                nextWeekTimetableEffect.value = state.denominatorTimetable
            }
            TypeOfWeekModel.Denominator -> {
                subtitleEffect.value = getString(
                    R.string.denominator_subtitle,
                    universityInfo.weekNumber.toString()
                )
                currentTimetableEffect.value = state.denominatorTimetable
                nextWeekTimetableEffect.value = state.numeratorTimetable
            }
            null -> {
                currentTimetableEffect.value = null
                nextWeekTimetableEffect.value = null
            }
        }.checkWhenAllHandled()
    }

    override fun renderSubscription(subscription: TimetableSubscription) =
        when (subscription) {
            is TimetableSubscription.Navigate -> navigate(subscription.screen)
        }

    private fun navigate(screen: TimetableScreen) =
        when (screen) {
            is TimetableScreen.NextWeekTimetable ->
                safeNavigate(
                    BottomNavFragmentDirections
                        .actionBottomNavFragmentToNextWeekTimetableFragment(
                            universityInfo = screen.universityInfo,
                            timetable = screen.timetable
                        )
                )
        }
}
