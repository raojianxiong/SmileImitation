package jianxiongrao.smileimitation.model

import com.google.gson.JsonObject

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
data class HuaBanResult(val showapi_res_code:String,val showapi_res_error:String,
                        val showapi_res_body:JsonObject)
data  class HuaBan(val title:String,val thumb:String,val url:String)