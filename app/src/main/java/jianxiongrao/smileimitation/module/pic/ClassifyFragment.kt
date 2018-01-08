package jianxiongrao.smileimitation.module.pic

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jianxiongrao.smileimitation.R
import jianxiongrao.smileimitation.commen.notNullSingleValue
import jianxiongrao.smileimitation.model.HuaBan
import jianxiongrao.smileimitation.model.services.HuaBanService
import jianxiongrao.smileimitation.module.BigPicActivity
import jianxiongrao.smileimitation.module.showSnckbar
import kotlinx.android.synthetic.main.classify_pic_fragment.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.properties.Delegates

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
class ClassifyFragment:Fragment() {
    //每个Fragment对应的type不变，都预加载好了
    private var mType :Int by notNullSingleValue()
    private var mPage:Int = 1
    private var mData :MutableList<HuaBan> = ArrayList()
    private var mLoading by Delegates.observable(true){
        _,_,new ->
        mSwipeRefreshLayout2.isRefreshing = new
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.classify_pic_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mType = arguments.getInt(EXTRA_TYPE)
        initView()
        initEvent()
        loadData()
    }
    fun initView(){
        mSwipeRefreshLayout2.setColorSchemeResources(R.color.colorPrimary)
        mRecyclerView2.setHasFixedSize(true)
        mRecyclerView2.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        //一定要写，为了保证每次刷新时候图片不会又调整摆放(边缘差异不做处理)
        (mRecyclerView2.layoutManager as StaggeredGridLayoutManager).gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
    }
    fun initEvent(){
        mSwipeRefreshLayout2.setOnRefreshListener {
            mPage = 1
            loadData()
        }
        mRecyclerView2.setOnTouchListener { _, _ ->
            if(!mLoading && !mRecyclerView2.canScrollVertically(1)){
                mPage ++
                loadData()
            }
            false
        }
    }
    private fun loadData(){
        mLoading = true
        doAsync {
            val data = HuaBanService.getData(mType,mPage)
            uiThread {
                mLoading = false
                if(data == null){
                    showSnckbar(view as ViewGroup,"加载失败")
                    return@uiThread
                }
                if(mRecyclerView2.adapter == null){
                    mData.addAll(data)
                    initAdapter()
                }else if(mPage > 1){
                    val pos = mData.size
                    mData.addAll(data)
                    mRecyclerView2.adapter.notifyItemRangeInserted(pos,data.size)
                }else {
                    mData.clear()
                    mData.addAll(data)
                    mRecyclerView2.adapter.notifyDataSetChanged()
                }
            }
        }
    }
    private fun initAdapter(){
        mRecyclerView2.adapter = PicAdapter(mData)
        (mRecyclerView2.adapter as PicAdapter).setOnItemCLick(object : PicAdapter.OnItemClickListener{
            override fun onClick(view: View, url: String) {
                BigPicActivity.launch(this@ClassifyFragment.activity as AppCompatActivity,view,url)
            }

        })
    }
    companion object{
        private val EXTRA_TYPE :String = "extra_type"
        //这样写目的其实除了切换横竖屏，就是优雅咯
        fun newInstance(type:Int):ClassifyFragment{
            val mFragment = ClassifyFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_TYPE,type)
            mFragment.arguments = bundle
            return mFragment
        }
    }
}