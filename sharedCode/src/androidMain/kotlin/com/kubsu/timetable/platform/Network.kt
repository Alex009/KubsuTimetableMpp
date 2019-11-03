package com.kubsu.timetable.platform

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build

actual fun whenNetworkConnectionBeActive(
    platformArgs: PlatformArgs,
    onActive: () -> Unit,
    onInactive: () -> Unit
) {
    val connectivityManager = platformArgs
        .context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            onActive()
        }

        override fun onUnavailable() {
            super.onUnavailable()
            onInactive()
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    } else {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }
}