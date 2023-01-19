package com.ismealdi.visit.helper

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 *  Single life event implementation
 *
 */
@Parcelize
open class Event<out T>(private val content: @RawValue T?) : Parcelable {

    var hasBeenHandled = false
        private set

    fun getEventIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T? = content

    companion object {
        fun <T> of(event: T?): Event<T> {
            return Event(event)
        }
    }
}

// Post event without wrapping data inside Event in related code
fun <T> MutableLiveData<Event<T>>.postEvent(t: T) {
    value = Event.of(t)
}

fun <T> MutableLiveData<T>?.repost() {
    this?.postValue(this.value)
}