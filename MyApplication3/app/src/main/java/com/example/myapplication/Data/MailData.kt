package com.example.myapplication.Data

import com.example.myapplication.Domain.Folder
import com.example.myapplication.Domain.Mail
import com.example.myapplication.Domain.Mapvalues
object MailData {
    val folderdata = mutableListOf(
            Folder(1, "Inbox", "Normal"),
            Folder(2, "Drafts", "Normal"),
            Folder(3, "Sent", "Normal"),
            Folder(4, "Spam", "Normal"),
            Folder(5, "Trash", "Normal")

    )
    private val maillist= mutableListOf<Mapvalues>()
    fun additems(mail: Mapvalues):MutableList<Mapvalues>
    {
        maillist.add(mail)
        return maillist
    }
    fun getitems(): MutableList<Mapvalues>
    {
        return maillist
    }
    fun cleardata()
    {
        maillist.clear()
    }
    fun getsize():Int
    {
        return maillist.size
    }

}