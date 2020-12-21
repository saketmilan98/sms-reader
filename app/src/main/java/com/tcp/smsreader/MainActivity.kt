package com.tcp.smsreader

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.tcp.smsreader.adapters.SmsListRecyclerViewAdapter
import com.tcp.smsreader.dataclass.ConversationDC
import com.tcp.smsreader.tools.Tools
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var selectedSms = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchSmsList()

    }

    private fun fetchSmsList(){
        Tools().getSmsConversation(this){ conversations ->
            val adapter = SmsListRecyclerViewAdapter(conversations as ArrayList<ConversationDC>, this)
            rv1_am.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this,
                RecyclerView.VERTICAL,false)
            rv1_am.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        if(intent.getStringExtra("timeInMillis") != null){
            val msgId = intent.getStringExtra("timeInMillis")
            Log.e("timeInMillis",msgId!!)
            selectedSms = msgId
            rv1_am.adapter?.notifyDataSetChanged()
            Toast.makeText(this,"timeInMillis transferred is $msgId",Toast.LENGTH_LONG).show()
        }
        else{
            Log.e("timeInMillis","null")
            Toast.makeText(this,"timeInMillis transferred is null",Toast.LENGTH_LONG).show()
        }
    }
}