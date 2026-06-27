package com.example.fitnesstrackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnesstrackerapp.data.Roomdatabase.FitnessDatabse
import com.example.fitnesstrackerapp.repository.FitnessRepository
import com.example.fitnesstrackerapp.ui.navigation.AppNavigation
import com.example.fitnesstrackerapp.ui.theme.FitnessTrackerAppTheme
import com.example.fitnesstrackerapp.viewmodel.FitnessViewModelFactory
import com.example.fitnesstrackerapp.viewmodel.FitnessViewmodel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = FitnessDatabse.getDatabase(this)
        val repository = FitnessRepository(database.fitnessDao())
        val factory = FitnessViewModelFactory(repository)

        enableEdgeToEdge()

        setContent {
            val viewModel: FitnessViewmodel = viewModel(factory = factory)
            
            // Fixed: Composable functions like collectAsState must be called within setContent or another Composable.
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()

            FitnessTrackerAppTheme(darkTheme = isDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}
