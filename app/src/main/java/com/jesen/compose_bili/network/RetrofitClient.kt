package com.jesen.compose_bili.network

import com.jesen.compose_bili.BuildConfig
import com.jesen.compose_bili.utils.BOARDING_PASS
import com.jesen.compose_bili.utils.oLog
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val instance: Retrofit by lazy {

        val logInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            //显示日志
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        // 请求头拦截器
        val headerInterceptor = Interceptor { chain ->
            val original: Request = chain.request()

            val requestBuilder: Request.Builder = original.newBuilder().apply {
                oLog("headerInterceptor:  ${original.url.pathSegments}")
                if (original.url.pathSegments.contains("home")) {
                    header(BOARDING_PASS, "E793ED7A61088AAA70DD32614448F2C4AF")
                }
            }
                .header("content-type", "application/json")
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }

        // 两个拦截器有先后顺序
        val okhttpClient = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(logInterceptor)
            .connectTimeout(5, TimeUnit.SECONDS)//设置超时时间
            .retryOnConnectionFailure(true).build()

        Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl("https://api.devio.org")
            //.addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }


    fun <T> createApi(clazz: Class<T>): T {
        return instance.create(clazz) as T
    }
}