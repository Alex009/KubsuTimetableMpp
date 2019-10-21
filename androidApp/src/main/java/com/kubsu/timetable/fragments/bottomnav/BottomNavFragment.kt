package com.kubsu.timetable.fragments.bottomnav

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kubsu.timetable.BottomNavGraphDirections
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.data.storage.displayed.subscription.DisplayedSubscriptionStorage
import com.kubsu.timetable.utils.selectedItem
import kotlinx.android.synthetic.main.bottom_nav_fragment.view.*

class BottomNavFragment(
    private val displayedSubscriptionStorage: DisplayedSubscriptionStorage
) : BaseFragment(R.layout.bottom_nav_fragment),
    NavHost {
    override fun getNavController(): NavController =
        findNavController(requireActivity(), R.id.bottom_nav_host_fragment)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view.bottom_navigation_view) {
            setupWithNavController(navController)
            if (selectedItem.itemId == R.id.timetableFragment)
                displayedSubscriptionStorage.get()?.let {
                    navController.navigate(BottomNavGraphDirections.actionGlobalTimetableFragment(it))
                }
        }
    }
}
