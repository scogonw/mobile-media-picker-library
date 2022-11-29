package com.scogo.mediapicker.compose.presentation.media

internal data class MediaUiState(
    val message: String
) {
    companion object {
        val EMPTY = MediaUiState(
            message = ""
        )
    }
}
