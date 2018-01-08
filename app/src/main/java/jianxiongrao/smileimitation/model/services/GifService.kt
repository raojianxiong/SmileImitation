package jianxiongrao.smileimitation.model.services

import com.google.gson.Gson
import jianxiongrao.smileimitation.commen.utils.Const
import jianxiongrao.smileimitation.model.Gif
import jianxiongrao.smileimitation.model.GifResult
import java.net.URL

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
class GifService {
    companion object{
        val baseUrl = "http://route.showapi.com/341-3"
        fun buildBaseUrl(page: Int, maxResult: Int): String {
            return Const.buildUrl("$baseUrl?page=$page&maxResult=$maxResult")
        }
        fun getData(page:Int,maxResult: Int = 5):List<Gif>?{
            var forecastJsonStr:String? = null
            try {
                forecastJsonStr = URL(buildBaseUrl(page,maxResult)).readText()
            }catch (e:Exception){
                return null
            }
            val data = Gson().fromJson(forecastJsonStr, GifResult::class.java)
            val gifs: List<Gif> = data.showapi_res_body.contentlist
            return if (gifs.isNotEmpty()) gifs else null
        }
    }
}