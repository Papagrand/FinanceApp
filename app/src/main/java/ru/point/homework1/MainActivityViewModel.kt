package ru.point.homework1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val _dataCollected = MutableStateFlow(false)
    val dataCollected = _dataCollected.asStateFlow()

    init {
        viewModelScope.launch {
            _dataCollected.value = true
        }
    }

}