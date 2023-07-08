package com.sportpassword.bm.bm_new.data.repo.match

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sportpassword.bm.bm_new.data.dto.match.MatchListDto
import timber.log.Timber

class MatchPagingSource(
    private val perPage: Int
) : PagingSource<Int, MatchListDto.Row>() {

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    override fun getRefreshKey(state: PagingState<Int, MatchListDto.Row>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchListDto.Row> {
        val nextPageNumber = params.key ?: 1
        var nextKey: Int? = null
        val teamList = mutableListOf<MatchListDto.Row>()
        Timber.d("paging source $nextPageNumber")
        listener?.getMatchList(nextPageNumber, perPage) {
            it.rows.let { data -> teamList.addAll(data) }
            nextKey = if (it.rows.isNotEmpty()) nextPageNumber + 1 else null
        }
        return LoadResult.Page(
            data = teamList,
            prevKey = null,
            nextKey = nextKey
        )
    }

    interface Listener {
        suspend fun getMatchList(nextPageNumber: Int, perPage: Int, callback: (MatchListDto) -> Unit)
    }
}