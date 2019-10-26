package com.kubsu.timetable.data.network.dto.timetable.data

import com.kubsu.timetable.data.network.dto.diff.FantasticFour

/**
 * It does not have its serialization, because the server developer was
 * too lazy and wrote a universal method of transfer [FantasticFour]
 * (hoping that in the future not only university info will be added).
 */
class UniversityInfoNetworkDto(
    val id: Int,
    val facultyId: Int,
    val typeOfWeek: Int
) {
    constructor(fantasticFour: FantasticFour) : this(
        id = fantasticFour.id,
        facultyId = fantasticFour.objectId,
        typeOfWeek = fantasticFour.data.getValue("current_type_of_week").int
    )
}