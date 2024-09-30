package com.github.yohannestz.satori.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module

const val SATORI_DEFAULT_DATASTORE = "satori_datastore"

val dataStoreModule = module {
    single {
        provideDataStore(get(), SATORI_DEFAULT_DATASTORE)
    }
}

private fun provideDataStore(context: Context, name: String) =
    PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile(name)
    }

fun <T> DataStore<Preferences>.getValue(key: Preferences.Key<T>) = data.map { it[key] }

fun <T> DataStore<Preferences>.getValue(
    key: Preferences.Key<T>,
    default: T,
) = data.map { it[key] ?: default }

suspend fun <T> DataStore<Preferences>.setValue(
    key: Preferences.Key<T>,
    value: T?
) = edit {
    if (value != null) it[key] = value
    else it.remove(key)
}

fun <T> DataStore<Preferences>.getValueBlocking(key: Preferences.Key<T>) =
    runBlocking { data.first() }[key]