package com.example.fitnesstrackerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnesstrackerapp.repository.FitnessRepository

/**
 * Factory supplying dependencies down into ViewModel constructors.
 */
class FitnessViewModelFactory(
    private val repository: FitnessRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(FitnessViewmodel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FitnessViewmodel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}