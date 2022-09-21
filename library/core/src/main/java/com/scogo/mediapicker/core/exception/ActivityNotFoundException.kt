package com.scogo.mediapicker.core.exception

class ActivityNotFoundException: Exception() {
    override fun toString(): String {
        return "Unable to launch Picker because current activity not found."
    }
}