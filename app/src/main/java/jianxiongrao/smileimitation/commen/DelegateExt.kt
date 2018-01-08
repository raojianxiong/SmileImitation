package jianxiongrao.smileimitation.commen

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/6
 */
/**
 *  * notNull委托，只能被赋值一次，如果第二次赋值就会抛异常
 */
fun <T> notNullSingleValue(): ReadWriteProperty<Any?, T> = NotNullSingleValueVar()

/**
 * SharedPreferences 委托 自动存取
 */
fun <T : Any> preference(context: Context, name: String, default: T):
        ReadWriteProperty<Any?, T> = Preference(context, name, default)

private class NotNullSingleValueVar<T> : ReadWriteProperty<Any?, T> {
    private var value: T? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("not initialized")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value else throw  IllegalStateException("already initialized")
    }
}

private class Preference<T>(val context: Context, val name: String, val default: T) :
        ReadWriteProperty<Any?, T> {
    val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("default", Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    private fun <T> findPreference(name: String, default: T): T {
        with(prefs) {
            val res: Any = when (default) {
                is Long -> getLong(name, default)
                is String -> getString(name, default)
                is Int -> getInt(name, default)
                is Boolean -> getBoolean(name, default)
                is Float -> getFloat(name, default)
                else -> throw  IllegalArgumentException("this type can not save into preference")
            }
            return res as T
        }
    }

    /**
     * 修改数据原子提交到内存, 而后异步真正提交到硬件磁盘
     * 不关心结果 可能存成功，可能失败
     */
    private fun <T> putPreference(name:String,value: T)= with(prefs.edit()){
        when(value){
            is Long -> putLong(name,value)
            is String -> putString(name,value)
            is Int -> putInt(name,value)
            is Boolean -> putBoolean(name,value)
            is Float -> putFloat(name,value)
            else -> throw IllegalArgumentException("this type can not save into preference")
        }.apply()
    }
}

