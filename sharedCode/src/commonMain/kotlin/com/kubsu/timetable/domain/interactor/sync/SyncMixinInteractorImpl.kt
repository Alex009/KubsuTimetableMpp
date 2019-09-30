package com.kubsu.timetable.domain.interactor.sync

import com.kubsu.timetable.data.storage.user.UserStorage
import com.kubsu.timetable.def
import com.kubsu.timetable.domain.entity.diff.Basename
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity

class SyncMixinInteractorImpl(
    private val userStorage: UserStorage,
    private val gateway: SyncMixinGateway
) : SyncMixinInteractor {
    override suspend fun updateData() = def {
        val user = userStorage.get()!!
        val diffList: List<DataDiffEntity> = gateway.getAvailableDiffList(user.id)

        for (dataDiff in diffList) {
            if (dataDiff.deletedIds.isNotEmpty())
                deleteData(dataDiff.basename, dataDiff.deletedIds)

            gateway.updateData(dataDiff.basename, dataDiff.updatedIds)
        }
    }

    private fun deleteData(basename: Basename, deletedIds: List<Int>) {

    }
}