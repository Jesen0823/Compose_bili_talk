package com.jesen.retrofit_lib

import com.jesen.retrofit_lib.com.BASE_URL
import com.jesen.retrofit_lib.com.BOARDING_PASS
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RetrofitClient {

    private val instance: Retrofit by lazy {

        val logInterceptor = HttpLoggingInterceptor()


        //if (BuildConfig.DEBUG) {
        //显示日志
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        /*} else {
            logInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }*/

        // 请求头拦截器
        val headerInterceptor = Interceptor { chain ->
            val original: Request = chain.request()

            val requestBuilder: Request.Builder = original.newBuilder().apply {
                // 检测是否追加请求头
                if (checkPath(original.url.pathSegments)) {
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


        // https证书相关
        val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return emptyArray()
            }
        })
        try {
            val ssl = SSLContext.getInstance("SSL")
            ssl.init(null, trustManagers, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl(BASE_URL)
            //.addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private fun checkPath(pathSegments: List<String>): Boolean = pathSegments.run {
        contains("home") ||
                contains("ranking") ||
                contains("favorites") ||
                contains("detail") ||
                contains("like") ||
                contains("favorite") ||
                contains("notice") ||
                contains("profile")
    }


    fun <T> createApi(clazz: Class<T>): T {
        return instance.create(clazz) as T
    }
}