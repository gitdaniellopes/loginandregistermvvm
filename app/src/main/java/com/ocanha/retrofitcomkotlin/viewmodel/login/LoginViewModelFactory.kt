package com.ocanha.retrofitcomkotlin.viewmodel.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ocanha.retrofitcomkotlin.repositories.UserRepository

class LoginViewModelFactory constructor(
    private val repository: UserRepository,
    private val context: Application
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(this.repository, this.context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}