package com.example.gitmo.data.remote

import com.example.gitmo.domain.models.contributerModel.ContributerData
import com.example.gitmo.domain.models.searchedRepoDataModel.SearchedRepoData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/repositories")
    suspend fun getRepo(
        @Query("q") query : String,
        @Query("per_page") perPage : Int,
        @Query("page") page : Int
    ): SearchedRepoData

    @GET("repos/{owner}/{repo}/contributors")
    suspend fun getContributors(
        @Path("owner") owner: String,
        @Path("repo") repo : String
    ):Response<ContributerData>

}