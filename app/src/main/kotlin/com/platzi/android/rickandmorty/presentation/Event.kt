package com.platzi.android.rickandmorty.presentation

/**
 * Manage the route on App
 *
 * @author kevc77
 */
class Event <out T>(private val content: T) {
    private var hasBeenHandle = false

    fun getContentIfNotHandle(): T? = if (hasBeenHandle) {
        null
    } else {
        hasBeenHandle = true
        content
    }
}