package com.tcp.smsreader.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tcp.smsreader.MainActivity
import com.tcp.smsreader.R
import com.tcp.smsreader.dataclass.ConversationDC
import com.tcp.smsreader.dataclass.MessageDC

class SmsListRecyclerViewAdapter(val dataa: ArrayList<ConversationDC>, val context: Context?) : androidx.recyclerview.widget.RecyclerView.Adapter<SmsListRecyclerViewAdapter.ViewHolder>() {

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
                //(context as MainActivity).selectedSms = ""
            }

            val adapter = SmsListInternalRecyclerViewAdapter(it.message as ArrayList<MessageDC>, context!!)
            holder.recyclerview1.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                RecyclerView.VERTICAL,false)
            holder.recyclerview1.adapter = adapter



            holder.itemView.setOnClickListener {

            }
        }
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.tv1_slrl)
        val recyclerview1 = view.findViewById<RecyclerView>(R.id.rv1_slrl)
    }


}

class SmsListInternalRecyclerViewAdapter(val dataa: ArrayList<MessageDC>, val context: Context?) : androidx.recyclerview.widget.RecyclerView.Adapter<SmsListInternalRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.sms_list_internal_recycler_layout, p0, false))
    }

    override fun getItemCount() =
        dataa.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        dataa[position].let{
            Log.e("selectedSms1",(context as MainActivity).selectedSms)
            Log.e("timeInMillis1",it.timeInMillis)
            if((context as MainActivity).selectedSms == it.body){
                Log.e("selectedSms2",(context as MainActivity).selectedSms)
                Log.e("timeInMillis2",it.timeInMillis)


                holder.textView.text = (
                        "SELECTED \nSMS id : ${it.id} \n" +
                                "SMS type:  ${it.type} \n" +
                                "Diff in hours : ${it.diffHours} \n" +
                                "Number: ${it.number} \n"+
                                "Message Body: ${it.body} \n" +
                                "Sent Date: ${it.date} \n"
                        )
                (context as MainActivity).selectedSms = ""
            }else{
                holder.textView.text = (
                        "SMS id : ${it.id} \n" +
                                "SMS type:  ${it.type} \n" +
                                "Diff in hours : ${it.diffHours} \n" +
                                "Number: ${it.number} \n"+
                                "Message Body: ${it.body} \n" +
                                "Sent Date: ${it.date} \n"
                        )
            }


        }
        holder.itemView.setOnClickListener {

        }
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.tv1_slirl)
    }


}