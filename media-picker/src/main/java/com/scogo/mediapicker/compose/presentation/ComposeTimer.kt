package com.scogo.mediapicker.compose.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import kotlin.math.roundToInt

internal class ComposeTimer {
    private val timer = Timer()
    private var timerTask: TimerTask? = null
    private var timerTime: Double = 0.0

    private val _time = MutableStateFlow("00:00")
    private val time: StateFlow<String> = _time
    fun readTime() = time

    companion object {
        fun get(): ComposeTimer {
            return ComposeTimer()
        }
    }

    fun start() {
        timerTask = object: TimerTask() {
            override fun run() {
                timerTime++
                calculateTime()
            }
        }
        timer.scheduleAtFixedRate(timerTask,0,1000)
    }

    fun stop() {
        timerTask?.cancel()
        _time.value = ""
        timerTime = 0.0
    }

    private fun calculateTime() {
        val rounded = timerTime.roundToInt()
        val seconds = ((rounded % 86400) % 3600) % 60
        val minutes = ((rounded % 86400) % 3600) / 60
        val hours = ((rounded % 86400) / 3600)

        val readableTime = String.format("%02d:%02d",minutes.toBigInteger(),seconds.toBigInteger())
        _time.value = readableTime
    }
}