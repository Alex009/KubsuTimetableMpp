package com.kubsu.timetable.domain.validator

import com.kubsu.timetable.Either
import com.kubsu.timetable.AuthFail

interface AuthValidator {
    fun validateData(email: String?, password: String?): Either<AuthFail, Unit>
}