package jianxiongrao.smileimitation.commen.download

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
class ProgressResponseBody(private val responseBody: ResponseBody, private val progressListener: ProgressListener?):ResponseBody() {
    private var bufferedSource:BufferedSource ? = null
    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if(bufferedSource == null){
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource!!
    }
    private fun source(source: Source):Source{
        return object : ForwardingSource(source){
            internal var totalBytesRead:Long = 0
            override fun read(sink: Buffer?, byteCount: Long): Long {
                val byteRead = super.read(sink, byteCount)
                totalBytesRead += if(byteRead != -1L) byteRead else 0
                progressListener?.onProgress(totalBytesRead,contentLength(),byteRead == -1L)
                return byteRead
            }
        }
    }
}