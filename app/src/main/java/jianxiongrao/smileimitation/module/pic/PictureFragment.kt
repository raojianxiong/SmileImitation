package jianxiongrao.smileimitation.module.pic

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jianxiongrao.smileimitation.R
import kotlinx.android.synthetic.main.pic_fragment.*

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/6
 */
class PictureFragment : Fragment() {
    val tabs: Array<String> = arrayOf("大胸妹", "小清新", "文艺范", "性感妹", "大长腿", "黑丝袜", "小翘臀")
    val types: Array<Int> = arrayOf(34, 35, 36, 37, 38, 39, 40)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pic_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewPager.offscreenPageLimit = tabs.size
        //注意需要用子Fragment来处理
        mViewPager.adapter = PicPageAdapter(childFragmentManager)
        mTabLayout.setupWithViewPager(mViewPager)
    }

    private inner class PicPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
           return ClassifyFragment.newInstance(types[position])
        }

        override fun getCount(): Int {
            return tabs.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return tabs[position]
        }

    }
}