package com.example.myapplication.Presentation.NavigationDrawer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.example.myapplication.Data.MailData
import com.example.myapplication.Domain.Mail
import com.example.myapplication.Data.MyContentProvider
import com.example.myapplication.Presentation.Compose.ComposeActivity
import com.example.myapplication.R
import com.example.myapplication.R.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class NavigationDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_drawer)
        this.setTitle("Inbox")
        val toolbar = findViewById<Toolbar>(id.toolbar)
        setSupportActionBar(toolbar)
        val fab = findViewById<FloatingActionButton>(id.fab)


        fab.setOnClickListener { view ->
            var intent = Intent(this, ComposeActivity::class.java)
            startActivity(intent)
        }
        val drawer = findViewById<DrawerLayout>(id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, string.navigation_drawer_open, string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val details: HomeFragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as HomeFragment?


        if (details == null ) {
            val fragment = HomeFragment()

            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.nav_host_fragment, fragment)
            fragment.displaylist("Inbox")
            ft.commit()
        }

         }


    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(id.drawer_layout)

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {

            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            val context=applicationContext
            val mail = Mail(MailData.getsize() + 1, "ram@gmail.com", "android@gmail.com", "Survey", "This is a survey", "12.05.2021", "01.15 pm")
            MyContentProvider.DatabaseHelper(context).insertdata1(mail)
            val mail1 = Mail(MailData.getsize() + 1, "gokul@gmail.com", "android@gmail.com", "Payment", "Your bill is overdue", "01.05.2021", "12.15 pm")
            MyContentProvider.DatabaseHelper(context).insertdata1(mail1)
            val mail2 = Mail(MailData.getsize() + 1, "rahul@gmail.com", "android@gmail.com", "Movie tickets", "You booked 3 tickets", "03.05.2021", "11.15 am")
            MyContentProvider.DatabaseHelper(context).insertdata1(mail2)
            val mail3 = Mail(MailData.getsize() + 1, "paul@gmail.com", "android@gmail.com", "Hdfc Bank", "1000rs has been debited", "12.05.2021", "01.35 pm")
            MyContentProvider.DatabaseHelper(context).insertdata1(mail3)
            val mail4 = Mail(MailData.getsize() + 1, "charles@gmail.com", "android@gmail.com", "Swiggy", "Your order has been delivered", "12.05.2021", "01.15 pm")
            MyContentProvider.DatabaseHelper(context).insertdata1(mail4)
            val details: HomeFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as HomeFragment
                details.displaylist("Inbox")
            true
        }
        else
            super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val details: HomeFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as HomeFragment

        if (id == R.id.nav_inbox) {
            details!!.displaylist("Inbox")
            toolbar.setTitle("Inbox")

        } else if (id == R.id.nav_drafts) {
            details!!.displaylist("Drafts")
            toolbar.setTitle("Drafts")

        } else if (id == R.id.nav_sent) {
            details!!.displaylist("Sent")
            toolbar.setTitle("Sent")
        } else if (id == R.id.nav_spam) {

            details!!.displaylist("Spam")
            toolbar.setTitle("Spam")

        } else if (id == R.id.nav_trash) {
            details!!.displaylist("Trash")
            toolbar.setTitle("Trash")
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }



}


