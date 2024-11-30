package com.example.gitmo.statesManagers

import androidx.paging.PagingData
import com.example.gitmo.domain.models.repositoryDataModel.RepositoryDataModel
import com.example.gitmo.domain.models.searchedRepoDataModel.Item
import kotlinx.coroutines.flow.Flow

sealed class RepoDataState {
    class Loading() : RepoDataState()
    class Error(val errorMessage: String) : RepoDataState()
    class Success(val data : RepositoryDataModel?) : RepoDataState()
}