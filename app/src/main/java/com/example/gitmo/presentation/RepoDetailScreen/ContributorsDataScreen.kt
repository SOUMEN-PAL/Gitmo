package com.example.gitmo.presentation.RepoDetailScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.gitmo.domain.models.contributerModel.ContributerDataItem


@Composable
fun ContributorsDataScreen(modifier: Modifier = Modifier , data : ContributerDataItem) {
    val avatarURL = data.avatar_url ?: ""
    val name = data.login ?: ""

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(avatarURL)
                .transformations(CircleCropTransformation())
                .build(),
            contentDescription = "Avatar URL",
            modifier = Modifier
                .size(90.dp)
                .padding(end = 8.dp)
        )

        Text(text = name, fontSize = 12.sp , color = Color.White)

    }
}