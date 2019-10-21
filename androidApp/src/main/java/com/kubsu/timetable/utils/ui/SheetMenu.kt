package com.kubsu.timetable.utils.ui

import android.content.Context
import androidx.fragment.app.Fragment
import ru.whalemare.sheetmenu.ActionItem
import ru.whalemare.sheetmenu.SheetMenu

fun Fragment.sheetMenu(
    context: Context,
    titleId: Int = 0,
    title: String? = "",
    menu: Int = 0,
    onClick: (ActionItem) -> Unit,
    showIcons: Boolean = false
) =
    SheetMenu(
        context = context,
        menu = menu,
        title = title ?: getString(titleId),
        onClick = onClick,
        showIcons = showIcons
    ).show(context, lifecycle)