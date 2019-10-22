package edu.isel.adeetc.pdm.kotlinx

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

/**
 * Extension function used to obtain, from the current [AppCompatActivity], the view model
 * associated with the given key.
 *
 * @param   [key]   the key that identifies the view model instance
 * @return  The corresponding view model instance, if one exists, or a newly created one
 *
 * @param [T]   The view model concrete type
 */
inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(key: String): T {
    return ViewModelProviders.of(this).get(key, T::class.java)
}

/**
 * Extension function used to obtain, from the current [AppCompatActivity], the view model
 * associated with the given key.
 *
 * @param   [key]       the key that identifies the view model instance
 * @param   [create]    the function to be used to create the view model, if one needs to be created
 * @return  The corresponding view model instance, if one exists, or a newly created one
 *
 * @param [T]   The view model concrete type
 */
inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(
    key: String, crossinline create: () -> T): T {

    @Suppress("UNCHECKED_CAST")
    val factory = object : ViewModelProvider.Factory {
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = create() as VM
    }

    return ViewModelProviders.of(this, factory).get(key, T::class.java)
}
