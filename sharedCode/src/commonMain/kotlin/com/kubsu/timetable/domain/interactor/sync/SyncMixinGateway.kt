package com.kubsu.timetable.domain.interactor.sync

import com.kubsu.timetable.domain.entity.diff.Basename
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity

interface SyncMixinGateway {
    suspend fun newDataDiff(entity: DataDiffEntity)
    suspend fun getAvailableDiffList(userId: Int): List<DataDiffEntity>
    suspend fun updateData(basename: Basename, updatedIds: List<Int>)
}