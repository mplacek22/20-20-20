package com.pwr.app20_20_20

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pwr.app20_20_20.storage.*
import com.pwr.app20_20_20.viewmodels.EyeExerciseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class EyeExerciseIntegrationTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var dao: EyeExerciseDao
    private lateinit var viewModel: EyeExerciseViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.eyeExerciseDao()
        viewModel = EyeExerciseViewModel(database, context.getSharedPreferences("test_prefs", Context.MODE_PRIVATE))
    }

    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

    @Test
    fun testInsertAndGetExercise() = runBlocking {
        val exercise = EyeExercise("1", "Blink", "Blink exercise", Uri.parse("http://example.com"), MediaType.IMAGE)
        val job = viewModel.insertExercise(exercise)
        job.join()

        val retrievedExercise = viewModel.getExercise("1").first()
        Assert.assertEquals(exercise, retrievedExercise)
    }

    @Test
    fun testGetAllExercises() = runBlocking {
        val exercise1 = EyeExercise("1", "Blink", "Blink exercise", Uri.parse("http://example.com"), MediaType.IMAGE)
        val exercise2 = EyeExercise("2", "Focus", "Focus exercise", Uri.parse("http://example.com"), MediaType.VIDEO)
        val job = viewModel.insertAllExercises(listOf(exercise1, exercise2))
        job.join()

        val exercises = viewModel.getAllExercises()
        Assert.assertEquals(2, exercises.size)
        Assert.assertEquals(exercise1, exercises[0])
        Assert.assertEquals(exercise2, exercises[1])
    }
}