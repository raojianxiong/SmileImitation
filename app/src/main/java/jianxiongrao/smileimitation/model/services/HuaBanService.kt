package jianxiongrao.smileimitation.model.services

import android.util.Log
import com.google.gson.Gson
import jianxiongrao.smileimitation.commen.utils.Const
import jianxiongrao.smileimitation.model.HuaBan
import jianxiongrao.smileimitation.model.HuaBanResult
import java.net.URL

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
class HuaBanService {
    companion object{
        val baseUrl = "http://route.showapi.com/819-1"
        fun buildBaseUrl(type: Int, page: Int, num: Int): String {
            return Const.buildUrl("$baseUrl?type=$type&num=$num&page=$page")
        }
        fun getData(type: Int, page: Int, num: Int = 20):MutableList<HuaBan>?{
            var forecastJsonStr: String? = null
            try {
                forecastJsonStr = URL(buildBaseUrl(type, page, num)).readText()
            } catch (e: Exception) {
                return null
            }
            val data = Gson().fromJson(forecastJsonStr,HuaBanResult::class.java)
            val iterator = data.showapi_res_body.entrySet().iterator()
            val hubans:MutableList<HuaBan> = ArrayList()
            while (iterator.hasNext()){
                val element = iterator.next()
                try{
                    val huaban = Gson().fromJson(element.value,HuaBan::class.java)
                    hubans.add(huaban)
                }catch (e:Exception){
                }
            }
            return  if(hubans.size > 0) hubans else null
        }
    }
}