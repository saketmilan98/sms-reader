package com.tcp.smsreader.broadcast

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.tcp.smsreader.MainActivity
import com.tcp.smsreader.R
import com.tcp.smsreader.app.MyApp.Companion.CHANNEL_ID
import com.tcp.smsreader.tools.GetContactNameListener
import com.tcp.smsreader.tools.Tools


class MySMSBroadCastReceiver : BroadcastReceiver(), GetContactNameListener {

    var messageBody = ""
    var senderNumber = ""
    var timeInMillis = ""
    var mContext : Context? = null

    override fun contactName(name: String?) {

        val notificationIntent = Intent(mContext, MainActivity::class.java)
        notificationIntent.putExtra("timeInMillis",messageBody)
        val pendingIntent = PendingIntent.getActivity(
            mContext!!,
            0, notificationIntent, PendingIntent.FLAG_ONE_SHOT
        )
        val notificationBuilder = NotificationCompat.Builder(mContext!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_icon_png)
            .setContentTitle("New sms received from $name")
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(messageBody))
            .setAutoCancel(true)
            .build()

        val notificationManager = mContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder)

    }
    override fun onReceive(context: Context?, intent: Intent?) {
        mContext = context
        if (intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            val smsMessages =
                Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in smsMessages) {
                messageBody = message.messageBody
                senderNumber = message.originatingAddress.toString()
                timeInMillis = message.timestampMillis.toString()
            }
            Tools().getContactName(senderNumber,context!!,this)
        }
    }
}