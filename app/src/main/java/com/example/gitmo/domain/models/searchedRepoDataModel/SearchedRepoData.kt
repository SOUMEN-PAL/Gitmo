package com.example.gitmo.domain.models.searchedRepoDataModel

data class SearchedRepoData(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)