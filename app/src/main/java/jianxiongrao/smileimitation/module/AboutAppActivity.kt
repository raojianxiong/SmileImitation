package jianxiongrao.smileimitation.module

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import jianxiongrao.smileimitation.R
import kotlinx.android.synthetic.main.activity_about_app.*

class AboutAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mVersion.text = "v  ${getVersion()}"
        about_toolbar.setNavigationOnClickListener { finish() }
    }
    private fun getVersion():String{
        try{
            val pi = packageManager.getPackageInfo(packageName,0)
            return pi.versionName
        }catch (e:Exception){
            return "1.0"
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0,R.anim.out_to_right)
    }
}
