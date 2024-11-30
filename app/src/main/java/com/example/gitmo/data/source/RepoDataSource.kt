package com.example.gitmo.data.source

import androidx.compose.runtime.MutableState
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitmo.domain.models.searchedRepoDataModel.Item
import com.example.gitmo.domain.repository.Repository

class RepoDataSource(private val repo : Repository , private val query : String) : PagingSource<Int, Item>() {
    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let {position->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        return try {
            val page = params.key ?: 1
            val perPage = params.loadSize
            val response = repo.getRepo(query = query , perPage , page)

            LoadResult.Page(
                data = response.items,
                prevKey = if(page == 1) null else page - 1,
                nextKey = if(response.items.isEmpty()) null else page + 1
            )

        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }

}