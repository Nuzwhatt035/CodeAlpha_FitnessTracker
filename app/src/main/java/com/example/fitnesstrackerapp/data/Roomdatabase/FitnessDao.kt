package com.example.fitnesstrackerapp.data.Roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FitnessDao {
    //Workout Operation
    @Insert
    suspend fun insertRecord(fitnessEntity: FitnessEntity)

    @Query("SELECT * FROM fitness_records")
    suspend fun getAllRecords(): List<FitnessEntity>

    @Query("SELECT * FROM fitness_records WHERE id = :id")
    suspend fun getRecordById(id: Int): FitnessEntity?

    @Delete
    suspend fun deleteRecord(fitnessEntity: FitnessEntity)

    @Update
    suspend fun updateRecord(fitnessEntity: FitnessEntity)

    @Query("DELETE FROM fitness_records")
    suspend fun deleteAllWorkouts()
//Profile Operation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profileEntity: UserProfileEntity)

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfile(): UserProfileEntity?

    // Flow observer guarantees dynamic UI updates on dashboard emission changes
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observeUserProfile(): Flow<UserProfileEntity?>


}
