package com.kubsu.timetable

/**
 * Base class for handling errors/failures.
 */
sealed class Failure

/**
 * Base class for network requests.
 */
sealed class NetworkFailure(val debugMessage: String?) : Failure() {
    class UnknownFailure(
        debugMessage: String?,
        val code: Int,
        val body: String
    ) : NetworkFailure(debugMessage)

    class Connection(debugMessage: String?) : NetworkFailure(debugMessage)
}

/**
 * Failures from business logic.
 */
sealed class DomainFailure : Failure()

object NoActiveUserFailure : DomainFailure()

/**
 * User authentication failure.
 */
sealed class SignInFail : DomainFailure() {
    object IncorrectEmailOrPassword : SignInFail()
    object AccountDeleted : SignInFail()
}

sealed class RegistrationFail : DomainFailure() {
    object InvalidEmail : RegistrationFail()
    object NotUniqueEmail : RegistrationFail()
    object ShortPassword : RegistrationFail()
    object CommonPassword : RegistrationFail()
    object EntirelyNumericPassword : RegistrationFail()
}

/**
 * Contains either a network failure or a domain failure.
 */
class RequestFailure<out DomainFailure> {
    val network: NetworkFailure?
    val domain: DomainFailure?

    constructor(networkFailure: NetworkFailure) {
        network = networkFailure
        domain = null
    }

    constructor(domainFailure: DomainFailure) {
        network = null
        domain = domainFailure
    }

    inline fun <C> fold(
        ifNetworkFailure: (NetworkFailure) -> C,
        ifDomainFailure: (DomainFailure) -> C
    ): C =
        when {
            network != null -> ifNetworkFailure(network)
            domain != null -> ifDomainFailure(domain)
            else -> throw IllegalStateException()
        }

    companion object {
        fun eitherLeft(failure: NetworkFailure): Either<RequestFailure<Nothing>, Nothing> =
            Either.left(RequestFailure(failure))

        fun <D> eitherLeft(failure: D): Either<RequestFailure<D>, Nothing> =
            Either.left(RequestFailure(failure))

        fun <R> eitherRight(right: R): Either<RequestFailure<Nothing>, R> =
            Either.right(right)
    }
}