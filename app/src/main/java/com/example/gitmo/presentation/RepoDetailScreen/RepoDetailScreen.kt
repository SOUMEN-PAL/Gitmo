package com.example.gitmo.presentation.RepoDetailScreen

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.gitmo.R
import com.example.gitmo.presentation.viewmodels.MainViewModel
import com.example.gitmo.statesManagers.ContributorFetchingState
import com.example.gitmo.statesManagers.RepoDataState

@Composable
fun RepoDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    ownerName: String,
    repoName: String
) {
    var isDataLoaded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isDataLoaded) { // Triggered only once after initial composition
        if (!isDataLoaded) {
            viewModel.getRepoDetails(ownerName, repoName)
            isDataLoaded = true
        }
    }
    var showWebView by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val dataState by viewModel.repoDataState.collectAsState()
    val contributorState by viewModel.contributorFetchingState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundBlack)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (dataState) {
            is RepoDataState.Error -> {
                Log.d("data_repo", "Error")
            }

            is RepoDataState.Loading -> {
                Log.d("data_repo", "Loading")
            }

            is RepoDataState.Success -> {
                val repoData = (dataState as RepoDataState.Success).data
                val repoDescription = repoData?.description ?: "No Description"
                val forkData = repoData?.forks_count ?: 0
                val avatarURL = repoData?.owner?.avatar_url
                val projectLink = repoData?.svn_url ?: "https://www.google.com/"

                Log.d("projectLink", projectLink)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .systemBarsPadding(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    item {


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(avatarURL)
                                    .transformations(CircleCropTransformation())
                                    .build(),
                                contentDescription = "Avatar URL",
                                modifier = Modifier
                                    .size(150.dp)
                                    .padding(end = 8.dp)
                            )

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = ownerName,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "fork",
                                        tint = Color.Yellow
                                    )
                                    Text(
                                        text = viewModel.formatNumber(forkData),
                                        color = Color.White
                                    )
                                }
                            }

                        }


                        Text(
                            text = repoName,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(12.dp),
                            color = Color.White
                        )

                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            modifier = Modifier
                                .fillMaxWidth(),
                            label = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Description",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = colorResource(id = R.color.forkTabColor),
                                disabledContainerColor = colorResource(id = R.color.ascentColor)
                            )

                        )

                        Spacer(modifier = modifier.height(12.dp))

                        Text(text = repoDescription, fontSize = 18.sp, color = Color.White)

                        Spacer(modifier = modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Project Link:",
                                textDecoration = TextDecoration.Underline,
                                color = Color.White,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .weight(4f)
                            )

                            Button(
                                onClick = {

                                    val url = projectLink
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)

                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.ascentColor)
                                ),
                                modifier = Modifier
                                    .border(
                                        width = 2.dp, // Adjust border width as needed
                                        color = colorResource(id = R.color.forkTabColor), // Adjust border color as needed
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .weight(6f)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Open Project",
                                    fontWeight = FontWeight.Thin,
                                    color = Color.White
                                )
                            }

                        }
                        
                        
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if(viewModel.contributorSearching.value){
                                    viewModel.resetContributorState()
                                    viewModel.contributorSearching.value = false
                                }
                                else{
                                    viewModel.contributorSearching.value = true
                                    viewModel.getContributorDetails(ownerName, repoName)
                                }


                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.ascentColor)
                            ),
                            modifier = Modifier
                                .border(
                                    width = 2.dp, // Adjust border width as needed
                                    color = colorResource(id = R.color.forkTabColor), // Adjust border color as needed
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .weight(6f)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Show Contributors",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = 24.sp
                            )
                        }
                    }

                    when(contributorState){
                        is ContributorFetchingState.Error -> {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Error in fetching Contributors")
                                }

                            }
                        }
                        is ContributorFetchingState.Loading -> {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(color = colorResource(id = R.color.gitGreen))
                                }
                            }
                        }
                        is ContributorFetchingState.Success -> {
                            val contributorsData = (contributorState as ContributorFetchingState.Success).data
                            if(contributorsData != null){
                                items(contributorsData.chunked(2)){dataItem->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        dataItem.forEach { item ->
                                            Column(
                                                modifier = Modifier.weight(1f),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                ContributorsDataScreen(data = item)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        is ContributorFetchingState.UnTouched -> {

                        }
                    }



                }

            }
        }
    }


}
