package com.example.gitmo.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gitmo.data.source.RepoDataSource
import com.example.gitmo.domain.models.searchedRepoDataModel.Item
import com.example.gitmo.domain.repository.Repository
import com.example.gitmo.statesManagers.RepoListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel (private val repository: Repository): ViewModel() {

    var currentQuery  = mutableStateOf("Android")
    var repoListSTate = MutableStateFlow<RepoListState>(RepoListState.Loading())



    fun getRepo(query: String): Flow<PagingData<Item>> {
        currentQuery.value = query
        val repoPager = Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            )
        ) {
            RepoDataSource(repository, currentQuery.value)
        }.flow.cachedIn(viewModelScope)

        repoListSTate.value = RepoListState.Success(repoPager)


        return repoPager

    }

}