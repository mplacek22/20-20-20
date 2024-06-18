package com.pwr.app20_20_20

import android.net.Uri
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.core.app.ApplicationProvider
import com.pwr.app20_20_20.storage.AppDatabase
import com.pwr.app20_20_20.storage.EyeExercise
import com.pwr.app20_20_20.storage.EyeExerciseDao
import com.pwr.app20_20_20.storage.MediaType
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: EyeExerciseDao

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.eyeExerciseDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun testInsertAndGetExercise() = runBlocking {
        val exercise = EyeExercise("1", "Blink", "Blink exercise", Uri.parse("http://example.com"), MediaType.IMAGE)
        dao.insertExercise(exercise)

        val retrievedExercise = dao.getExerciseById("1").first()
        assertThat(retrievedExercise.id, `is`("1"))
        assertThat(retrievedExercise.name, `is`("Blink"))
    }

    @Test
    fun testGetAllExercises() = runBlocking {
        val exercise1 = EyeExercise("1", "Blink", "Blink exercise", Uri.parse("http://example.com"), MediaType.IMAGE)
        val exercise2 = EyeExercise("2", "Focus", "Focus exercise", Uri.parse("http://example.com"), MediaType.VIDEO)
        dao.insertAll(listOf(exercise1, exercise2))

        val exercises = dao.getAllExercises().first()
        assertThat(exercises.size, `is`(2))
        assertThat(exercises[0].id, `is`("1"))
        assertThat(exercises[1].id, `is`("2"))
    }
}