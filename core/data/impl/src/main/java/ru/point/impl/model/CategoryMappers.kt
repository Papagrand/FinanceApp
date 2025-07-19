package ru.point.impl.model

import ru.point.api.model.AllCategoriesDto
import ru.point.api.model.CategoryDto
import ru.point.local.entities.AllCategoryEntity
import ru.point.local.entities.MyCategoryEntity

fun CategoryStat.toMyEntity(): MyCategoryEntity =
    MyCategoryEntity(
        categoryId = categoryId,
        categoryName = categoryName,
        emoji = emoji,
        amount = amount
    )

fun AllCategories.toAllEntity(): AllCategoryEntity =
    AllCategoryEntity(
        categoryId = categoryId,
        categoryName = categoryName,
        emoji = emoji,
        isIncome = isIncome
    )


fun MyCategoryEntity.toCategoryDto(): CategoryDto =
    CategoryDto(
        categoryId = categoryId,
        categoryName = categoryName,
        emoji = emoji,
        amount = amount
    )

fun AllCategoryEntity.toAllCategoriesDto(): AllCategoriesDto =
    AllCategoriesDto(
        categoryId = categoryId,
        categoryName = categoryName,
        emoji = emoji,
        isIncome = isIncome
    )