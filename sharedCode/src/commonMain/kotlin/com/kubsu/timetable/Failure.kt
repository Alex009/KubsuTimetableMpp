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

    class ParsingError(debugMessage: String) : DataFailure(debugMessage)

    class NotAuthenticated(debugMessage: String? = null) : DataFailure(debugMessage)

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
    object RequiredEmail : SignInFail()
    object RequiredPassword : SignInFail()
}

sealed class UserInfoFail : DomainFailure() {
    sealed class FirstName : UserInfoFail() {
        object TooLong : FirstName()
    }

    sealed class LastName : UserInfoFail() {
        object TooLong : LastName()
    }

    sealed class Email : UserInfoFail() {
        object Invalid : Email()
        object NotUnique : Email()
        object Required : Email()
    }

    sealed class Password : UserInfoFail() {
        object TooShort : Password()
        object TooCommon : Password()
        object EntirelyNumeric : Password()
        object Required : Password()
    }
}

sealed class SubscriptionFail : DomainFailure() {
    object TooLongTitle : SubscriptionFail()
    object RequiredTitle : SubscriptionFail()
}

/**
 * Contains either a network failure or a domain failure.
 */
class RequestFailure<D>(
    val domain: D? = null,
    val data: List<DataFailure>? = null
) {
    constructor(dataFail: DataFailure) : this(data = listOf(dataFail))

    inline fun handle(ifDomain: (D) -> Unit, ifData: (List<DataFailure>) -> Unit) {
        domain?.let(ifDomain)
        data?.let(ifData)
    }

    fun plus(requestFailure: RequestFailure<D>, domainPlus: (D?, D?) -> D?): RequestFailure<D> {
        val oldDataList = data ?: emptyList()
        val newDataList = requestFailure.data ?: emptyList()
        return RequestFailure(
            domain = domainPlus(domain, requestFailure.domain),
            data = oldDataList + newDataList
        )
    }

    fun <R> mapDomain(block: (D?) -> R?): RequestFailure<R> =
        RequestFailure(block(domain))
}