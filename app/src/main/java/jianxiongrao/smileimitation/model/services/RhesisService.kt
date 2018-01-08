package jianxiongrao.smileimitation.model.services

import com.google.gson.Gson
import jianxiongrao.smileimitation.commen.utils.Const
import jianxiongrao.smileimitation.model.Rhesis
import jianxiongrao.smileimitation.model.RhesisResult
import java.net.URL

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
class RhesisService {
    companion object{
        val baseUrl = "http://route.showapi.com/1211-1"
        fun buildBaseUrl(count:Int):String{
            return Const.buildUrl("$baseUrl?count=$count")
        }
        fun getData(count: Int = 10):List<Rhesis>?{
            var forecastJsonString:String?= null
            try {
                forecastJsonString = URL(buildBaseUrl(count)).readText()
            }catch (e:Exception){
                return null
            }
            val data = Gson().fromJson(forecastJsonString,RhesisResult::class.java)
            val texts:List<Rhesis> = data.showapi_res_body.data
            return if(texts.isNotEmpty()) texts else null
        }
    }

}