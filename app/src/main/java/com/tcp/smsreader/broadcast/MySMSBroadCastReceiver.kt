package com.tcp.smsreader.broadcast

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import androidx.core.app.NotificationCompat
import com.tcp.smsreader.MainActivity
import com.tcp.smsreader.R
import com.tcp.smsreader.app.MyApp.Companion.CHANNEL_ID
import com.tcp.smsreader.tools.Tools


class MySMSBroadCastReceiver : BroadcastReceiver() {

    var messageBody = ""
    var senderNumber = ""
    var timeInMillis = ""

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            val smsMessages =
                Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in smsMessages) {
                messageBody = message.messageBody
                senderNumber = message.originatingAddress.toString()
                timeInMillis = message.timestampMillis.toString()
            }

            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.putExtra("messageBody",messageBody)
            val pendingIntent = PendingIntent.getActivity(
                context!!,
                0, notificationIntent, PendingIntent.FLAG_ONE_SHOT
            )
            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon_png)
                .setContentTitle("New sms received from ${Tools().getContactName2(senderNumber, context)}")
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText(messageBody))
                .setAutoCancel(true)
                .build()

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notiId = (0..100).random()
            notificationManager.notify(notiId /* ID of notification */, notificationBuilder)
        }
    }
}