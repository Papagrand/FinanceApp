package ru.point.transactions.addOrEditTransaction.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.api.model.CategoryDto
import ru.point.transactions.addOrEditTransaction.domain.usecase.AddTransactionUseCase
import ru.point.transactions.addOrEditTransaction.domain.usecase.DeleteTransactionUseCase
import ru.point.transactions.addOrEditTransaction.domain.usecase.GetAllCategoriesUseCase
import ru.point.transactions.addOrEditTransaction.domain.usecase.GetTransactionInfoUseCase
import ru.point.transactions.addOrEditTransaction.domain.usecase.UpdateTransactionUseCase
import ru.point.transactions.addOrEditTransaction.domain.vmValidator.AddOrEditTransactionValidator
import ru.point.transactions.addOrEditTransaction.ui.mvi.AddOrEditTransactionEffect.Finish
import ru.point.transactions.addOrEditTransaction.ui.mvi.AddOrEditTransactionEffect.ShowSnackbar
import ru.point.utils.common.Result
import ru.point.utils.extensionsAndParsers.DateParcer
import ru.point.utils.extensionsAndParsers.ScreenEnums
import ru.point.utils.extensionsAndParsers.buildIsoInstantString
import ru.point.utils.extensionsAndParsers.validateBalance
import ru.point.utils.model.toUserMessage
import javax.inject.Inject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class AddOrEditTransactionViewModel @Inject constructor(
    private val transactionId: Int?,
    private val isIncome: Boolean,
    private val getTransactionInfoUseCase: GetTransactionInfoUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val prefs: AccountPreferencesRepo,
) : ViewModel(), DateParcer {
    private val intents = MutableSharedFlow<AddOrEditTransactionIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(AddOrEditTransactionState())
    val state: StateFlow<AddOrEditTransactionState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AddOrEditTransactionEffect>()
    val effect: SharedFlow<AddOrEditTransactionEffect> = _effect.asSharedFlow()

    private val _accountId = MutableStateFlow<Int?>(null)

    private val _accountName = MutableStateFlow<String?>(null)
    val accountName: StateFlow<String?> = _accountName

    private val _lastUpdate: StateFlow<String> =
        prefs.lastUpdateFlow
            .filterNotNull()
            .map { iso ->
                try {
                    Instant.parse(iso)
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm"))
                } catch (e: Exception) {
                    ""
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(1_000),
                initialValue = ""
            )
    val lastUpdate: StateFlow<String> = _lastUpdate

    init {
        viewModelScope.launch {
            val mode =
                if (transactionId == null || (transactionId < 0 && transactionId>-100)) {
                    ScreenEnums.CREATE
                } else {
                    ScreenEnums.EDIT
                }

            _state.update {
                it.copy(
                    screenMode = mode,
                    transactionId = transactionId,
                    isIncome = isIncome,
                )
            }

            if (mode == ScreenEnums.EDIT) {
                loadTransactionInfo(requireNotNull(transactionId))
            }
        }

        viewModelScope.launch {
            getAllCategories()
        }

        viewModelScope.launch {
            prefs.accountIdFlow
                .filterNotNull()
                .collectLatest { id ->
                    _accountId.value = id
                }
        }

        viewModelScope.launch {
            prefs.accountNameFlow
                .filterNotNull()
                .collect { name ->
                    _accountName.value = name
                }
        }

        viewModelScope.launch {
            intents.collectLatest { intent ->
                when (intent) {
                    is AddOrEditTransactionIntent.Load,

                    AddOrEditTransactionIntent.ClearError,
                    -> _state.update { it.copy(error = null) }

                    AddOrEditTransactionIntent.Retry -> retryLastRequest()

                    AddOrEditTransactionIntent.Cancel -> finishEditing()

                    AddOrEditTransactionIntent.Save ->
                        onSave()

                    AddOrEditTransactionIntent.DeleteTransaction -> deleteTransaction()

                    is AddOrEditTransactionIntent.CategoryPicked -> {
                        _state.update { it.copy(pickedCategory = intent.category) }
                    }

                    is AddOrEditTransactionIntent.AmountChanged -> changeAmount(intent.newAmount)

                    is AddOrEditTransactionIntent.DateChanged -> {
                        _state.update { it.copy(date = intent.date) }
                    }

                    is AddOrEditTransactionIntent.TimeChanged -> {
                        _state.update { it.copy(time = intent.time) }
                    }

                    is AddOrEditTransactionIntent.CommentChanged -> {
                        _state.update { it.copy(comment = intent.comment) }
                    }
                }
            }
        }
    }

    fun dispatch(intent: AddOrEditTransactionIntent) {
        intents.tryEmit(intent)
    }

    private fun getAllCategories() {
        viewModelScope.launch {
            getAllCategoriesUseCase(_state.value.isIncome).collect { result ->
                when (result) {
                    is Result.Loading -> _state.update { it.copy(isLoading = true, error = null) }
                    is Result.Error -> {
                        val msg = result.cause.toUserMessage()
                        _state.update { it.copy(isLoading = false, error = msg) }
                        _effect.emit(ShowSnackbar("Ошибка: $msg"))
                    }

                    is Result.Success ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                allCategories = result.data,
                                error = null,
                            )
                        }
                }
            }
        }
    }

    private suspend fun onSave() {
        val st = _state.value

        val result =
            AddOrEditTransactionValidator.validate(
                accountId = _accountId.value,
                categoryId = st.pickedCategory?.categoryId,
                amountRaw = st.amountInput,
                amountValid = st.amountValid,
                date = st.date,
                time = st.time,
            )
        if (!result.isValid) {
            viewModelScope.launch { _effect.emit(ShowSnackbar(result.message!!)) }
            return
        }

        val datetime = buildIsoInstantString(st.date, st.time)
        if (datetime == null) {
            viewModelScope.launch {
                _effect.emit(ShowSnackbar("Неверная дата/время"))
            }
            return
        } else {
            _state.update {
                it.copy(
                    datetime = datetime,
                )
            }
        }

        _accountId.value
            ?.let {
                if (st.screenMode == ScreenEnums.CREATE) {
                    addTransaction(it)
                } else {
                    updateTransaction(it)
                }
            }
            ?: _effect.emit(
                ShowSnackbar(
                    "Account ID ещё не инициализирован",
                ),
            )
    }

    private fun retryLastRequest() =
        viewModelScope.launch {
            _state.update { it.copy(error = null) }

            if (_state.value.screenMode == ScreenEnums.EDIT && _state.value.transactionId != null) {
                _state.value.transactionId?.let { loadTransactionInfo(it) }
            } else {
                _accountId.value
                    ?.let {
                        if (_state.value.screenMode == ScreenEnums.CREATE) {
                            addTransaction(it)
                        } else {
                            updateTransaction(it)
                        }
                    }
                    ?: _effect.emit(
                        ShowSnackbar(
                            "Account ID ещё не инициализирован",
                        ),
                    )
            }

            if (_state.value.allCategories.isEmpty()) {
                getAllCategories()
            }
        }

    private fun loadTransactionInfo(transactionId: Int) {
        viewModelScope.launch {
            getTransactionInfoUseCase(transactionId).collect { result ->
                when (result) {
                    is Result.Loading -> _state.update { it.copy(isLoading = true, error = null) }
                    is Result.Error -> {
                        val msg = result.cause.toUserMessage()
                        _state.update { it.copy(isLoading = false, error = msg) }
                        _effect.emit(ShowSnackbar("Ошибка: $msg"))
                    }

                    is Result.Success ->
                        _state.update {
                            val dto = result.data
                            it.copy(
                                isLoading = false,
                                accountName = dto.accountName,
                                amountInput = dto.amount,
                                pickedCategory =
                                    CategoryDto(
                                        categoryId = dto.categoryId,
                                        categoryName = dto.categoryName,
                                        emoji = dto.emoji,
                                        amount = dto.amount,
                                    ),
                                date = parseDate(dto.dateTime),
                                time = parseTime(dto.dateTime),
                                comment = dto.comment,
                                error = null,
                            )
                        }
                }
            }
        }
    }

    private fun deleteTransaction() {
        viewModelScope.launch {
            val st = _state.value
            deleteTransactionUseCase(_state.value.transactionId!!, st.isIncome).collect { result ->
                when (result) {
                    is Result.Loading -> _state.update { it.copy(isLoading = true, error = null) }
                    is Result.Error -> {
                        val msg = result.cause.toUserMessage()
                        _state.update { it.copy(isLoading = false, error = msg) }
                        _effect.emit(ShowSnackbar("Ошибка: $msg"))
                    }
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = null,
                            )
                        }
                        _effect.emit(Finish)
                    }
                }
            }
        }
    }

    private fun changeAmount(input: String) {
        val tooLong = input.length > 15
        val normalized = if (tooLong) null else input.validateBalance()
        val isValid = normalized != null

        val errText =
            when {
                input.isBlank() -> "Введите сумму"
                tooLong -> "Не более 13 символов до сотых"
                !isValid -> "Формат: 12345,67"
                else -> null
            }

        _state.update { st ->
            st.copy(
                amountInput = input,
                amountValid = isValid,
                amountError = errText,
            )
        }
    }

    private fun addTransaction(accountId: Int) {
        val st = _state.value
        viewModelScope.launch {
            addTransactionUseCase(
                accountId = accountId,
                categoryId = st.pickedCategory!!.categoryId,
                amount = st.amountInput,
                transactionDate = st.datetime,
                comment = st.comment,
                accountName = st.accountName,
                categoryName = st.pickedCategory.categoryName,
                emoji = st.pickedCategory.emoji
            ).collect { result ->
                when (result) {
                    is Result.Loading -> _state.update { it.copy(isLoading = true, error = null) }
                    is Result.Error -> {
                        val msg = result.cause.toUserMessage()
                        _state.update { it.copy(isLoading = false, error = msg) }
                        _effect.emit(ShowSnackbar("Ошибка: $msg"))
                        cancel()
                    }

                    is Result.Success -> {
                        _state.update { it.copy(isLoading = false) }
                        _effect.emit(Finish)
                        cancel()
                    }
                }
            }
        }
    }

    private fun updateTransaction(accountId: Int) {
        val st = _state.value
        viewModelScope.launch {
            updateTransactionUseCase(
                accountId = accountId,
                categoryId = st.pickedCategory!!.categoryId,
                amount = st.amountInput,
                transactionDate = st.datetime,
                comment = st.comment,
                isIncome = st.isIncome,
                transactionId = st.transactionId!!,
            ).collect { result ->
                when (result) {
                    is Result.Loading -> _state.update { it.copy(isLoading = true, error = null) }
                    is Result.Error -> {
                        val msg = result.cause.toUserMessage()
                        _state.update { it.copy(isLoading = false, error = msg) }
                        _effect.emit(ShowSnackbar("Ошибка: $msg"))
                        cancel()
                    }

                    is Result.Success -> {
                        _state.update { it.copy(isLoading = false) }
                        _effect.emit(Finish)
                        cancel()
                    }
                }
            }
        }
    }

    private fun finishEditing() {
        viewModelScope.launch {
            _effect.emit(Finish)
        }
    }
}
