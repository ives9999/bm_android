package com.sportpassword.bm.v2.arena.read

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sportpassword.bm.Utilities.PERPAGE
import com.sportpassword.bm.v2.base.ApiService2
import com.sportpassword.bm.v2.base.IRepository
import java.io.IOException

class ArenaPagingSource(private val apiService2: ApiService2): PagingSource<Int, PageArena>() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PageArena> {
        // Start refresh at position 1 if undefined
        val page = params.key ?: 1
        val offset = if (params.key != null) ((page - 1) * PERPAGE) + 1 else 1

        return try {
            val response = apiService2.getRead(page, PERPAGE)
            LoadResult.Page(
                data = response.data.rows.map {
                    val currentPage = response.data.meta.currentPage
                    PageArena(it.id, it.name, it.token, it.pv, it.created_at, it.images, it.zone, it.member, currentPage)
                },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.data.meta.totalPage == page) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PageArena>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}






















