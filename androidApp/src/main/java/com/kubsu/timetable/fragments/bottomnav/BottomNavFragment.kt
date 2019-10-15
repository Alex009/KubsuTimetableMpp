package com.kubsu.timetable.fragments.bottomnav

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import kotlinx.android.synthetic.main.bottom_nav_fragment.view.*

class BottomNavFragment : BaseFragment(R.layout.bottom_nav_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(
            requireActivity(),
            R.id.bottom_nav_host_fragment
        )
        view.bottom_navigation_view.setupWithNavController(navController)
    }
}
