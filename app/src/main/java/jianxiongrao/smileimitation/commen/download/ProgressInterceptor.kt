package jianxiongrao.smileimitation.commen.download

import okhttp3.Interceptor
import okhttp3.Response

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
class ProgressInterceptor(private val processListener: ProgressListener):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originResponse = chain.proceed(chain.request())
        return originResponse.newBuilder().body(ProgressResponseBody(originResponse.body()!!,processListener)).build()
    }
}