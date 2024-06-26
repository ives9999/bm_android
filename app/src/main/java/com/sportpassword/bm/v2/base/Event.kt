package com.sportpassword.bm.v2.base

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents on event.
 */

class Event<out T>(private val content: T) {
    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

}