package com.example.myapplication.Data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import com.example.myapplication.Domain.Mail

class MailData private constructor(val context: Context) {

    private val maillist = mutableListOf<Mail>()
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: MailData? = null
        fun getInstance(context: Context): MailData {
            if (instance == null)
                instance = MailData(context)

            return instance!!
        }
    }

    fun additems(mail: Mail): MutableList<Mail> {
        maillist.add(mail)
        return maillist
    }

    fun getquery() {
        val messageprojection = arrayOf("MessageId", "FolderId", "Subject", "Body", "Emailfrom", "Emailto", "Date", "Time", "Isread")
        val cursor = context?.contentResolver?.query(MyContentProvider.CONTENT_URI_MESSAGES,
                messageprojection, null, null, null)

        if (cursor!!.moveToFirst()) {
            do {
                val mailId: Int = cursor!!.getString(0).toInt()
                val folderId: Int = cursor!!.getString(1).toInt()
                val subject: String = cursor!!.getString(2)
                val body: String = cursor!!.getString(3)
                val from: String = cursor!!.getString(4)
                val to: String = cursor.getString(5)
                val date: String = cursor.getString(6)
                val time: String = cursor!!.getString(7)
                val isread: Int = cursor.getString(8).toInt()

                val mail = Mail(mailId, folderId, isread, from, to, subject, body, date, time)
                additems(mail)


            } while (cursor.moveToNext())
            cursor.close()
        }
        println(maillist)
    }

    fun getfoldername(id: Int): String {
        val folderprojection = arrayOf("FolderId", "Foldername")
        val cursor = context?.contentResolver?.query(MyContentProvider.CONTENT_URI_FOLDER,
                folderprojection, "FolderId = $id", null, null)
        cursor?.moveToFirst()
        val foldername = cursor!!.getString(1)
        return foldername
    }

    fun insertdata(mail: Mail) {
        val values = ContentValues()
        values.put("MessageId", mail.mailId)
        values.put("FolderId", mail.folderId)
        values.put("Subject", mail.subject)
        values.put("Body", mail.body)
        values.put("Emailfrom", mail.from)
        values.put("Emailto", mail.to)
        values.put("Date", mail.date)
        values.put("Time", mail.time)
        values.put("Isread", mail.isRead)
        context.contentResolver.insert(MyContentProvider.CONTENT_URI_MESSAGES, values)
        cleardata()
        getquery()
    }

    fun deletedata(messageid: Int) {
        context.contentResolver.delete(MyContentProvider.CONTENT_URI_MESSAGES, "MessageId = $messageid", null)
    }

    fun updatedata(messageid: Int) {
        val values = ContentValues()
        values.put("FolderId", 5)
        context.contentResolver.update(MyContentProvider.CONTENT_URI_MESSAGES, values, "MessageId = $messageid", null)
    }

    fun isread(messageid: Int) {
        val values = ContentValues()
        values.put("Isread", 0)
        context.contentResolver.update(MyContentProvider.CONTENT_URI_MESSAGES, values, "MessageId = $messageid", null)
    }

    fun getitems(): MutableList<Mail> {
        return maillist
    }

    fun cleardata() {
        maillist.clear()
    }

    fun getsize(): Int {
        return maillist.size
    }

}

