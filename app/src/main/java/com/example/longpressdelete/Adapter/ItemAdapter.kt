package com.example.longpressdelete.Adapter

import android.content.Context
import android.graphics.Color
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.longpressdelete.Model.Item
import com.example.longpressdelete.Model.LongClick
import com.example.longpressdelete.R
import com.example.longpressdelete.databinding.ItemLayoutBinding

class ItemAdapter(private val itemlist:ArrayList<Item>,private val context: Context,
                  private val longClick: LongClick):RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private var multiselect=false
    private val selecteditems= arrayListOf<Item>()

    class ViewHolder(val binding: ItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=itemlist[position]
        holder.binding.name.text=item.name
        holder.binding.school.text=item.school
        holder.binding.age.text=item.age.toString()
        checkItem(item,holder)

        if (selecteditems.isEmpty()){
            multiselect=false
        }

        holder.itemView.setOnLongClickListener {
            multiselect=true
            longClick.onLongpress(position)
            selectItem(item)
            true
        }
        holder.itemView.setOnClickListener {
            if (multiselect){
                longClick.onLongpress(position)
                selectItem(item)
            }else{
                Toast.makeText(context,item.name,Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkItem(item: Item, holder: ItemAdapter.ViewHolder) {
        if (!item.isSelected){
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            holder.binding.check.visibility= View.GONE
            multiselect=false
        }else{
            holder.binding.check.visibility=View.VISIBLE
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.gray))
            multiselect=true
        }
    }
    private fun selectItem(item: Item) {
        if (selecteditems.contains(item)){
            selecteditems.remove(item)
        }else{
            selecteditems.add(item)
        }
    }
    override fun getItemCount(): Int {
        return itemlist.size
    }

}