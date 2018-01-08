package jianxiongrao.smileimitation.commen.utils

import java.security.MessageDigest

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
object MD5Util {
    /**
     * 对url或path进行MD5编码，避免特殊字符导致非法命名文件
     */
    fun getHashKey(key: String): String {
        var cacheKey: String
        try {
            val mDigest = MessageDigest.getInstance("MD5")
            //使用指定的字节数组更新摘要
            mDigest.update(key.toByteArray())
            cacheKey = byteToHexString(mDigest.digest())
        } catch (e: Exception) {
            cacheKey = key.hashCode().toString()
        }
        return cacheKey
    }
    private fun byteToHexString(bytes: ByteArray): String{
        val sb = StringBuilder()
        for(i in bytes.indices){
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if(hex.length == 1){
                sb.append(0)
            }
            sb.append(hex)
        }
        return sb.toString()
    }
}