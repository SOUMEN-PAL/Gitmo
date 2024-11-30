package com.example.gitmo.statesManagers

import androidx.paging.PagingData
import com.example.gitmo.domain.models.searchedRepoDataModel.Item
import kotlinx.coroutines.flow.Flow

sealed class RepoListState {
    class Loading() : RepoListState()
    class Error(val errorMessage: String) : RepoListState()
    class Success(val data : Flow<PagingData<Item>>) : RepoListState()
}