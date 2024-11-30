package com.example.gitmo.statesManagers

import com.example.gitmo.domain.models.searchedRepoDataModel.Item

sealed class RepoListState {
    class Loading() : RepoListState()
    class Error(val errorMessage: String) : RepoListState()
    class Success(val data : List<Item>?) : RepoListState()
}