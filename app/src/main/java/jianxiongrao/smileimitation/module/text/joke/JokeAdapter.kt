package jianxiongrao.smileimitation.module.text.joke

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import jianxiongrao.smileimitation.R
import jianxiongrao.smileimitation.model.Joke
import org.jetbrains.anko.find
import java.util.regex.Pattern

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
class JokeAdapter(var items: List<Joke>?) : RecyclerView.Adapter<JokeAdapter.MyViewHolder>() {
    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder?{
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_joke,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = items?.get(position)?.title
        //原数据有html标签，过滤掉
        val p_html = Pattern.compile("<[^>]+>",Pattern.CASE_INSENSITIVE)//忽略大小写,<>中间不是>即可
        val m_html = p_html.matcher(items?.get(position)?.text)
        holder.textView.text = m_html.replaceAll("")
    }

    class MyViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val textView: TextView = item.find(R.id.text)
        val title: TextView = item.find(R.id.title)
    }

}