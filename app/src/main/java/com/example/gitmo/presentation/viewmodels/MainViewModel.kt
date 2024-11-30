package com.example.gitmo.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitmo.domain.repository.Repository
import com.example.gitmo.statesManagers.RepoListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel (private val repository: Repository): ViewModel() {
    private val _repoListState = MutableStateFlow<RepoListState>(RepoListState.Loading())

    val repoListState = _repoListState.asStateFlow()

    fun getRepoList(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getRepo()
                _repoListState.value =
                    RepoListState.Success(repository.repoList.value)
            }catch (e : Exception){
                _repoListState.value = RepoListState.Error("Unable To fetch Data")
            }
        }
    }

}