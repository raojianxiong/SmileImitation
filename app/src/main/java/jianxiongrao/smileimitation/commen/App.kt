package jianxiongrao.smileimitation.commen

import android.app.Application
import android.os.Build
import android.view.View

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/6
 */
class App:Application() {
    companion object{
        var instance :App by notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}