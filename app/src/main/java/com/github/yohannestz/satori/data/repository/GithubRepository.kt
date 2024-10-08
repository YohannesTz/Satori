package com.github.yohannestz.satori.data.repository

import com.github.yohannestz.satori.data.remote.service.GithubApi

class GithubRepository(
    private val githubApi: GithubApi
) {
    suspend fun getContributors(repoSlug: String) = githubApi.getContributors(repoSlug)
}