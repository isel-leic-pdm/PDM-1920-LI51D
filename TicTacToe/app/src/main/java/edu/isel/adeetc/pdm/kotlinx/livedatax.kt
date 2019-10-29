package edu.isel.adeetc.pdm.kotlinx

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Extension function used to obtain register the given function to be called whenever the associated
 * [MutableLiveData] instance contents change.
 *
 * @param   [owner]     the host whose lifecycle is to be respected
 * @param   [onChange]  the function to be called to produce the notification
 *
 * @param [T]   The mutable live data createdChallenge's concrete type
 */
inline fun <T> MutableLiveData<T>.observe(owner: LifecycleOwner, crossinline onChange: (T) -> Unit) {
    this.observe(owner, Observer { onChange(it) })
}
