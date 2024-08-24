package net

import kotlinx.serialization.json.Json
import model.DepartureBoard
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

private const val BASE_URL = "https://api1.raildata.org.uk/1010-live-departure-board-dep/LDBWS/api/20220120/"

private val client = OkHttpClient.Builder()
    .addInterceptor(AuthenticationInterceptor())
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .build()

private val json = Json {
    ignoreUnknownKeys = true
}

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface OpenLDBWSSpec {
    @GET("GetDepBoardWithDetails/{crs}")
    suspend fun getDepBoardWithDetails(@Path("crs") crs: String): DepartureBoard
}

object OpenLDBWS {
    val retrofitService: OpenLDBWSSpec by lazy {
        retrofit.create(OpenLDBWSSpec::class.java)
    }
}