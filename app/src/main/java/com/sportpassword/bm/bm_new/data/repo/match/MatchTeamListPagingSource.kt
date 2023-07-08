package com.sportpassword.bm.bm_new.data.repo.match

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sportpassword.bm.bm_new.data.dto.match.MatchTeamListDto

class MatchTeamListPagingSource(
    private val perPage: Int
) : PagingSource<Int, MatchTeamListDto.Row>() {

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    override fun getRefreshKey(state: PagingState<Int, MatchTeamListDto.Row>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchTeamListDto.Row> {
        val nextPageNumber = params.key ?: 1
        var nextKey: Int? = null
        val teamList = mutableListOf<MatchTeamListDto.Row>()
        listener?.getMatchTeamList(nextPageNumber, perPage) {
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
        suspend fun getMatchTeamList(
            nextPageNumber: Int,
            perPage: Int,
            callback: (MatchTeamListDto) -> Unit
        )
    }
}