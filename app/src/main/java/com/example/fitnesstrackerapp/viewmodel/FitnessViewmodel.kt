package com.example.fitnesstrackerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstrackerapp.data.Roomdatabase.FitnessEntity
import com.example.fitnesstrackerapp.data.Roomdatabase.UserProfileEntity
import com.example.fitnesstrackerapp.repository.FitnessRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel holding UI states and executing asynchronous background DB transactions.
 */
class FitnessViewmodel(
    private val repository: FitnessRepository
) : ViewModel() {

    // State flow holding recent workout records list
    private val _records = MutableStateFlow<List<FitnessEntity>>(emptyList())
    val records: StateFlow<List<FitnessEntity>> = _records

    // Holds observable state for the user profile configuration
    private val _userProfile = MutableStateFlow<UserProfileEntity?>(null)
    val userProfile: StateFlow<UserProfileEntity?> = _userProfile

    // State tracking light/dark mode theme preferences
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    /**
     * Single initialization block to load initial app data safely on launch.
     */
    init {
        loadRecords()
        loadUserProfile()
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun insertRecord(fitnessEntity: FitnessEntity) {
        viewModelScope.launch {
            repository.insertRecord(fitnessEntity)
            loadRecords()
        }
    }

    fun clearAllWorkouts() {
        viewModelScope.launch {
            repository.deleteAllWorkouts()
            // Suspend briefly to ensure Room transaction commits before reloading
            delay(100)
            loadRecords()
        }
    }

    fun deleteRecord(fitnessEntity: FitnessEntity) {
        viewModelScope.launch {
            repository.deleteRecord(fitnessEntity)
            loadRecords()
        }
    }

    fun loadRecords() {
        viewModelScope.launch {
            // Fetches persistent list from the repository
            _records.value = repository.getAllRecords()
        }
    }

    suspend fun getRecordById(id: Int): FitnessEntity? {
        return repository.getRecordById(id)
    }

    fun updateRecord(fitnessEntity: FitnessEntity) {
        viewModelScope.launch {
            repository.updateRecord(fitnessEntity)
            loadRecords()
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _userProfile.value = repository.getUserProfile()
        }
    }

    /**
     * Saves user profile parameters into the local SQLite table.
     */
    fun saveProfile(name: String, age: Int, height: Float, weight: Float, gender: String, goal: String) {
        viewModelScope.launch {
            val profile = UserProfileEntity(
                id = 1, // Enforces single row replacement
                fullName = name,
                age = age,
                heightCm = height,
                weightKg = weight,
                gender = gender,
                fitnessGoal = goal
            )
            repository.insertOrUpdateProfile(profile)
            _userProfile.value = profile
        }
    }
}