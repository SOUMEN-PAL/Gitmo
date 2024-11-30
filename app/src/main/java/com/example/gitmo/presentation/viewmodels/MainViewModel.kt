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
import com.example.gitmo.statesManagers.ContributorFetchingState
import com.example.gitmo.statesManagers.RepoDataState
import com.example.gitmo.statesManagers.RepoListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel (private val repository: Repository): ViewModel() {

    var currentQuery  = mutableStateOf("")
    var repoListSTate = MutableStateFlow<RepoListState>(RepoListState.Loading())
    var searchingState = mutableStateOf(false)
    var contributorSearching = mutableStateOf(false)

    private val _repoDataState = MutableStateFlow<RepoDataState>(RepoDataState.Loading())
    val repoDataState = _repoDataState.asStateFlow()

    private val _contributorFetchingState = MutableStateFlow<ContributorFetchingState>(ContributorFetchingState.UnTouched())
    val contributorFetchingState = _contributorFetchingState.asStateFlow()

    fun resetContributorState(){
        _contributorFetchingState.value = ContributorFetchingState.UnTouched()
    }

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

    fun formatNumber(number: Int): String {
        return when {
            number >= 1000000 -> String.format("%.1fM", number / 1000000.0)
            number >= 1000 -> String.format("%.1fK", number / 1000.0)
            else -> number.toString()
        }
    }


    fun resetListState(){
        repoListSTate.value = RepoListState.Loading()
    }

    fun getRepoDetails(ownerName : String , repoName : String){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                repository.getRepoDetails(ownerName, repoName)
                _repoDataState.value = RepoDataState.Success(repository.repoInfoState.value)
            }catch (e : Exception){
                _repoDataState.value = RepoDataState.Error(e.message.toString())
            }
        }
    }

    fun getContributorDetails(ownerName : String , repoName : String){
        _contributorFetchingState.value = ContributorFetchingState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getContributorData(ownerName , repoName)
                _contributorFetchingState.value = ContributorFetchingState.Success(repository.contributorData.value)

            }catch (
                e :Exception
            ){
                _contributorFetchingState.value = ContributorFetchingState.Error();
            }
        }
    }

}