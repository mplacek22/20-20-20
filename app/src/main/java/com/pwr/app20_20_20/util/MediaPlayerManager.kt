package com.pwr.app20_20_20.util

import android.content.Context
import android.media.MediaPlayer

class MediaPlayerManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playSound(resourceId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, resourceId)
        mediaPlayer?.start()
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
