package net

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        return chain.proceed(originalRequest.newBuilder()
            .header("x-apikey", System.getenv("RDM_APIKEY") ?: "")
            .build())
    }
}