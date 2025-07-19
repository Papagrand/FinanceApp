package ru.point.transactions.analysis.domain

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.api.model.TransactionDto
import ru.point.api.repository.TransactionRepository
import ru.point.transactions.analysis.domain.model.AnalysisTransactions
import ru.point.transactions.analysis.domain.model.CategorySummary
import ru.point.utils.common.Result

internal class GetAnalysisTransactionsUseCase @Inject constructor(
    private val repo: TransactionRepository
) {
    operator fun invoke(
        accountId: Int,
        isIncome: Boolean,
        startDate: String,
        endDate: String
    ): Flow<Result<AnalysisTransactions>> {
        val formatter = DateTimeFormatter.ISO_DATE
        val isoStart = startDate.format(formatter)
        val isoEnd = endDate.format(formatter)

        return repo.observePeriod(accountId, isoStart, isoEnd)
            .map { result ->
                when (result) {
                    is Result.Loading -> Result.Loading
                    is Result.Error -> Result.Error(result.cause)
                    is Result.Success -> {
                        val transactionsByCategory: Map<Int, List<TransactionDto>> =
                            result.data
                                .asSequence()
                                .filter { transaction -> transaction.isIncome == isIncome }
                                .groupBy { transaction -> transaction.categoryId }

                        val grandTotalAmount: BigDecimal = transactionsByCategory
                            .values
                            .sumOf { transactionsInCategory ->
                                transactionsInCategory.sumOf { it.amount.toBigDecimal() }
                            }

                        val categorySummaries: List<CategorySummary> = transactionsByCategory
                            .map { (categoryId, categoryTransactions) ->
                                val totalAmountForCategory: BigDecimal =
                                    categoryTransactions.sumOf { it.amount.toBigDecimal() }

                                val percentageOfTotal: BigDecimal =
                                    if (grandTotalAmount.compareTo(BigDecimal.ZERO) == 0)
                                        BigDecimal.ZERO
                                    else
                                        totalAmountForCategory
                                            .multiply(BigDecimal(100))
                                            .divide(grandTotalAmount, 1, RoundingMode.HALF_UP)

                                CategorySummary(
                                    categoryId = categoryId,
                                    categoryName = categoryTransactions.first().categoryName,
                                    emoji = categoryTransactions.first().emoji,
                                    totalAmount = totalAmountForCategory,
                                    percentage = percentageOfTotal
                                )
                            }
                            .sortedByDescending { summary -> summary.totalAmount }

                        Result.Success(
                            AnalysisTransactions(
                                categorySummaries = categorySummaries,
                                grandTotalAmount = grandTotalAmount
                            )
                        )
                    }
                }
            }
    }
}