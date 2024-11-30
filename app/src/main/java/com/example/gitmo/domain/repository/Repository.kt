package com.example.gitmo.domain.repository

import com.example.gitmo.data.remote.ApiService
import com.example.gitmo.domain.models.searchedRepoDataModel.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Repository(private val service: ApiService) {
    private val _repoList = MutableStateFlow<List<Item>?>(null)


    val repoList = _repoList.asStateFlow()



    suspend fun getRepo(){
        val response = service.getRepo("Android" , 10, 1)
        if(response.isSuccessful){
            _repoList.value = response.body()?.items
        }
    }

}