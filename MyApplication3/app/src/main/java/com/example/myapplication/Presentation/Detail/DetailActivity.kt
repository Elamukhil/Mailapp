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
        val mail = intent.getIntExtra("mail", 0)
        val list = MailData.getInstance(applicationContext).getitems().filter { it.mailId == mail }
        val from = list[0].from
        val subject = list[0].subject
        val body = list[0].body
        val date = list[0].date
        val time = list[0].time
        val to = list[0].to
        val webtext = "<html> <body>" +
                "<p>$body</p>" +
                "</body></html>"
        val foldername = MailData.getInstance(applicationContext).getfoldername(list[0].folderId)
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
        val myDate: Date = sdf.parse(date)
        sdf.applyPattern("EEE, d MMM yyyy")
        val Date: String = sdf.format(myDate)
        val view = findViewById<View>(R.id.cardtextview) as TextView
        view.setText(subject)
        val textView1 = findViewById<View>(R.id.textView1) as TextView
        textView1.setText(from)
        val textView2 = findViewById<View>(R.id.textView2) as TextView
        textView2.setText(Date)
        val textView3 = findViewById<View>(R.id.textView3) as TextView
        textView3.setText(time)
        val textView4 = findViewById<View>(R.id.textView4) as TextView
        textView4.setText("To : $to")
        val textView5 = findViewById<View>(R.id.textView5) as TextView
        textView5.setText(from.substring(0, 1).toUpperCase())
        val textView6 = findViewById<View>(R.id.textView6) as TextView
        textView6.setText("\u2022 $foldername")
        val mywebview = findViewById<View>(R.id.webView) as WebView
        mywebview.loadData(webtext!!, "text/html", "UTF-8")
        val context = applicationContext
        MailData.getInstance(context).isread(mail)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        val intent = intent
        val mail = intent.getIntExtra("mail", 1)
        return if (id == R.id.action_settings) {
            val list = MailData.getInstance(applicationContext).getitems().filter { it.mailId == mail }
            if (MailData.getInstance(applicationContext).getfoldername(list[0].folderId) == "Trash") {
                Toast.makeText(getApplicationContext(), "Mail deleted", Toast.LENGTH_SHORT).show()
                MailData.getInstance(applicationContext).deletedata(mail)
                finish()
                true
            } else {
                MailData.getInstance(applicationContext).updatedata(mail)
                Toast.makeText(getApplicationContext(), "Mail moved to Trash", Toast.LENGTH_SHORT).show()
                finish()
                true
            }

        } else
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

    fun click() {
        val view: WebView = findViewById(R.id.webView)
        if (view.visibility == View.VISIBLE)
            view.setVisibility(View.INVISIBLE)
        else
            view.setVisibility(View.VISIBLE)
    }

}