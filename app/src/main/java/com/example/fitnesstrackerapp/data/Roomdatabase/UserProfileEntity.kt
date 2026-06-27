package com.example.fitnesstrackerapp.data.Roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database entity storing persistent user profile configurations, restricted to a single row (id = 1).
 */
@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val fullName: String,
    val age: Int,
    val heightCm: Float,
    val weightKg: Float,
    val gender: String,
    val fitnessGoal: String = "Maintain Fitness"
)