package com.example.fitnesstrackerapp.repository

import com.example.fitnesstrackerapp.data.Roomdatabase.FitnessDao
import com.example.fitnesstrackerapp.data.Roomdatabase.FitnessEntity
import com.example.fitnesstrackerapp.data.Roomdatabase.UserProfileEntity

class FitnessRepository(
    private val fitnessDao: FitnessDao
) {
    suspend fun insertRecord(fitnessEntity: FitnessEntity) {
        fitnessDao.insertRecord(fitnessEntity)
    }

    suspend fun getAllRecords(): List<FitnessEntity> {
        return fitnessDao.getAllRecords()
    }

    suspend fun getRecordById(id: Int): FitnessEntity? {
        return fitnessDao.getRecordById(id)
    }

    suspend fun deleteRecord(fitnessEntity: FitnessEntity) {
        fitnessDao.deleteRecord(fitnessEntity)
    }

    suspend fun updateRecord(fitnessEntity: FitnessEntity) {
        fitnessDao.updateRecord(fitnessEntity)
    }

    suspend fun deleteAllWorkouts() {
        fitnessDao.deleteAllWorkouts()
    }

    suspend fun insertOrUpdateProfile(profile: UserProfileEntity) = fitnessDao.insertOrUpdateProfile(profile)
    suspend fun getUserProfile(): UserProfileEntity? = fitnessDao.getUserProfile()
    fun observeUserProfile() = fitnessDao.observeUserProfile()
}
