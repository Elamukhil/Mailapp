package com.example.myapplication.Data

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

import com.example.myapplication.Domain.Mail
import com.example.myapplication.Domain.Mapvalues


class MyContentProvider : ContentProvider() {
    companion object {
        // defining authority so that other application can access it
        const val PROVIDER_NAME = "com.example.myapplication.Data.MyContentProvider"

        // defining content URI
        const val URL = "content://$PROVIDER_NAME/users"

        // parsing the content URI
        val CONTENT_URI = Uri.parse(URL)
        const val uriCode = 1
        var uriMatcher: UriMatcher? = null


        // declaring name of the database
        const val DATABASE_NAME = "Mail"

        // declaring table name of the database
        const val TABLE_NAME = "Messages"

        // declaring version of the database
        const val DATABASE_VERSION = 2

        // sql query to create the table
        const val CREATE_DB_TABLE = "create table Messages " +
                "(MessageId integer primary key, Subject text,Body text,Emailfrom text, Emailto text,Date date,Time text)"

        const val CREATE_DB_TABLE1 = "create table Folder " +
                "(FolderId integer primary key, Foldername text)"
        const val CREATE_DB_TABLE2 = "create table MessagesMapping " +
        "(MessageId integer, FolderId integer,Isread integer)"




        init {

            // to match the content URI
            // every time user access table under content provider
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            // to access whole table
            uriMatcher!!.addURI(
                    PROVIDER_NAME,
                    "users",
                    uriCode
            )

            // to access a particular row
            // of the table
            uriMatcher!!.addURI(
                    PROVIDER_NAME,
                    "users/*",
                    uriCode
            )
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher!!.match(uri)) {
            uriCode -> "vnd.android.cursor.dir/users"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    // creating the database
    override fun onCreate(): Boolean {
        val context = context
        val dbHelper =
                DatabaseHelper(context)
        db = dbHelper.writableDatabase
        return if (db != null) {
            true
        } else false
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        TODO("Not yet implemented")
    }


    // adding data to the database

    // creating object of database
    // to perform query
    private var db: SQLiteDatabase? = null

    // creating a database
    class DatabaseHelper // defining a constructor
    internal constructor(context: Context?) : SQLiteOpenHelper(
            context,
            DATABASE_NAME,
            null,
            DATABASE_VERSION
    ) {
        // creating a table in the database
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_DB_TABLE)
            db.execSQL(CREATE_DB_TABLE1)
            db.execSQL(CREATE_DB_TABLE2)
            db.execSQL("INSERT INTO Messages VALUES(1,'Testing mail','How are you','peter@gmail.com','android@gmail.com','12.04.2021','12.30 pm');")
            db.execSQL("INSERT INTO Messages values (2,'Welcome message','How are you','ram@gmail.com','android@gmail.com','1.05.2021','1.05 am');")
            db.execSQL("INSERT INTO Messages VALUES(3,'Event','Event remainder','dom@gmail.com','android@gmail.com','05.04.2021','12.45 pm');")
            db.execSQL("INSERT INTO Messages VALUES(4,'Job search','Several job matches your profile','raven@gmail.com','android@gmail.com','05.04.2021','12.45 pm');")
            db.execSQL("INSERT INTO Messages VALUES(5,'Job search','Several job matches your profile','raven@gmail.com','android@gmail.com','05.04.2021','12.45 pm');")



            db.execSQL("INSERT INTO Folder VALUES(1,'Inbox');")
            db.execSQL("INSERT INTO Folder VALUES(2,'Drafts');")
            db.execSQL("INSERT INTO Folder VALUES(3,'Sent');")
            db.execSQL("INSERT INTO Folder VALUES(4,'Spam');")
            db.execSQL("INSERT INTO Folder VALUES(5,'Trash');")
            db.execSQL("INSERT INTO MessagesMapping VALUES(1,1,1);")
            db.execSQL("INSERT INTO MessagesMapping VALUES(2,2,0);")
            db.execSQL("INSERT INTO MessagesMapping VALUES(3,1,1);")
            db.execSQL("INSERT INTO MessagesMapping VALUES(4,1,0);")
            db.execSQL("INSERT INTO MessagesMapping VALUES(5,5,0);")


        }

        override fun onUpgrade(
                db: SQLiteDatabase,
                oldVersion: Int,
                newVersion: Int
        ) {

            // sql query to drop a table
            // having similar name
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }

        fun getdata() {
            val db = this.readableDatabase

            val sql = "select * from MessagesMapping"
            val cursor = db.rawQuery(sql, null)
            if (cursor.moveToFirst()) {
                do {
                    val messageid = cursor.getString(0).toInt()
                    val folderid = cursor.getString(1).toInt()
                    val cursor1 = db.rawQuery("select * from Messages where MessageId == $messageid ", null)
                    val cursor2 = db.rawQuery("select * from Folder where FolderId == $folderid ", null)
                    cursor1.moveToFirst()
                    cursor2.moveToFirst()
                    val folder = cursor2.getString(1)
                    val isread=cursor.getInt(2)
                    val mailId: Int = cursor1.getString(0).toInt()
                    val subject: String = cursor1.getString(1)
                    val body: String = cursor1.getString(2)
                    val from: String = cursor1.getString(3)
                    val to: String = cursor1.getString(4)
                    val date: String = cursor1.getString(5)
                    val time: String = cursor1.getString(6)
                    val mail = Mail(mailId, from, to, subject, body, date, time)
                    val obj = Mapvalues(mail, folder,isread)
                    MailData.additems(obj)

                } while (cursor.moveToNext())
            }
            cursor.close()
        }

        fun insertdata(mail: Mail) {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put("MessageId", mail.mailId)
            values.put("Subject",mail.subject)
            values.put("Body", mail.body)
            values.put("Emailfrom", mail.from)
            values.put("Emailto",mail.to)
            values.put("Date", mail.date)
            values.put("Time", mail.time)
            db.insert("Messages", null, values)
            val mailid=mail.mailId
            db.execSQL("INSERT INTO MessagesMapping VALUES($mailid,3,0);")
            MailData.cleardata()
            getdata()

        }
        fun insertdata1(mail: Mail) {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put("MessageId", mail.mailId)
            values.put("Subject",mail.subject)
            values.put("Body", mail.body)
            values.put("Emailfrom", mail.from)
            values.put("Emailto",mail.to)
            values.put("Date", mail.date)
            values.put("Time", mail.time)
            db.insert("Messages", null, values)
            val mailid=mail.mailId
            db.execSQL("INSERT INTO MessagesMapping VALUES($mailid,1,0);")
            MailData.cleardata()
            getdata()

        }
        fun deletedata(messageid:Int) {
            val db = this.writableDatabase
            db.execSQL("DELETE FROM Messages WHERE MessageId = $messageid")
            db.execSQL("DELETE FROM MessagesMapping WHERE MessageId = $messageid")
            MailData.cleardata()
            getdata()
        }
        fun updatedata(messageid:Int) {
            val db = this.writableDatabase
            db.execSQL("UPDATE MessagesMapping SET FolderId = 5 WHERE MessageId=$messageid")
            MailData.cleardata()
            getdata()
        }
        fun isread(messageid: Int) {
            val db = this.writableDatabase
            db.execSQL("UPDATE MessagesMapping SET Isread = 0 WHERE MessageId=$messageid")
            MailData.cleardata()
            getdata()
        }
    }

}
