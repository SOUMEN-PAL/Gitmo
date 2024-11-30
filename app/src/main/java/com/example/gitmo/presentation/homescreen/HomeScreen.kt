package com.example.gitmo.presentation.homescreen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.gitmo.R
import com.example.gitmo.domain.models.searchedRepoDataModel.Item
import com.example.gitmo.presentation.navigation.Screens
import com.example.gitmo.presentation.viewmodels.MainViewModel
import com.example.gitmo.statesManagers.RepoListState

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: MainViewModel , navController: NavController) {
    val query = viewModel.currentQuery
    val focusRequester = remember {
        FocusRequester()
    }
    var isFocused by rememberSaveable { mutableStateOf(viewModel.searchingState.value) }
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        containerColor = colorResource(id = R.color.backgroundBlack),

        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 8.dp, top = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gitmo",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(8f),
                        value = query.value,
                        singleLine = true,
                        onValueChange = {
                            query.value = it
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colorResource(id = R.color.ascentColor),
                            unfocusedContainerColor = colorResource(id = R.color.ascentColor),
                            focusedTextColor = colorResource(id = R.color.white),
                            focusedBorderColor = colorResource(id = R.color.gitGreen),
                            cursorColor = colorResource(id = R.color.forkTabColor)
                        )
                    )

                    IconButton(
                        onClick = {

                            if (viewModel.searchingState.value) {
                                viewModel.searchingState.value = false
                                viewModel.currentQuery.value = ""
                                viewModel.resetListState()
                                focusManager.clearFocus()
                            } else {
                                if (query.value != "" && viewModel.searchingState.value == false) {
                                    viewModel.searchingState.value = true
                                    viewModel.getRepo(query.value)
                                }

                            }

                        },
                        modifier = Modifier.weight(2f),
                    ) {
                        if (viewModel.searchingState.value) {
                            Icon(
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = "Cancel",
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )

                        } else {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search",
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

    ) { itPadding ->
        Column(
            modifier = Modifier.padding(itPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val repoListState by viewModel.repoListSTate.collectAsStateWithLifecycle()

            when (repoListState) {
                is RepoListState.Error -> {
                    Text(text = "Error in fetching Data")
                }

                is RepoListState.Loading -> {

                }

                is RepoListState.Success -> {
                    val repoList =
                        (repoListState as RepoListState.Success).data.collectAsLazyPagingItems()
                    LazyColumn(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(repoList.itemCount) { index ->
                            val items = repoList[index]
                            if (items != null) {
                                RepoItem(item = items , onClick = {ownerName , repoName ->
                                    navController.navigate(Screens.RepositoryScreen.route + "/$ownerName/$repoName")
                                })
                            }
                        }

                        when {
                            repoList.loadState.refresh is LoadState.Loading -> {
                                item { CircularProgressIndicator(color = colorResource(id = R.color.gitGreen)) }
                            }

                            repoList.loadState.append is LoadState.Loading -> {
                                item { CircularProgressIndicator(color = colorResource(id = R.color.gitGreen)) }
                            }

                            repoList.loadState.refresh is LoadState.Error -> {
                                item { Text("Error loading data") }
                            }

                            repoList.loadState.append is LoadState.Error -> {
                                item {
                                    Button(onClick = { repoList.retry() }) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

//            if (viewModel.searchingState.value && query.value != "") {
//                val repoList = viewModel.getRepo(query.value).collectAsLazyPagingItems()
//                LazyColumn {
//                    items(repoList.itemCount) { index ->
//                        val items = repoList[index]
//                        if (items != null) {
//                            RepoItem(item = items)
//                        }
//                    }
//                }
//            }
    }
}


@Composable
fun RepoItem(item: Item , onClick : (ownerName: String , repoName : String) -> Unit) {

    val ownerName = if (item.owner != null && item.owner.login != null) {
        item.owner.login
    } else {
        "undefined"
    }

    val repoName = if (item.name != null) {
        item.name
    } else {
        "Undefined"
    }

    val repoDesc = if (item.description != null) {
        item.description
    } else {
        "No Description!!"
    }

    val forkCount = if (item.forks_count != null) {
        item.forks_count
    } else {
        0
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(ownerName , repoName)
            }
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.owner.avatar_url)
                    .transformations(CircleCropTransformation())
                    .build(),
                contentDescription = "Avatar URL",
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
            )

            Text(text = ownerName, fontSize = 16.sp, color = Color.White)
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = repoName,
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = repoDesc,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.StarRate,
                contentDescription = "star",
                tint = Color.Yellow
            )
            Text(text = formatNumber(forkCount), color = colorResource(id = R.color.forkTabColor))
        }

    }

    HorizontalDivider(
        color = colorResource(id = R.color.border)
    )
}

fun formatNumber(number: Int): String {
    return when {
        number >= 1000000 -> String.format("%.1fM", number / 1000000.0)
        number >= 1000 -> String.format("%.1fK", number / 1000.0)
        else -> number.toString()
    }
}