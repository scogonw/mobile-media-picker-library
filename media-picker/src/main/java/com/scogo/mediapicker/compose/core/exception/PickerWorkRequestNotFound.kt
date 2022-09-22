package com.scogo.mediapicker.compose.core.exception

internal class PickerWorkRequestNotFound: Exception() {
    override fun toString(): String {
        return "Picker work request not found"
    }
}