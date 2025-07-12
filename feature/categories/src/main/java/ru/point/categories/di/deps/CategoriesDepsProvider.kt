package ru.point.categories.di.deps

interface CategoriesDepsProvider {
    val categoriesDeps: CategoriesDeps

    companion object : CategoriesDepsProvider by CategoriesDepsStore
}
