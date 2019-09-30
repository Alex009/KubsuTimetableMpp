package com.kubsu.timetable

/**
 * Base class for handling errors/failures.
 */
sealed class Failure

/**
 * Base class for network requests.
 */
sealed class NetworkFailure : Failure() {
    class SomeFailure(val debugMessage: String) : NetworkFailure()
    object Connection : NetworkFailure()
}

/**
 * Contains either a network failure or a domain failure.
 */
class WrapperFailure<out D : DomainFailure> {
    val network: NetworkFailure?
    val domain: D?

    constructor(networkFailure: NetworkFailure) {
        network = networkFailure
        domain = null
    }

    constructor(domainFailure: D) {
        network = null
        domain = domainFailure
    }

    inline fun <C> fold(
        ifNetworkFailure: (NetworkFailure) -> C,
        ifDomainFailure: (D) -> C
    ): C =
        when {
            network != null -> ifNetworkFailure(network)
            domain != null -> ifDomainFailure(domain)
            else -> throw IllegalStateException()
        }
}

/**
 * Failures from business logic.
 */
sealed class DomainFailure : Failure()

object NoActiveUserFailure : DomainFailure()

/**
 * User authentication failure.
 */
sealed class AuthFail : DomainFailure() {
    object EmptyEmail : AuthFail()
    object EmptyPassword : AuthFail()
    object IncorrectEmail : AuthFail()
    object IncorrectPassword : AuthFail()
}