package jianxiongrao.smileimitation.module.text.rhesis

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import jianxiongrao.smileimitation.R
import jianxiongrao.smileimitation.model.Rhesis
import org.jetbrains.anko.find

/**
 * author: Jianxiong Rao
 * email:1272670593@qq.com
 * on 2018/1/7
 */
class RhesisAdapter(var items:List<Rhesis>?) : RecyclerView.Adapter<RhesisAdapter.MyViewHolder>(){
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = items?.get(position)?.english+"\n"+"\n"+items?.get(position)?.chinese
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder? {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rhesis,parent,false))
    }

    override fun getItemCount(): Int {
        return items?.size ?:0
    }
    class MyViewHolder(item:View) :RecyclerView.ViewHolder(item){
        val textView:TextView = item.find(R.id.text)
    }
}