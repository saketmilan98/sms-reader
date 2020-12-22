package com.tcp.smsreader.tools

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.provider.Telephony
import com.tcp.smsreader.dataclass.ConversationDC
import com.tcp.smsreader.dataclass.MessageDC
import java.util.*


class Tools{
    fun getDiffInHours(date1InMillis : Long, date2InMillis : Long) : Long{
        val mills = date1InMillis - date2InMillis
        val hours: Long = mills / (1000 * 60 * 60)
        val mins = (mills / (1000 * 60) % 60)

        return hours
    }
    fun getSmsConversation(context: Context, completion: (conversations: List<ConversationDC>?) -> Unit) {
        val cursor = context.contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
        val messages = ArrayList<MessageDC>()
        val diffHourList = ArrayList<String>()
        val results = ArrayList<ConversationDC>()
        while (cursor != null && cursor.moveToNext()) {
            val smsType = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.TYPE))
            val smsDate  = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
            val diffHours = Tools().getDiffInHours(System.currentTimeMillis(),smsDate.toLong())
            if(diffHours <= 24){
                if(smsType == "1"){
                    val smsId = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms._ID))
                    val number = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))

                    diffHourList.add(getHourByCategory(diffHours).toString())
                    messages.add(MessageDC(smsId,smsType, number, body, Date(smsDate.toLong()), getHourByCategory(diffHours).toString(), smsDate, number))
                }
            }
            else{
                break
            }
        }
        cursor?.close()

        diffHourList.forEach { hour ->
            if (results.find { it.diffHour == hour } == null) {
                val msg = messages.filter { it.diffHours == hour }
                results.add(ConversationDC(diffHour = hour, message = msg))
            }
        }

        completion(results)
    }

    private fun getHourByCategory(hour1 : Long) : Int { // method for returning appropriate Group for 1 hour, 2 hours, 3 hours, 6 hours, 12 hours and 1 day ago.

        return if(hour1 in 0..1)
            1
        else if( hour1 in 1..2)
            2
        else if( hour1 in 2..3)
            3
        else if( hour1 in 3..6)
            6
        else if( hour1 in 6..12)
            12
        else if( hour1 in 12..24)
            24
        else
            99
    }

    fun getContactName2(phoneNumber: String?, context : Context) : String {
        val cr: ContentResolver = context.contentResolver
        val uri: Uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )
        val cursor = cr.query(
            uri,
            arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
            null,
            null,
            null
        )
            ?: return phoneNumber!!
        var contactName: String? = null
        if (cursor.moveToFirst()) {
            contactName =
                cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
        }
        if (!cursor.isClosed) {
            cursor.close()
        }
        if(contactName == null){
            return phoneNumber!!
        }
        else{
            return contactName
        }
    }
}