package jianxiongrao.smileimitation.commen.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import jianxiongrao.smileimitation.R

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
object ScreenUtil{
    internal var height = 0
    internal var width = 0
    internal  fun h(context:Context):Int{
        if(height == 0){
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
             wm.defaultDisplay.getMetrics(outMetrics)
            height = outMetrics.heightPixels
        }
        return height
    }
    internal  fun w(context:Context):Int{
        if(width == 0){
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(outMetrics)
            width = outMetrics.widthPixels
        }
        return width
    }
    //px = dp * desinty
    internal  fun dp2px(context: Context,dpVal:Float):Int{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,context.resources.displayMetrics).toInt()
    }
    fun setTransparentStatusBar(activity:AppCompatActivity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = R.color.colorPrimary
        }
    }
}
