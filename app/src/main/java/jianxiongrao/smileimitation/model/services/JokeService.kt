package jianxiongrao.smileimitation.model.services

import com.google.gson.Gson
import jianxiongrao.smileimitation.commen.utils.Const
import jianxiongrao.smileimitation.model.Joke
import jianxiongrao.smileimitation.model.JokeResult
import java.net.URL

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
class JokeService {
    companion object {
        val baseUrl = "http://route.showapi.com/341-1"
        fun buildBaseUrl(page: Int, maxResult: Int): String {
            return Const.buildUrl("$baseUrl?page=$page&maxResult=$maxResult")
        }

        fun getData(page: Int, maxResult: Int = 10): List<Joke>? {
            var forecastJsonStr: String? = null
            try {
                    forecastJsonStr = URL(buildBaseUrl(page,maxResult)).readText()
            } catch (e: Exception) {
                    return null
            }
            val data = Gson().fromJson(forecastJsonStr,JokeResult::class.java)
            val jokes :List<Joke> = data.showapi_res_body.contentlist
            return if(jokes.isNotEmpty())jokes else null
        }
    }
}