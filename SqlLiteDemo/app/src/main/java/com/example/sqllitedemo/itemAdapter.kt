package com.example.sqllitedemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class itemAdapter (val context: Context , val items: ArrayList<EmpModelClass>) : RecyclerView.Adapter<itemAdapter.ViewHolder>(){
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val llMain = view.llMain
//        val tvName = view.tvName
        val tvEmail= view.tvEmail
//        val tvAlamat= view.tvAlamat
        val ivEdit = view.ivEdit
        val ivDelete= view.ivDelete
        val ivDetail= view.ivDetail
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row,parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

//        holder.tvName.text = item.name
        holder.tvEmail.text = item.email
//        holder.tvAlamat.text = item.alamat

        if (position % 2 == 0){
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context,R.color.teal_200))
        }else{
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context,R.color.teal_700))
        }
        holder.ivDelete.setOnClickListener{View->
            if (context is MainActivity){
                context.deleteRecordAlertDialog(item)
            }
        }
        holder.ivEdit.setOnClickListener{ View ->
            if (context is MainActivity){
                context.updateRecordDialog(item)
            }
        }
        holder.ivDetail.setOnClickListener { View ->
            if (context is MainActivity) {
                context.detailRecordDialog(item)
            }
        }
    }
}