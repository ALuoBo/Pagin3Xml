package com.sunbio.pagin3xml

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * 第一个参数是分页类型
 * 第二个参数是数据类型（每一个item）
 */
class RepoPagingSource(private val gitHubService: GitHubService) : PagingSource<Int, Repo>() {
    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
       return  try {
           val page = params.key ?: 1 // 当前的页数
           val pageSize = params.loadSize // 每页包含多少数据
           val repoResponse = gitHubService.searchRepos(page,pageSize) // 从服务器获取当前页对应的数据
           val repoItems = repoResponse.items
           val prevKey = if (page>1) page-1 else null
           val nextKey = if (repoItems.isNotEmpty()) page+1 else null
           LoadResult.Page(repoItems,prevKey,nextKey)
       }catch (e:Exception){
           LoadResult.Error(e)
       }


    }
}