package com.example.tictactoe.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class SoundManager(private val context: Context) {
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun playMoveSound() {
        try {
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 50)
            vibrate(50)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playWinSound() {
        try {
            // Play victory melody
            playSequence(
                listOf(
                    ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD to 200L,
                    ToneGenerator.TONE_CDMA_CONFIRM to 300L
                )
            )
            vibrate(200)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playDrawSound() {
        try {
            // Play draw melody
            playSequence(
                listOf(
                    ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE to 200L,
                    ToneGenerator.TONE_CDMA_LOW_SS to 300L
                )
            )
            vibrate(100)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playSequence(tones: List<Pair<Int, Long>>) {
        Thread {
            tones.forEach { (tone, duration) ->
                toneGenerator.startTone(tone, duration.toInt())
                Thread.sleep(duration)
            }
        }.start()
    }

    private fun vibrate(duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }

    fun release() {
        try {
            toneGenerator.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
} 