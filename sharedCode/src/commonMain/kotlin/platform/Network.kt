package platform

expect fun whenNetworkConnectionBeActive(
    platformArgs: PlatformArgs,
    onActive: () -> Unit,
    onInactive: () -> Unit
)