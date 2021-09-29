package com.ocanha.retrofitcomkotlin.datastore

import kotlinx.coroutines.flow.Flow

interface SessionInterface {

    val token: Flow<String?>

    suspend fun saveToken(token: String)

    suspend fun clear()

    suspend fun isUserLogged(): Boolean
}