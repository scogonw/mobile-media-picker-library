package com.scogo.mediapicker.core.exception

class PickerWorkRequestNotFound: Exception() {
    override fun toString(): String {
        return "Picker work request not found"
    }
}