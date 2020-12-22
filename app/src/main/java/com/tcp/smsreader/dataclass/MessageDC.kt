package com.tcp.smsreader.dataclass

import java.util.*

class MessageDC(val id : String, val type: String,  var number: String, val body: String, val date: Date, val diffHours: String, val timeInMillis : String, val originalNumber : String)