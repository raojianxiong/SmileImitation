package jianxiongrao.smileimitation.module.text.joke

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import jianxiongrao.smileimitation.R
import jianxiongrao.smileimitation.model.Joke
import jianxiongrao.smileimitation.model.services.JokeService
import jianxiongrao.smileimitation.module.showSnckbar
import kotlinx.android.synthetic.main.joke_fragment.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.properties.Delegates

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
class JokeFragment : Fragment() {
    private var mData: MutableList<Joke> = ArrayList()
    private var mPage: Int = 1
    private var mLoading by Delegates.observable(true) {
        _, _, new ->
        mSwipeRefreshLayout.isRefreshing = new
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.joke_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
        loadData()
    }

    private fun initView() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener {
            mPage = 1
            loadData()
        }
        mRecyclerView.setOnTouchListener { view, motionEvent ->
            //意思是不处于加载状态，下拉到底部不能拉时加载数据 如果是-1则表示是顶部
            if (!mLoading && !mRecyclerView.canScrollVertically(1)) {
                mPage++
                loadData()
            }
            //不消耗，否则其他事件就不能执行了
            false
        }
    }

    private fun loadData() {
        mLoading = true
        doAsync {
            val data = JokeService.getData(mPage)
            uiThread {
                mLoading = false
                if (data == null) {
                    showSnckbar(view as ViewGroup, "加载失败")
                    return@uiThread
                }
                if (mRecyclerView.adapter == null) {
                    //向集合内添加数据
                    mData.addAll(data)
                    initAdapter()
                } else if (mPage > 1) {
                    val pos = mData.size
                    mData.addAll(data)
                    //局部更新
                    mRecyclerView.adapter.notifyItemRangeInserted(pos, data.size)
                } else {
                    // adapter != null and mPage = 1
                    mData.clear()
                    mData.addAll(data)
                    mRecyclerView.adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun initAdapter() {
        mRecyclerView.adapter = JokeAdapter(mData)

    }
}