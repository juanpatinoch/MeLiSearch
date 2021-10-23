package com.mercadolibre.search.model.repository.search.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mercadolibre.search.model.dto.response.CustomResponse
import com.mercadolibre.search.model.dto.search.SearchDto
import com.mercadolibre.search.model.remote.search.SearchDataSource
import com.mercadolibre.search.utils.Constants
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSource(
    private val siteId: String,
    private val query: String,
    private val searchDataSource: SearchDataSource,
) : PagingSource<Int, SearchDto>() {

    override fun getRefreshKey(state: PagingState<Int, SearchDto>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchDto> {
        return try {
            val pageNumber = params.key ?: 1
            val response = searchDataSource.searchByQuery(
                siteId,
                query,
                Constants.pagingPageSize,
                pageNumber * Constants.pagingPageSize
            )
            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
            when (response) {
                is CustomResponse.Failure -> {
                    throw response.exception
                }
                is CustomResponse.Success -> {
                    val searchDtoList = mutableListOf<SearchDto>()
                    for (item in response.data.results) {
                        searchDtoList.add(
                            SearchDto(
                                id = item.id,
                                title = item.title,
                                price = item.price
                            )
                        )
                    }
                    val nextKey = if (searchDtoList.isNotEmpty()) pageNumber + 1 else null
                    LoadResult.Page(
                        data = searchDtoList,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
            }

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}