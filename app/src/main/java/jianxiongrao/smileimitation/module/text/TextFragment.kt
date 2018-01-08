package jianxiongrao.smileimitation.module.text

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jianxiongrao.smileimitation.R
import jianxiongrao.smileimitation.module.text.joke.JokeFragment
import jianxiongrao.smileimitation.module.text.rhesis.RhesisFragment
import kotlinx.android.synthetic.main.text_fragment.*

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/6
 * 此处需要注意 在主界面的appbarlayout中，需要经elevation置为0dp，否则没有衔接效果
 */
class TextFragment : Fragment() {
    val tabs: Array<String> = arrayOf("搞笑段子", "励志鸡汤")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.text_fragment, container, false)
    }

    //为了防止卡顿
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewPager.offscreenPageLimit = tabs.size
        mViewPager.adapter = TextPageAdapter(childFragmentManager)
        mTabLayout.setupWithViewPager(mViewPager)
    }
    private inner class TextPageAdapter(fm: FragmentManager):FragmentPagerAdapter(fm){
        override fun getItem(position: Int): Fragment {
            return when(position){
                0  ->  JokeFragment()
                1 ->  RhesisFragment()
                else ->  JokeFragment()
            }
        }

        override fun getCount(): Int {
            return tabs.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return tabs[position]
        }

    }
}