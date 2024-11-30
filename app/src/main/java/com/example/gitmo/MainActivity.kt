package com.example.gitmo

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gitmo.domain.models.searchedRepoDataModel.Item
import com.example.gitmo.domain.repository.Repository
import com.example.gitmo.presentation.viewmodels.MainViewModel
import com.example.gitmo.presentation.viewmodels.MainViewModelFactory
import com.example.gitmo.statesManagers.RepoListState
import com.example.gitmo.ui.theme.GitmoTheme

class MainActivity : ComponentActivity() {
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val repository : Repository = (application as GitmoApplication).repository
            mainViewModel = viewModel(factory = MainViewModelFactory(repository))

            GitmoTheme {
                dataLog(viewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun dataLog(viewModel: MainViewModel){
    val repoListState by viewModel.repoListState.collectAsStateWithLifecycle()
    var repoList : List<Item>  = emptyList()

    LaunchedEffect(null) {
        viewModel.getRepoList()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(repoListState){
            is RepoListState.Error -> {

            }
            is RepoListState.Loading -> {

            }
            is RepoListState.Success -> {
                repoList = (repoListState as RepoListState.Success).data!!

                LazyColumn {
                    items(repoList){repoDetails->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = repoDetails.name)
                        }
                    }
                }

            }
        }


    }
}