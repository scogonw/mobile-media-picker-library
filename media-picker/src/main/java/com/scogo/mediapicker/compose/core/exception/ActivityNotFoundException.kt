package com.scogo.mediapicker.compose.core.exception

internal class ActivityNotFoundException: Exception() {
    override fun toString(): String {
        return "Unable to launch Picker because current activity not found."
    }
}