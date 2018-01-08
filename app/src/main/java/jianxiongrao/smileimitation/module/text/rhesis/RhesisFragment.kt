package jianxiongrao.smileimitation.module.text.rhesis

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jianxiongrao.smileimitation.R
import jianxiongrao.smileimitation.model.Rhesis
import jianxiongrao.smileimitation.model.services.RhesisService
import jianxiongrao.smileimitation.module.showSnckbar
import kotlinx.android.synthetic.main.rhesis_fragment.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.properties.Delegates

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
class RhesisFragment : Fragment() {
    private var mData: MutableList<Rhesis> = ArrayList()
    private var mLoadding by Delegates.observable(true) {
        _, _, new ->
        mSwipeRefreshLayout.isRefreshing = new
    }
    private var mPage: Int = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.rhesis_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
        loadData()
    }

    fun initView() {
        //setColorSchemeColors 虽然最后调用的是它，但是此处不允许传递id参数
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    fun initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener {
            mPage = 1
            loadData()
        }
        mSwipeRefreshLayout.setOnTouchListener { view, motionEvent ->
            if (!mLoadding && !mRecyclerView.canScrollVertically(1)) {
                mPage++
                loadData()
            }
            false
        }
    }

    fun loadData() {
        mLoadding = true
        doAsync {
            val data = RhesisService.getData()
            uiThread {
                if (data == null) {
                    showSnckbar(view as ViewGroup, "加载失败")
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

    private fun initAdapter() {
            mRecyclerView.adapter = RhesisAdapter(mData)
    }
}