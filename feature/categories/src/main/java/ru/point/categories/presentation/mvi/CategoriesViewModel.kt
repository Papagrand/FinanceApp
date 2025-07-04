package ru.point.categories.presentation.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.point.api.model.CategoryDto
import ru.point.categories.domain.usecase.ObserveCategoriesUseCase
import ru.point.utils.common.AccountPreferences
import ru.point.utils.common.Result
import ru.point.utils.model.AppError
import ru.point.utils.network.NetworkTracker
import javax.inject.Inject

/**
 * CategoriesViewModel
 *
 * Ответственность:
 * - получение и фильтрация списка категорий через ObserveCategoriesUseCase;
 * - управление MVI-потоком: приём интентов (Load, Retry, Search), обновление состояния и эмиссия эффектов (Snackbar);
 *
 */

class CategoriesViewModel @Inject constructor(
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val prefs: AccountPreferences,
    internal val tracker: NetworkTracker,
) : ViewModel() {
    private val intents = MutableSharedFlow<CategoriesIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CategoriesEffect>()
    val effect: SharedFlow<CategoriesEffect> = _effect.asSharedFlow()

    private val _accountId = MutableStateFlow<Int?>(null)

    init {
        viewModelScope.launch {
            prefs.accountIdFlow
                .filterNotNull()
                .collectLatest { id ->
                    _accountId.value = id
                    performLoad(id)
                }
        }

        viewModelScope.launch {
            intents.collectLatest { intent ->
                when (intent) {
                    CategoriesIntent.Load,
                    CategoriesIntent.Retry,
                    -> {
                        _accountId.value
                            ?.let { performLoad(it) }
                            ?: _effect.emit(
                                CategoriesEffect.ShowSnackbar(
                                    "Account ID ещё не инициализирован",
                                ),
                            )
                    }

                    is CategoriesIntent.Search -> {
                        _state.update { it.copy(query = intent.query) }

                        val newFiltered = applyQuery(_state.value.rawList, intent.query)

                        _state.update { it.copy(list = newFiltered) }
                    }
                }
            }
        }

        dispatch(CategoriesIntent.Load)
    }

    fun dispatch(intent: CategoriesIntent) {
        viewModelScope.launch {
            intents.emit(intent)
        }
    }

    private fun performLoad(accountId: Int) {
        viewModelScope.launch {
            observeCategoriesUseCase(accountId).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }

                    is Result.Success -> {
                        val full = result.data
                        val filtered = applyQuery(full, _state.value.query)

                        _state.update {
                            it.copy(
                                isLoading = false,
                                rawList = full,
                                list = filtered,
                                error = null,
                            )
                        }
                    }

                    is Result.Error -> {
                        Log.e("WhyCause", result.cause.toString())
                        val msg =
                            when (val cause = result.cause) {
                                AppError.BadRequest -> "Неверный формат данных"
                                AppError.Unauthorized -> "Неавторизованный доступ"
                                AppError.NoInternet -> "Нет подключения к интернету"
                                is AppError.ServerError -> "Сервер временно недоступен"
                                is AppError.Http -> "HTTP ${cause.code}: ${cause.body ?: "Ошибка"}"
                                else -> "Неизвестная ошибка"
                            }
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = msg,
                            )
                        }
                        _effect.emit(CategoriesEffect.ShowSnackbar("Ошибка: $msg"))
                    }
                }
            }
        }
    }

    private fun applyQuery(
        list: List<CategoryDto>,
        query: String,
    ): List<CategoryDto> {
        if (query.isBlank()) return list
        val q = query.trim().lowercase()
        return list.filter { it.categoryName.lowercase().contains(q) }
    }
}
