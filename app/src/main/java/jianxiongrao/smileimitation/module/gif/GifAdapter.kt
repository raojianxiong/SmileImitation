package jianxiongrao.smileimitation.module.gif

import android.graphics.Bitmap
import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.github.lzyzsd.circleprogress.DonutProgress
import jianxiongrao.smileimitation.R
import jianxiongrao.smileimitation.commen.download.ProgressDownload
import jianxiongrao.smileimitation.commen.download.ProgressListener
import jianxiongrao.smileimitation.commen.utils.ScreenUtil
import jianxiongrao.smileimitation.model.Gif
import jianxiongrao.smileimitation.module.text.rhesis.RhesisAdapter
import kotlinx.android.synthetic.main.item_gif.*
import org.jetbrains.anko.find
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
class GifAdapter(var items: List<Gif>?, val recyclerView: RecyclerView) : RecyclerView.Adapter<GifAdapter.MyViewHolder>() {
    var mHeights: MutableMap<Int, Int> = HashMap()
    //磁盘缓存
    val options: RequestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    var gifDrawable: GifDrawable? = null

    /**
     * 在清理缓存时，需要停止动画
     */
    fun pauseGif() {
        if (gifDrawable != null) {
            gifDrawable!!.pause()
        }
    }

    init {
        //滚动的时候停止动画
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                pauseGif()
            }
        })
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(holder.gifImageView)
                .asBitmap()
                .load(items?.get(position)?.img)
                .transition(BitmapTransitionOptions().crossFade(800))
                .apply(options)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        val width: Int = (ScreenUtil.w(holder.gifImageView.context) - ScreenUtil.dp2px(holder.gifImageView.context, 5F) * 2).toInt()
                        val height: Int = ((width.toDouble() / resource.width) * resource.height).toInt()
                        mHeights.put(position, height)
                        holder.gifImageView.layoutParams.height = height
                        holder.gifImageView.layoutParams.width = width
                        holder.textView.visibility = View.VISIBLE
                        holder.textView.text = items?.get(position)?.title
                        return false
                    }
                }).into(holder.gifImageView)
        //有缓存，从缓冲拿就不请求网络了
        if (mHeights.containsKey(position)) {
            holder.gifImageView.layoutParams.height = mHeights[position]!!
            holder.textView.text = items?.get(position)?.title
        }
        //当前可以用第三方库，vitmito 带进度条自动播放
        holder.gifImageView.setOnClickListener {
            pauseGif()
            ProgressDownload.downLoadPhoto(items?.get(position)?.img!!, object : ProgressListener {
                override fun onProgress(readByte: Long, totalByte: Long, done: Boolean) {
                    holder.progressBar.post {
                        holder.progressBar.visibility = View.VISIBLE
                        holder.progressBar.progress = (readByte.toFloat()/totalByte)*100
                    }
                }

                override fun onSave(filePath: String) {
                    gifDrawable = GifDrawable(filePath)
                    holder.gifImageView.post {
                        holder.progressBar.visibility = View.GONE
                        holder.gifImageView.setImageDrawable(gifDrawable)
                    }
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_gif, parent, false))
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    class MyViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val gifImageView: GifImageView = item.find(R.id.gifImageView)
        val progressBar: DonutProgress = item.find(R.id.progressBar)
        val textView: TextView = item.find(R.id.textView)
    }

}