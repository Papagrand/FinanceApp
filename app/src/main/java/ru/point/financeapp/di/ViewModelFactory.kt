package ru.point.financeapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val creators: Map<
        @JvmSuppressWildcards Class<out ViewModel>,
        @JvmSuppressWildcards Provider<ViewModel>,
        >,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val provider =
            creators[modelClass]
                ?: creators.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
                ?: throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        @Suppress("UNCHECKED_CAST")
        return provider.get() as T
    }
}
