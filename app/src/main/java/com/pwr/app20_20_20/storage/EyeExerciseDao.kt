package com.pwr.app20_20_20.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EyeExerciseDao {
    @Query("SELECT * FROM eye_exercises WHERE id = :exerciseId")
    fun getExerciseById(exerciseId: String): Flow<EyeExercise>

    @Query("SELECT * FROM eye_exercises")
    fun getAllExercises(): Flow<List<EyeExercise>>

    @Query("SELECT * FROM eye_exercises")
    suspend fun getAllExercisesList(): List<EyeExercise>

    @Insert
    suspend fun insertExercise(exercise: EyeExercise)

    @Insert
    suspend fun insertAll(exercises: List<EyeExercise>)
}