package jianxiongrao.smileimitation.commen.download

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
interface ProgressListener {
    fun  onProgress(readByte:Long,totalByte:Long, done: Boolean)
    fun onSave(filePath:String)
}