package com.tcp.smsreader.adapters

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tcp.smsreader.MainActivity
import com.tcp.smsreader.R
import com.tcp.smsreader.dataclass.ConversationDC
import com.tcp.smsreader.dataclass.MessageDC
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule

class SmsListRecyclerViewAdapter(val dataa: ArrayList<ConversationDC>, val context: Context?) : androidx.recyclerview.widget.RecyclerView.Adapter<SmsListRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.sms_list_recycler_layout, p0, false))
    }

    override fun getItemCount() =
        dataa.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataa[position].let {
            if(it.diffHour.toInt() == 1){
                holder.textView.text =( "${it.diffHour} hour ago ")
            } else if (it.diffHour.toInt() in 2..12) {
                holder.textView.text =( "${it.diffHour} hours ago ")
            } else if (it.diffHour.toInt() in 13..24) {
                holder.textView.text =( "1 day ago ")
            } else{
                holder.textView.text =( "Older")
            }

            if((context as MainActivity).selectedSms.isNotEmpty()){
                holder.recyclerview1.adapter?.notifyDataSetChanged()
            }

            val adapter = SmsListInternalRecyclerViewAdapter(it.message as ArrayList<MessageDC>, context!!)
            holder.recyclerview1.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                RecyclerView.VERTICAL,false)
            holder.recyclerview1.adapter = adapter
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.tv1_slrl)
        val recyclerview1 = view.findViewById<RecyclerView>(R.id.rv1_slrl)
    }


}

class SmsListInternalRecyclerViewAdapter(val dataa: ArrayList<MessageDC>, val context: Context?) : androidx.recyclerview.widget.RecyclerView.Adapter<SmsListInternalRecyclerViewAdapter.ViewHolder>() {

    companion object{
        val format1 = SimpleDateFormat("hh:mm a",Locale.getDefault())
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.sms_list_internal_recycler_layout, p0, false))
    }

    override fun getItemCount() =
        dataa.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataa[position].let{
            if((context as MainActivity).selectedSms == it.body){
                holder.cl1.setBackgroundColor(context.resources!!.getColor(R.color.yellow,context.theme))
                //Timer().schedule(10000) {
                //    holder.cl1.setBackgroundColor(context.resources!!.getColor(R.color.white,context.theme))
                //}

            }
            val tempDate1 = Date()
            tempDate1.time = it.timeInMillis.toLong()
            holder.textView2.text = format1.format(tempDate1)
            holder.textView1.text = it.number
            holder.textView.text = it.body

        }
        holder.itemView.setOnClickListener {
            (context as MainActivity).selectedSms = ""
            val dialogBuilder = AlertDialog.Builder(context!!)
            dialogBuilder.setMessage(
                        "Sender: ${dataa[position].number} (${dataa[position].originalNumber})\n"+
                        "Message: ${dataa[position].body} \n" +
                        "Sent on: ${dataa[position].date}"
            )
                .setCancelable(true)
                .setPositiveButton("Close", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            val alert = dialogBuilder.create()
            alert.setTitle("Message Details")
            alert.show()
            holder.cl1.setBackgroundColor(context.resources!!.getColor(R.color.white,context.theme))
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.tv3_slirl)
        val textView1 = view.findViewById<TextView>(R.id.tv2_slirl)
        val textView2 = view.findViewById<TextView>(R.id.tv4_slirl)
        val cl1 = view.findViewById<ConstraintLayout>(R.id.cl1_slirl)

    }


}