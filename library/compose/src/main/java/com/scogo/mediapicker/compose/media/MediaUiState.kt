package com.scogo.mediapicker.compose.media

data class MediaUiState(
    val message: String
) {
    companion object {
        val EMPTY = MediaUiState(
            message = ""
        )
    }
}
