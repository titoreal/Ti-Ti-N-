package com.example.tttapp.utils

import android.content.Context
import android.media.MediaPlayer



object SoundManager {
    fun playSound(context: Context, resId: Int) {
        val mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
    }
}