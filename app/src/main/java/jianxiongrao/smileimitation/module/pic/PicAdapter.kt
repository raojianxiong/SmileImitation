package jianxiongrao.smileimitation.module.pic

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.GlideContext
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import jianxiongrao.smileimitation.R
import jianxiongrao.smileimitation.commen.utils.ScreenUtil
import jianxiongrao.smileimitation.model.HuaBan

import org.jetbrains.anko.find

/**
 * Created by yhao on 17-9-6.
 *
 */
class PicAdapter(var items: List<HuaBan>?) : RecyclerView.Adapter<PicAdapter.MyViewHolder>() {

    private var mHeights: MutableMap<Int, Int> = HashMap()


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(holder.imageView)
                .asBitmap()
                .load(items?.get(position)?.thumb)
                .transition(BitmapTransitionOptions().crossFade(800))
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Bitmap>?, p3: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(bitmap: Bitmap, p1: Any?, p2: Target<Bitmap>?, p3: DataSource?, p4: Boolean): Boolean {
                        val imageViewWidth = (ScreenUtil.w(holder.imageView.context)
                                - ScreenUtil.dp2px(holder.imageView.context,4.0F) * 4) / 2
                        val imageViewHeight: Int = ((imageViewWidth.toDouble() / bitmap.width) * bitmap.height).toInt()
                        mHeights.put(position, imageViewHeight)
                        holder.imageView.layoutParams.height = imageViewHeight
                        holder.imageView.layoutParams.width = imageViewWidth
                        return false
                    }
                })
                .into(holder.imageView)
        holder.imageView.setOnClickListener({
            if (mOnItemClickListener != null) {
                mOnItemClickListener!!.onClick(holder.imageView, items?.get(position)?.thumb!!)
            }

        })
        if (mHeights.containsKey(position)) {
            holder.imageView.layoutParams.height = mHeights[position]!!
        }
    }

    interface OnItemClickListener {
        fun onClick(view: View, url: String)
    }

    fun setOnItemCLick(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    var mOnItemClickListener: OnItemClickListener? = null

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder? {
        Log.i("rjx", "url2 " + items?.get(0)?.thumb)
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pic, parent, false))
    }

    class MyViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val imageView: ImageView = item.find(R.id.picture)
    }


}