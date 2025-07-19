package ru.point.transactions.analysis.ui.mvi

internal sealed interface AnalysisTransactionsEffect {
    data class ShowSnackbar(val message: String) : AnalysisTransactionsEffect

    data object Finish : AnalysisTransactionsEffect
}
