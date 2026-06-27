package com.example.fitnesstrackerapp.data.Roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FitnessEntity::class, UserProfileEntity::class],
    version = 2 // Bumped up version to accommodate the new user_profile table cleanly
)
abstract class FitnessDatabse : RoomDatabase() {

    abstract fun fitnessDao(): FitnessDao

    companion object {
        @Volatile
        private var INSTANCE: FitnessDatabse? = null

        fun getDatabase(context: Context): FitnessDatabse {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitnessDatabse::class.java,
                    "fitness_databse" // Kept your local db filename string
                )
                    .fallbackToDestructiveMigration() // Wipes tables if version changes without explicit migration
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}