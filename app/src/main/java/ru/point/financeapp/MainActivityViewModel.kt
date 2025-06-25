package ru.point.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor() : ViewModel() {

    private val _dataCollected = MutableStateFlow(false)
    val dataCollected = _dataCollected.asStateFlow()

    init {
        viewModelScope.launch {
            _dataCollected.value = true
        }
    }

}