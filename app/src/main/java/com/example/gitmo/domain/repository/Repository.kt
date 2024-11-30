package com.example.gitmo.domain.repository

import com.example.gitmo.data.remote.ApiService
import com.example.gitmo.domain.models.repositoryDataModel.RepositoryDataModel
import com.example.gitmo.domain.models.searchedRepoDataModel.Item
import com.example.gitmo.domain.models.searchedRepoDataModel.SearchedRepoData
import com.example.gitmo.statesManagers.RepoDataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Repository(private val service: ApiService) {

    private val _repoInfoState = MutableStateFlow<RepositoryDataModel?>(null)

    val repoInfoState = _repoInfoState.asStateFlow()


    suspend fun getRepo(query: String, perPage: Int, page: Int): SearchedRepoData {
        delay(1000L)
        val response = service.getRepo(query, perPage, page)
        return response
    }

    suspend fun getRepoDetails(ownerName: String, repoName: String) {

        val response = service.getRepoDetails(owner = ownerName, repo = repoName)
        if (response.isSuccessful) {
            val data = response.body()
            _repoInfoState.value = data
        }


    }

}