package com.example.myapplication.Data

import com.example.myapplication.Domain.Mapvalues

object MailData {
    private val maillist = mutableListOf<Mapvalues>()
    fun additems(mail: Mapvalues): MutableList<Mapvalues> {
        maillist.add(mail)
        return maillist
    }

    fun getitems(): MutableList<Mapvalues> {
        return maillist
    }

    fun cleardata() {
        maillist.clear()
    }

    fun getsize(): Int {
        return maillist.size
    }

}