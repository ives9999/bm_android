package com.sportpassword.bm.v2.arena.read

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sportpassword.bm.Utilities.PERPAGE
import com.sportpassword.bm.v2.base.IRepository

class ArenaPagingSource(private val repository: IRepository<ReadDao>): PagingSource<Int, ReadDao>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReadDao> {
        // Start refresh at position 1 if undefined
        val position = params.key ?: 1
        val offset = if (params.key != null) ((position - 1) * PERPAGE) + 1 else 1

        repository.getRead(position, PERPAGE, null, object : IRepository.IDaoCallback<ReadDao> {

            override fun onSuccess(res: ReadDao) {

//                if (res.status != 200) {
//                    error.getNetworkNotExist()
//                    isShowError.postValue(true)
//                } else {
//                    this@ViewModel.readDao.postValue(res)
//                }
//                isEmpty.postValue(!(res.status == 200 && res.data.rows.isNotEmpty()))
//                isShowLoading.postValue(false)
            }

            override fun onFailure(status: Int, msg: String) {
                //LoadResult.Error()

//                error.id = status
//                error.msg = msg
//                isShowError.postValue(true)
//                isShowLoading.postValue(false)
            }
        })
    }

    override fun getRefreshKey(state: PagingState<Int, ReadDao>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}






















