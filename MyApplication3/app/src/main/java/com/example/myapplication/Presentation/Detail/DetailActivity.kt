package com.example.myapplication.Presentation.Detail


import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.Data.MailData
import com.example.myapplication.Data.MyContentProvider
import com.example.myapplication.R
import java.text.SimpleDateFormat
import java.util.*


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        val intent = intent
        val mail=intent.getIntExtra("mail", 0)
        val list= MailData.getitems().filter { it.mail.mailId == mail }
        val str = list[0].mail.from
        val str1 = list[0].mail.subject
        val str2 = list[0].mail.body
        val str3 = list[0].mail.date
        val str5=list[0].mail.time
        val str6=list[0].mail.to
        val str4="<html> <body>" +
                "<p>$str2</p>" +
                "</body></html>"
        var str7=list[0].label

        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
        val myDate: Date = sdf.parse(str3)
        sdf.applyPattern("EEE, d MMM yyyy")
        val Date: String = sdf.format(myDate)
        val view = findViewById<View>(R.id.cardtextview) as TextView
        view.setText(str1)

        val textView1 = findViewById<View>(R.id.textView1) as TextView
        textView1.setText(str)
        val textView2 = findViewById<View>(R.id.textView2) as TextView
        textView2.setText(Date)
        val textView3 = findViewById<View>(R.id.textView3) as TextView
        textView3.setText(str5)
        val textView4 = findViewById<View>(R.id.textView4) as TextView
        textView4.setText("To : $str6")
        val textView5 = findViewById<View>(R.id.textView5) as TextView
        textView5.setText(str.substring(0,1).toUpperCase())
        val textView6 = findViewById<View>(R.id.textView6) as TextView
        textView6.setText("\u2022 $str7")


        val mywebview = findViewById<View>(R.id.webView) as WebView

        mywebview.loadData(str4!!, "text/html", "UTF-8")
        val context=applicationContext
        MyContentProvider.DatabaseHelper(context).isread(mail)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        val intent = intent
        val mail=intent.getIntExtra("mail", 1)
        return if (id == R.id.action_settings) {
            val list = MailData.getitems().filter { it.mail.mailId == mail }
            if(list[0].label=="Trash") {


                Toast.makeText(getApplicationContext(), "Mail deleted", Toast.LENGTH_SHORT).show()
                val context=applicationContext
                MyContentProvider.DatabaseHelper(context).deletedata(mail)


                finish()
                return true
            }
            else
            {
                val context=applicationContext
                MyContentProvider.DatabaseHelper(context).updatedata(mail)
                Toast.makeText(getApplicationContext(), "Mail moved to Trash", Toast.LENGTH_SHORT).show()


                finish()
                return true
            }

        }
        else
            super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun click(view: View?) {
        val view: WebView = findViewById(R.id.webView)
        if(view.visibility==View.VISIBLE)
        view.setVisibility(View.INVISIBLE)
        else
            view.setVisibility(View.VISIBLE)


    }

}