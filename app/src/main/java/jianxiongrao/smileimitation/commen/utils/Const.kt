package jianxiongrao.smileimitation.commen.utils

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
class Const {
    companion object{
        private val yiyuan_appid = "45578"
        private val yiyuan_secret="4e6e7a13e16a42059a75a9a8931a779f"
        private val yiyuanAuth = "&showapi_sign=$yiyuan_secret&showapi_appid=$yiyuan_appid"
        fun buildUrl(url:String):String{
            return "$url$yiyuanAuth"
        }
    }
}