package com.android.challengervlt.network

import com.android.challengervlt.BuildConfig
import com.android.challengervlt.network.model.ItemsResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

class NetworkServiceImpl(
    retrofit: Retrofit
) : NetworkService {

    internal val api = retrofit.create<Api>(Api::class.java)

    override fun getRates(base: String): Single<ItemsResponse> {
        return api.getRates(base)
    }

    internal interface Api {
        @GET("latest")
        fun getRates(@Query("base") base: String): Single<ItemsResponse>
    }

}