package com.ocanha.retrofitcomkotlin.viewmodel.newrecipe

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ocanha.retrofitcomkotlin.repositories.RecipeRepository

class NewRecipeViewModelFactory constructor(private val repository: RecipeRepository,
private val context: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NewRecipeViewModel::class.java)) {
            NewRecipeViewModel(this.repository, this.context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}