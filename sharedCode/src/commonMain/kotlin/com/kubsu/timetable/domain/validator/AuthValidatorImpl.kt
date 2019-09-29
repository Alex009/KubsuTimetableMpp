package com.kubsu.timetable.domain.validator

import com.kubsu.timetable.Either
import com.kubsu.timetable.AuthFail

class AuthValidatorImpl : AuthValidator {
    override fun validateData(email: String?, password: String?): Either<AuthFail, Unit> {
        if (email == null || email.isEmpty())
            return Either.left(AuthFail.EmptyEmail)

        if (password == null || password.isEmpty())
            return Either.left(AuthFail.EmptyPassword)

        return Either.right(Unit)
    }
}