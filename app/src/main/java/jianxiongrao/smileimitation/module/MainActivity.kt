package jianxiongrao.smileimitation.module

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import jianxiongrao.smileimitation.R
import jianxiongrao.smileimitation.commen.App
import jianxiongrao.smileimitation.commen.preference
import jianxiongrao.smileimitation.commen.utils.FileUtil
import jianxiongrao.smileimitation.commen.utils.ScreenUtil
import jianxiongrao.smileimitation.module.gif.GifFragment
import jianxiongrao.smileimitation.module.pic.PictureFragment
import jianxiongrao.smileimitation.module.text.TextFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import kotlin.properties.Delegates

fun showSnckbar(viewGroup: ViewGroup, text: String, duration: Int = 1000) {
    val snack = Snackbar.make(viewGroup, text, duration)
    snack.view.setBackgroundColor(ContextCompat.getColor(viewGroup.context, R.color.colorPrimary))
    snack.show()
}

class MainActivity : AppCompatActivity() {
    val mFragments: Array<Fragment> = arrayOf(TextFragment(), PictureFragment(), GifFragment())
    var mDefaultIndex: Int by preference(this@MainActivity, "sp_key_default_fragment", 0)

    var mCurrentIndex: Int by Delegates.observable(0) {
        _, _, new ->
        navigationView.setCheckedItem(when (new) {
            0 -> R.id.nav_text
            1 -> R.id.nav_pic
            2 -> R.id.nav_gif
            else -> R.id.nav_text
        })
    }
    var mBackPressedTime by Delegates.observable(0L) {
        _, old, new ->
        if (new - old > 1000) {
            showSnckbar(coordinatorLayout, getString(R.string.exit_message))
        }
        if (new - old in 1..1000) {
            //mDefaultIndex 设置了委托属性，下面这句话其实调用了set方法
            mDefaultIndex = mCurrentIndex
            finish()
        }
    }
    var mIsMenuOpen: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        init()
        ScreenUtil.setTransparentStatusBar(this)
    }

    private fun init() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_text -> switchFragment(0, item.title.toString(), item)
                R.id.nav_pic -> switchFragment(1, item.title.toString(), item)
                R.id.nav_gif -> switchFragment(2, item.title.toString(), item)
                R.id.nav_clear -> clearCache(item)
                R.id.nav_about -> {
                    val intent = Intent(this, AboutAppActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.in_from_right, R.anim.stay)
                    true
                }
                else -> false
            }
        }
        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerClosed(drawerView: View?) {
                navigationView.menu.findItem(R.id.nav_clear).title = "清理缓存"
                mIsMenuOpen = false
            }

            override fun onDrawerOpened(drawerView: View?) {
                mIsMenuOpen = true
                doAsync {
                    val glideCacheDir = Glide.getPhotoCacheDir(this@MainActivity) as File
                    var totalSize: Long = FileUtil.getFolderSize(glideCacheDir)
                    totalSize += FileUtil.getFolderSize(App.instance.cacheDir)
                    uiThread {
                        navigationView.menu.findItem(R.id.nav_clear).title =
                                "清理缓存${FileUtil.getPrintSize(totalSize)}"
                    }
                }
                //停止Gif的动画 防止你要清除动画，它还显示
                (mFragments[2] as GifFragment).pauseGif()

            }
        })
        mCurrentIndex = mDefaultIndex
        supportFragmentManager.beginTransaction().replace(R.id.content, mFragments[mCurrentIndex]).commit()
    }

    private fun clearCache(item: MenuItem): Boolean {
        item.title = "正在清理...."
        doAsync {
            val glideCacheDir = Glide.getPhotoCacheDir(this@MainActivity) as File
            var totalSize: Long = FileUtil.getFolderSize(glideCacheDir)
            totalSize += FileUtil.getFolderSize(App.instance.cacheDir)
            uiThread {
                if (totalSize == 0L) {
                    item.title = "清理完成"
                    return@uiThread
                }
                item.title = "正在清理${FileUtil.getPrintSize(totalSize)}...."
                doAsync {
                    FileUtil.deleteFloderFile(object : FileUtil.Companion.DeleteListener {
                        override fun onDone() {
                            uiThread {
                                item.title = "清理完成"
                            }
                        }

                        override fun onDelte(size: Long) {
                            uiThread {
                                totalSize -= size
                                item.title = "正在清理${FileUtil.getPrintSize(totalSize)}...."
                            }
                        }
                    }, glideCacheDir, App.instance.cacheDir)
                }
            }
        }
        return true
    }

    private fun switchFragment(index: Int, title: String, item: MenuItem): Boolean {
        if (index != mCurrentIndex) {
            val trx = supportFragmentManager.beginTransaction()
            trx.hide(mFragments[mCurrentIndex])
            if (!mFragments[index].isAdded) {
                trx.add(R.id.content, mFragments[index])
            }
            trx.show(mFragments[index]).commit()
            item.isChecked = true
            toolbar.title = title
            mCurrentIndex = index
        }
        drawerLayout.closeDrawers()
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mBackPressedTime = if (mIsMenuOpen) {
                drawerLayout.closeDrawers()
                //如果打开则关闭侧边栏并且值不改变，否则将触发委托
                mBackPressedTime
            } else {
                System.currentTimeMillis()
            }
        }

        //消耗
        return true
    }

    override fun onPause() {
        super.onPause()
        //停止动画
        (mFragments[2] as GifFragment).pauseGif()
    }
}
