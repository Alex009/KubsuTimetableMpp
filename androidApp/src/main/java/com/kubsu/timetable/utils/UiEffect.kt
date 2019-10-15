package com.kubsu.timetable.utils

class UiEffect<T>(initialValue: T) {
    var value = initialValue
        set(value) {
            if (field != value)
                field = value
            change?.invoke(value)
        }

    var change: ((T) -> Unit)? = null
}

infix fun <T> UiEffect<T>.bind(body: (T) -> Unit) {
    change = body
    body.invoke(value)
}

fun <T> UiEffect<T>.unbind() {
    change = null
}