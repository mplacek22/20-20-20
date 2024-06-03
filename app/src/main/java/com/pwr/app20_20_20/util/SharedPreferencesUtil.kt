package com.pwr.app20_20_20.util

import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDate

private const val PREF_NAME = "exercise_history_prefs"

object SharedPreferencesModule {
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}

fun saveExerciseHistory(sharedPreferences: SharedPreferences, history: Map<String, List<LocalDate>>) {
    val editor = sharedPreferences.edit()
    history.forEach { (exerciseId, dates) ->
        val dateStrings = dates.joinToString(",") { it.toString() }
        editor.putString(exerciseId, dateStrings)
    }
    editor.apply()
}

fun loadExerciseHistory(sharedPreferences: SharedPreferences): Map<String, List<LocalDate>> {
    val allEntries = sharedPreferences.all
    val history = mutableMapOf<String, List<LocalDate>>()
    allEntries.forEach { (key, value) ->
        val dateStrings = (value as String).split(",")
        val dates = dateStrings.map { LocalDate.parse(it) }
        history[key] = dates
    }
    return history
}
