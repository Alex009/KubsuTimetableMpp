package com.kubsu.timetable

/**
 * Base class for handling errors/failures.
 */
sealed class Failure

sealed class DataFailure(val debugMessage: String?) : Failure() {
    class UnknownResponse(
        val code: Int,
        val body: String,
        debugMessage: String? = null
    ) : DataFailure(debugMessage)

    class NotAuthenticated(debugMessage: String?) : DataFailure(debugMessage)

    class ConnectionToRepository(debugMessage: String?) : DataFailure(debugMessage)
}

/**
 * Failures from business logic.
 */
sealed class DomainFailure : Failure()

/**
 * User authentication failure.
 */
sealed class SignInFail : DomainFailure() {
    object IncorrectEmailOrPassword : SignInFail()
    object AccountInactivate : SignInFail()
    object InvalidEmail : SignInFail()
}

sealed class RegistrationFail : DomainFailure() {
    sealed class Email : RegistrationFail() {
        object Invalid : Email()
        object NotUnique : Email()
    }

    sealed class Password : RegistrationFail() {
        object TooShort : Password()
        object TooCommon : Password()
        object EntirelyNumeric : Password()
    }
}

sealed class UserUpdateFail : DomainFailure() {
    object TooLongFirstName : UserUpdateFail()
    object TooLongLastName : UserUpdateFail()
    class Info(val fail: RegistrationFail) : UserUpdateFail()
}

sealed class SubscriptionFail : DomainFailure() {
    object TooLongTitle : SubscriptionFail()
    object SubscriptionAlreadyExists : SubscriptionFail()
}

/**
 * Contains either a network failure or a domain failure.
 */
class RequestFailure<D>(
    val domain: D? = null,
    val data: List<DataFailure>? = null
) {
    constructor(dataFail: DataFailure) : this(data = listOf(dataFail))

    fun handle(ifDomain: (D) -> Unit, ifData: (List<DataFailure>) -> Unit) {
        domain?.let(ifDomain)
        data?.let(ifData)
    }

    fun plus(requestFailure: RequestFailure<D>, domainPlus: (D?, D?) -> D?): RequestFailure<D> =
        RequestFailure(
            domain = domainPlus(domain, requestFailure.domain),
            data = data
                ?.plus(requestFailure.data ?: emptyList())
                ?.toList()
        )

    fun <R> mapDomain(block: (D?) -> R?): RequestFailure<R> =
        RequestFailure(block(domain))
}