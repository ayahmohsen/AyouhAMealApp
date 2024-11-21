package com.ayouha.mealapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ayouha.mealapp.db.MealDatabase

class HomeViewModelFactory(
    private val mealDatabase: MealDatabase
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel (mealDatabase) as T
    }

}