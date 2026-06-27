package com.example.fitnesstrackerapp.ui.navigation

object AppRoute {

    const val DASHBOARD = "dashboard"
    const val ADD_WORKOUT = "add_workout"

    //Dynamic route that accepts a primary key ID for editing an existing recird
    const val EDIT_WORKOUT = "edit_workout/{workoutId}"

    //Helper fn to safely build the edit route with an actual ID
    fun editWorkoutRoute(workoutId: Int): String {
        return "edit_workout/$workoutId"
    }
}
