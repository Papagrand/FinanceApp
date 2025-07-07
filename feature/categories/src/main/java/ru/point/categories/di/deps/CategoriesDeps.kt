package ru.point.categories.di.deps

import ru.point.api.flow.AccountPreferencesRepo
import ru.point.api.repository.CategoryRepository

interface CategoriesDeps {
    val categoryRepository: CategoryRepository
    val accountPreferencesRepo: AccountPreferencesRepo
}
