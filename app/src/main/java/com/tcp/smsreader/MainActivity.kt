package com.tcp.smsreader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tcp.smsreader.tools.Tools


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchSmsList()

    }

    private fun fetchSmsList(){
        Tools().getSmsConversation(this){ conversations ->

            conversations?.forEach { it ->
                println("Diff in hours (parent):  ${it.diffHour}")

                it.message.forEach { itt ->
                    println("SMS id:  ${itt.id}")
                    println("SMS type:  ${itt.type}")
                    println("Diff in hours : ${itt.diffHours}")
                    println("Number: ${itt.number}")
                    println("Message Body: ${itt.body}")
                    println("Sent Date: ${itt.date}")
                }


            }


        }
    }
}