package jianxiongrao.smileimitation.model

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
data class RhesisResult(val showapi_res_code:String,val showapi_res_error:String,
                        val  showapi_res_body:RhesisBody)
data class RhesisBody(val ret_code:String,val ret_message:String,
                      val data:List<Rhesis>)
data class Rhesis(val english:String,val chinese:String)