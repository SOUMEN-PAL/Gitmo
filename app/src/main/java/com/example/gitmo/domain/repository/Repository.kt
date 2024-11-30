package com.example.gitmo.domain.repository

import com.example.gitmo.data.remote.ApiService
import com.example.gitmo.domain.models.searchedRepoDataModel.Item
import com.example.gitmo.domain.models.searchedRepoDataModel.SearchedRepoData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Repository(private val service: ApiService) {
    private val _repoList = MutableStateFlow<List<Item>?>(null)


    val repoList = _repoList.asStateFlow()



    suspend fun getRepo(query : String , perPage : Int , page : Int) : SearchedRepoData {
        delay(1000L)
        val response = service.getRepo(query , perPage, page)
        return response
    }

}