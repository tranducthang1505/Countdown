/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CountdownViewModel : ViewModel() {

    /**
     * State countdown text
     */
    var countdownText by mutableStateOf("00:00:00")
        private set

    private var countdownJob: Job? = null

    private var remainingSeconds = 0L

    /**
     * Event: Start
     */
    fun setTimerAndStart(hours: Int, minutes: Int, seconds: Int) {
        val totalSeconds = ((hours * (60 * 60)) + minutes * 60 + seconds).toLong()
        setTimeAndStart(totalSeconds)
    }

    private fun setTimeAndStart(totalSeconds: Long) {
        remainingSeconds = totalSeconds
        if (countdownJob?.isCancelled == false) countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            do {
                delay(1000)
                remainingSeconds--
                countdownText = getTextFormatFromSeconds(remainingSeconds)
            } while (totalSeconds > 0)
        }
    }

    /**
     * event: Pause
     */
    fun pause() {
        countdownJob?.cancel()
    }

    /**
     * event: Resume
     */
    fun resume() {
        setTimeAndStart(remainingSeconds)
    }

    /**
     * event: Stop
     */
    fun stopCountdown() {
        countdownJob?.cancel()
        remainingSeconds = 0
        countdownText = getTextFormatFromSeconds(remainingSeconds)
    }

    /**
     * Convert seconds to HH:mm:ss format
     */
    private fun getTextFormatFromSeconds(remainingSeconds: Long) = String.format(
        "%d:%02d:%02d",
        remainingSeconds / 3600,
        (remainingSeconds % 3600) / 60,
        remainingSeconds % 60
    )
}
