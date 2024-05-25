package com.pwr.app20_20_20.storage

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eye_exercises")
data class EyeExercise(
    @PrimaryKey
    val id: String,
    val name: String,
    val instruction: String,
    val mediaUri: Uri
)
