package com.tcp.smsreader

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.tcp.smsreader.adapters.SmsListRecyclerViewAdapter
import com.tcp.smsreader.dataclass.ConversationDC
import com.tcp.smsreader.tools.Tools
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    var conversationsList = ArrayList<ConversationDC>()
    var selectedSms = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkForPermissions()
    }

    private fun fetchSmsList(){
        Tools().getSmsConversation(this){ conversations ->
            conversationsList = conversations as ArrayList<ConversationDC>
            setRecyclerView()
            thread {
                conversationsList.forEach {
                    it.message.forEach { it1 ->
                        it1.number = Tools().getContactName2(it1.number, this)
                    }
                }
                runOnUiThread {
                    rv1_am.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setRecyclerView(){
        val adapter = SmsListRecyclerViewAdapter(conversationsList, this)
        rv1_am.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        rv1_am.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        if(intent.getStringExtra("messageBody") != null){
            val messageBody = intent.getStringExtra("messageBody")
            selectedSms = messageBody!!
            rv1_am.adapter?.notifyDataSetChanged()
        }
    }

    private fun checkForPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this,"Please grant permissions.",Toast.LENGTH_LONG).show()
            val perms = arrayOf(
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_CONTACTS
            )
            ActivityCompat.requestPermissions(this, perms, PERMS_REQUEST_CODE)
            return
        } else {
            fetchSmsList()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMS_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                for (i in grantResults.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        if (i == grantResults.size-1) {
                            fetchSmsList()
                        }
                    } else {
                        finish()
                        break
                    }
                }
            }
            else{
                finish()
            }
        }
    }

    companion object{
        val PERMS_REQUEST_CODE = 200
    }

}