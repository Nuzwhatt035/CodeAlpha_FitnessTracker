package com.example.fitnesstrackerapp.data.Roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "fitness_records")

data class FitnessEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val exerciseType: String,
    val durationMinutes: Int,
    val caloriesBurnedRecord: Int,
    val stepsRecord:  Int

)
