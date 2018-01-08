package jianxiongrao.smileimitation.commen.download

import jianxiongrao.smileimitation.commen.App
import jianxiongrao.smileimitation.commen.utils.MD5Util
import okhttp3.*
import okio.Okio
import java.io.File
import java.io.IOException


/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
object ProgressDownload {
    private var progressListener:ProgressListener?=null
    private val mClient:OkHttpClient by lazy{
        OkHttpClient.Builder().addNetworkInterceptor(ProgressInterceptor(listener)).build()
    }
    //这里可以不新建此listener，共用一个即可
    private val listener:ProgressListener = object : ProgressListener {
        override fun onProgress(readByte: Long, totalByte: Long, done: Boolean) {
            if(progressListener != null){
                progressListener!!.onProgress(readByte,totalByte,done)
            }
        }

        override fun onSave(filePath: String) {
        }
    }

    fun downLoadPhoto(url:String,progressListener: ProgressListener){
        val existFilePath:String? = exist(url)
        if(existFilePath != null){
            progressListener.onSave(existFilePath)
            return
        }
        //文件存在，保存即可，不存在就得新建保存了
        this.progressListener = progressListener
        val request = Request.Builder().url(url).build()
        mClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {

            }

            override fun onResponse(call: Call?, response: Response) {
                val file = File(App.instance.cacheDir,MD5Util.getHashKey(url))
                val sink = Okio.buffer(Okio.sink(file))
                val source = response.body()!!.source()
                //存起来
                sink.writeAll(source)
                sink.flush()
                progressListener.onSave(file.absolutePath)
            }
        })
    }

    private fun exist(url: String): String? {
        val file = File(App.instance.cacheDir,MD5Util.getHashKey(url))
        return if(file.exists()) file.absolutePath else null
    }

}