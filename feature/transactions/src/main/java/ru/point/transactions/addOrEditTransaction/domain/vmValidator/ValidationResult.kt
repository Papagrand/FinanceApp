package ru.point.transactions.addOrEditTransaction.domain.vmValidator

data class ValidationResult(val isValid: Boolean, val message: String? = null)

object AddOrEditTransactionValidator {
    fun validate(
        accountId: Int?,
        categoryId: Int?,
        amountRaw: String,
        amountValid: Boolean,
        date: String,
        time: String,
        maxIntegerLength: Int = 13,
    ): ValidationResult {
        val errors = mutableListOf<String>()
        if (accountId == null) errors += "Счёт не определён"
        if (categoryId == null) errors += "Не выбрана статья"

        val sanitized = amountRaw.replace(",", ".")
        val intPart = sanitized.substringBefore('.', sanitized)
        if (amountRaw.isBlank()) {
            errors += "Введите сумму"
        } else if (intPart.length > maxIntegerLength) {
            errors += "Не более $maxIntegerLength символов до запятой"
        } else if (!amountValid) {
            errors += "Неверный формат суммы"
        }

        if (date.isBlank() || time.isBlank()) {
            errors += "Дата и/или время"
        }

        return if (errors.isEmpty()) {
            ValidationResult(true)
        } else {
            ValidationResult(
                isValid = false,
                message =
                    buildString {
                        append("Неверный формат данных\n")
                        errors.forEach { append("● $it\n") }
                        setLength(length - 1)
                    },
            )
        }
    }
}
