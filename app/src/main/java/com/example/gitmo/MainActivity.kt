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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.gitmo.domain.models.searchedRepoDataModel.Item
import com.example.gitmo.domain.repository.Repository
import com.example.gitmo.presentation.homescreen.HomeScreen
import com.example.gitmo.presentation.viewmodels.MainViewModel
import com.example.gitmo.presentation.viewmodels.MainViewModelFactory
import com.example.gitmo.statesManagers.RepoListState
import com.example.gitmo.ui.theme.GitmoTheme
import kotlinx.coroutines.flow.Flow

class MainActivity : ComponentActivity() {
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val repository : Repository = (application as GitmoApplication).repository
            mainViewModel = viewModel(factory = MainViewModelFactory(repository))

            GitmoTheme {
                HomeScreen(viewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun RepoItem(item : Item){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = item.url)
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = item.name , color = Color.Red)
        }
    }
}

@Composable
fun dataLog(viewModel: MainViewModel){


//    val repoList = viewModel.getRepo("Java").collectAsLazyPagingItems()
//
//    LazyColumn {
//        // ... (Rest of your LazyColumn code) ...
//        if (repoList != null) {
//            items(repoList.itemCount) { index ->
//                val item = repoList[index]
//                if (item != null) {
//                    RepoItem(item = item)
//                } else {
//                    // Display placeholder or loading indicator
//                }
//            }
//        } else {
//            // Display initial loading indicator or empty state
//        }
//    }

    LaunchedEffect(null) {
        viewModel.getRepo("Python")
    }

    val repoListState by viewModel.repoListSTate.collectAsStateWithLifecycle()

    when(repoListState){
        is RepoListState.Error -> {

        }
        is RepoListState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        is RepoListState.Success -> {
            val repoList = (repoListState as RepoListState.Success).data.collectAsLazyPagingItems()
            LazyColumn {
                // ... (Rest of your LazyColumn code) ...
                if (repoList != null) {
                    items(repoList.itemCount) { index ->
                        val item = repoList[index]
                        if (item != null) {
                            RepoItem(item = item)
                        } else {
                            // Display placeholder or loading indicator
                        }
                    }
                } else {
                    // Display initial loading indicator or empty state
                }
            }
        }
    }

}


