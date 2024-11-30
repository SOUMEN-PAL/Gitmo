package com.example.gitmo

import android.app.Application
import com.example.gitmo.data.remote.ApiService
import com.example.gitmo.data.remote.RetrofitHelper
import com.example.gitmo.domain.repository.Repository

class GitmoApplication:Application() {

    lateinit var repository : Repository

    override fun onCreate() {
        initialize()
        super.onCreate()
    }

    fun initialize(){
        val service : ApiService = RetrofitHelper.getInstance().create(ApiService::class.java)
        repository = Repository(service)
    }
}