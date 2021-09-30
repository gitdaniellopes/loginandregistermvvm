package com.ocanha.retrofitcomkotlin.viewmodel.newrecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ocanha.retrofitcomkotlin.datastore.SessionDataStore
import com.ocanha.retrofitcomkotlin.model.Recipe
import com.ocanha.retrofitcomkotlin.repositories.RecipeRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class NewRecipeViewModel constructor(
    private val repository: RecipeRepository,
    context: Application
) : AndroidViewModel(context) {

    private val session = SessionDataStore(context)
    val status = MutableLiveData<Boolean>()

    fun getToken(): String {
        var token: String? = null
        viewModelScope.launch {
            val authToken = session.token.first()
            authToken?.let {
                token = it
            }
        }
        return token.toString()
    }

    fun saveRecipe(token: String, recipe: Recipe) {

        val request = repository.saveRecipe(token, recipe)
        request.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.code() == HttpURLConnection.HTTP_OK) {
                    status.postValue(true)
                } else {
                    status.postValue(false)
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                status.postValue(false)
            }
        })
    }
}