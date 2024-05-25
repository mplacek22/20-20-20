package com.pwr.app20_20_20.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [EyeExercise::class], version = 1)
@TypeConverters(UriTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eyeExerciseDao(): EyeExerciseDao
}