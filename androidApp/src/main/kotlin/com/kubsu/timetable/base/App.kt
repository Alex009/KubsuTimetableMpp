package com.kubsu.timetable.base

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.kubsu.timetable.InformationSynchronizer
import com.kubsu.timetable.di.appKodein
import com.kubsu.timetable.domain.interactor.sync.SyncMixinInteractor
import com.kubsu.timetable.domain.interactor.userInfo.UserInteractor
import com.kubsu.timetable.utils.logics.activityLifecycleInjector
import io.fabric.sdk.android.Fabric
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.erased.instance
import platform.PlatformArgs

class App : Application(), KodeinAware {
    override val kodein: Kodein by lazy { appKodein(this) }

    private val lifecycleCallbacks by kodein.instance<ActivityLifecycleCallbacks>()
    private val userInteractor by kodein.instance<UserInteractor>()
    private val syncMixinInteractor by kodein.instance<SyncMixinInteractor>()
    private val platformArgs by kodein.instance<PlatformArgs>()

    private val informationSynchronizer by lazy {
        InformationSynchronizer(
            userInteractor = userInteractor,
            syncMixinInteractor = syncMixinInteractor,
            platformArgs = platformArgs
        )
    }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(lifecycleCallbacks)
        registerActivityLifecycleCallbacks(activityLifecycleInjector)

        Fabric.with(this, Crashlytics())

        informationSynchronizer.awaitConnectionAndSync()
    }
}