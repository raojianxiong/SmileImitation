package jianxiongrao.smileimitation.module.gif

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jianxiongrao.smileimitation.R
import jianxiongrao.smileimitation.model.Gif
import jianxiongrao.smileimitation.model.services.GifService
import jianxiongrao.smileimitation.module.showSnckbar
import kotlinx.android.synthetic.main.gif_fragment.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.properties.Delegates

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/6
 */
class GifFragment:Fragment() {
    private var mData:MutableList<Gif> = ArrayList()
    private var mPage:Int = 1
    private var mLoading by Delegates.observable(true){
        _,_,new ->
        mSwipeRefreshLayout.isRefreshing = new
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.gif_fragment,container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
        loadData()
    }
    private fun initView(){
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
    }
    private fun initEvent(){
        mSwipeRefreshLayout.setOnRefreshListener {
            mPage = 1
            loadData()
        }
        mRecyclerView.setOnTouchListener { _, _ ->
            if(!mLoading && !mRecyclerView.canScrollVertically(1)){
                mPage ++
                loadData()
            }
            false
        }

    }
    private fun loadData(){
        mLoading = true
        doAsync {
            val data = GifService.getData(mPage)
            uiThread {
                mLoading = false
                if(data == null){
                    showSnckbar(view as ViewGroup,"加载失败")
                    return@uiThread
                }
                if(mRecyclerView.adapter == null){
                    mData.addAll(data)
                    initAdapter()
                }else if(mPage > 1){
                    val pos = mData.size
                    mData.addAll(data)
                    mRecyclerView.adapter.notifyItemRangeInserted(pos,data.size)
                }else{
                    mData.clear()
                    mData.addAll(data)
                    mRecyclerView.adapter.notifyDataSetChanged()
                }
            }
        }
    }
     fun initAdapter(){
        mRecyclerView.adapter = GifAdapter(mData,mRecyclerView)
    }
    fun pauseGif(){
        if(mRecyclerView != null && mRecyclerView.adapter != null){
            (mRecyclerView.adapter as GifAdapter).pauseGif()
        }
    }
}