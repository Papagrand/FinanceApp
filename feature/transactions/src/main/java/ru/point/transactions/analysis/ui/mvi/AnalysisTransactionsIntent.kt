package ru.point.transactions.analysis.ui.mvi

internal sealed interface AnalysisTransactionsIntent {
    data object Load : AnalysisTransactionsIntent

    data object Retry : AnalysisTransactionsIntent

}
