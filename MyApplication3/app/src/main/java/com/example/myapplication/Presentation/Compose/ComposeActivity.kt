package com.example.myapplication.Presentation.Compose


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.Data.MailData
import com.example.myapplication.Domain.Mail
import com.example.myapplication.Data.MyContentProvider
import com.example.myapplication.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.compose_activity)
        val toolbar = findViewById<Toolbar>(R.id.toolbar3)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.WHITE)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        val editText: EditText = findViewById(R.id.editText3)
        editText.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                editText.hint = getString(R.string.compose)
            } else {
                editText.hint = null
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main1, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.send) {
            val editText: EditText = findViewById(R.id.editText1)
            val editText1: EditText = findViewById(R.id.editText2)
            val editText2: EditText = findViewById(R.id.editText3)
            val string = editText.text.toString()
            val string1 = editText1.text.toString()
            val string2 = editText2.text.toString()
            if (string.isEmpty()) {
                val builder = AlertDialog.Builder(this)
                //set title for alert dialog
                //set message for alert dialog
                builder.setMessage("No recipients in To or Cc")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("OK") { _, _ ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                true
            }
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(string).matches()) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Email id is invalid")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("OK") { _, _ ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                true
            }
            else if (string1.isEmpty()) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Send email without subject?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("SEND") { _, _ ->
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh.mm a")
                    val formatted = current.format(formatter).toString()
                    val list = formatted.split(" ")
                    val mail = Mail(MailData.getsize() + 1, "android@gmail.com", string, string1, string2, list[0], list[1] + " " + list[2])
                    val context = applicationContext
                    MyContentProvider.DatabaseHelper(context).insertdata(mail)
                    Toast.makeText(applicationContext, "Mail sent", Toast.LENGTH_SHORT).show()
                    finish()
                }
                builder.setNegativeButton("CANCEL") { _, _ ->
                }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                true
            }
            else
            {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh.mm a")
                val formatted = current.format(formatter).toString()
                val list = formatted.split(" ")
                val mail = Mail(MailData.getsize() + 1, "android@gmail.com", string, string1, string2, list[0], list[1] + " " + list[2])
                val context = applicationContext
                MyContentProvider.DatabaseHelper(context).insertdata(mail)
                Toast.makeText(applicationContext, "Mail sent", Toast.LENGTH_SHORT).show()
                finish()
                true
            }
        }
        else
        {
            super.onOptionsItemSelected(item)
        }




    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}