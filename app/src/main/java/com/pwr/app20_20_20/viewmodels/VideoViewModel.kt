package com.pwr.app20_20_20.viewmodels

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.pwr.app20_20_20.storage.EyeExercise

class VideoViewModel(
    eyeExercise: EyeExercise,
    var player: Player
) : ViewModel(){

    private var mediaItem = MediaItem.fromUri(eyeExercise.mediaUri)

    init {
        player.prepare()
        player.addMediaItem(mediaItem)
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}