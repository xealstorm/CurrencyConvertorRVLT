package com.android.challengervlt.util.picasso

import android.app.Application
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.io.File

object PicassoHelper {
    fun setupPicasso(app: Application) {
        Picasso.setSingletonInstance(
            Picasso.Builder(app)
                .downloader(
                    OkHttp3Downloader(
                        OkHttpClient.Builder()
                            .protocols(listOf(Protocol.HTTP_1_1))
                            .apply {
                                cache(
                                    Cache(
                                        File(app.cacheDir,
                                            CACHE_FILE_NAME
                                        ),
                                        CACHE_MAX_SIZE
                                    )
                                )

                            }
                            .build())
                )
                .indicatorsEnabled(false)
                .loggingEnabled(false)
                .build())
    }


    private const val CACHE_MAX_SIZE = 1024 * 1024 * 16L
    private const val CACHE_FILE_NAME = "picasso"
}