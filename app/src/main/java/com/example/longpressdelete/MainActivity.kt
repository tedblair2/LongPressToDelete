package com.example.longpressdelete

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode
import android.view.ActionMode.Callback
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.longpressdelete.Adapter.ItemAdapter
import com.example.longpressdelete.Model.Item
import com.example.longpressdelete.Model.LongClick
import com.example.longpressdelete.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(),LongClick{
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: ItemAdapter
    lateinit var itemlist:ArrayList<Item>
    private var actionMode: ActionMode?=null
    private var position:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item1=Item("Ted Omino","Jkuat",23,false)
        val item2=Item("Israel Omino","TTTI",21,false)
        val item3=Item("Brayden Omino","Komarock primary",9,false)
        val item4=Item("Valarie Omino","Lwak Girls",33,false)
        val item5=Item("Egbert Omino","Lenana School",31,false)
        val item6=Item("Young Bill Omino","Kanga School",28,false)
        itemlist= arrayListOf()
        itemlist.add(item1)
        itemlist.add(item2)
        itemlist.add(item3)
        itemlist.add(item4)
        itemlist.add(item5)
        itemlist.add(item6)
        adapter= ItemAdapter(itemlist,this,this)
        binding.recyclerItems.setHasFixedSize(true)
        binding.recyclerItems.layoutManager=LinearLayoutManager(this)
        binding.recyclerItems.adapter=adapter

    }
    private val actioncallback=object :ActionMode.Callback{
        override fun onCreateActionMode(p0: ActionMode, p1: Menu): Boolean {
            p0.menuInflater.inflate(R.menu.del_menu,p1)
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode, p1: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(p0: ActionMode, p1: MenuItem): Boolean {
            return when(p1.itemId){
                R.id.delete->{
                    deleteItems()
                    true
                }
                else->false
            }
        }

        override fun onDestroyActionMode(p0: ActionMode) {
            for(item in itemlist){
                item.isSelected=false
            }
            adapter.notifyDataSetChanged()
            actionMode = null
        }
    }

    override fun onLongpress(position: Int) {
        if (actionMode == null){
            actionMode=startActionMode(actioncallback)
        }
        //toggle selection
        this.position=position
        val item=itemlist[position]
        itemlist[position].isSelected=!item.isSelected
        adapter.notifyDataSetChanged()

        var total=0
        for (child in itemlist){
            if (child.isSelected){
                total++
            }
        }
        actionMode?.title=total.toString()
    }

    private fun deleteItems() {
        val dialog=AlertDialog.Builder(this)
        dialog.setTitle("Delete")
        dialog.setMessage("Are you sure you want to delete these items?")
        dialog.setPositiveButton("Yes"){_,_->
            val list= arrayListOf<Item>()
            var total=0
            list.addAll(itemlist)
            for (item in list){
                if (item.isSelected){
                    total++
                    itemlist.remove(item)
                    adapter.notifyItemRemoved(position!!)
                }
            }
            Snackbar.make(binding.recyclerItems,"Deleted $total items",Snackbar.LENGTH_LONG).show()
            actionMode?.finish()
        }
        dialog.setNegativeButton("No"){_,_->
            for(item in itemlist){
                item.isSelected=false
            }
            adapter.notifyDataSetChanged()
            actionMode = null
        }
        dialog.create().show()
    }
}