package com.example.myapplication.Data

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import com.example.myapplication.Domain.Mail
import com.example.myapplication.Domain.Mapvalues


class MyContentProvider : ContentProvider() {
    companion object {
        // defining authority so that other application can access it
        const val PROVIDER_NAME = "com.example.myapplication.Data.MyContentProvider"

        // defining content URI
        const val URL = "content://$PROVIDER_NAME/users"
        const val URL1 = "content://$PROVIDER_NAME/users1"
        const val URL2 = "content://$PROVIDER_NAME/users2"

        // parsing the content URI
        val CONTENT_URI = Uri.parse(URL)
        val CONTENT_URI1 = Uri.parse(URL1)
        val CONTENT_URI2 = Uri.parse(URL2)
        const val uriCode = 1
        const val uriCode1 = 2
        const val uriCode2 = 3
        var uriMatcher: UriMatcher? = null


        // declaring name of the database
        const val DATABASE_NAME = "Mail"

        // declaring table name of the database
        const val TABLE_NAME = "Messages"
        const val TABLE_NAME1 = "MessagesMapping"

        // declaring version of the database
        const val DATABASE_VERSION = 1

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
            uriMatcher!!.addURI(
                    PROVIDER_NAME,
                    "users1",
                    uriCode1
            )

            // to access a particular row
            // of the table
            uriMatcher!!.addURI(
                    PROVIDER_NAME,
                    "users1/*",
                    uriCode1
            )
            uriMatcher!!.addURI(
                    PROVIDER_NAME,
                    "users2",
                    uriCode2
            )

            // to access a particular row
            // of the table
            uriMatcher!!.addURI(
                    PROVIDER_NAME,
                    "users2/*",
                    uriCode2
            )
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher!!.match(uri)) {
            uriCode -> "vnd.android.cursor.dir/users"
            uriCode1 -> "vnd.android.cursor.dir/users1"
            uriCode2 -> "vnd.android.cursor.dir/users2"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var _uri: Uri? = null
        when (uriMatcher!!.match(uri)) {
            uriCode -> {
                val rowID = db!!.insert(TABLE_NAME, "", values)
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI, rowID)
                    context!!.contentResolver.notifyChange(_uri, null)
                }
            }
            uriCode1 -> {
                val rowID1 = db!!.insert(TABLE_NAME1, "", values)
                if (rowID1 > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI1, rowID1)
                    context!!.contentResolver.notifyChange(_uri, null)
                }
            }
        }
        return _uri
    }

    override fun delete(
            uri: Uri,
            selection: String?,
            selectionArgs: Array<String>?
    ): Int {
        val uriType = uriMatcher?.match(uri)
        val rowsDeleted: Int

        when (uriType) {
            uriCode -> {
                rowsDeleted = db!!.delete(TABLE_NAME,
                        selection,
                        selectionArgs)
            }
            uriCode1 -> {
                rowsDeleted = db!!.delete(TABLE_NAME1,
                        selection,
                        selectionArgs)


            }
            else -> throw IllegalArgumentException("Unknown URI: " + uri)
        }
        context!!.contentResolver.notifyChange(uri, null)
        return rowsDeleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        var count = 0
        count = when (uriMatcher!!.match(uri)) {
            uriCode -> db!!.update(TABLE_NAME, values, selection, selectionArgs)
            uriCode1 -> db!!.update(TABLE_NAME1, values, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
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
        val queryBuilder = SQLiteQueryBuilder()
        if (uri == CONTENT_URI)
            queryBuilder.tables = "Messages"
        if (uri == CONTENT_URI1)
            queryBuilder.tables = "MessagesMapping"
        if (uri == CONTENT_URI2)
            queryBuilder.tables = "Folder"
        val uriType = uriMatcher?.match(uri)

        when (uriType) {
            uriCode -> queryBuilder.appendWhere("MessageId")
            uriCode1 -> queryBuilder.appendWhere("MessageId")
            uriCode2 -> queryBuilder.appendWhere("FolderId")


            else -> throw java.lang.IllegalArgumentException("Unknown URI")
        }

        val cursor = queryBuilder.query(db!!,
                projection, selection, selectionArgs, null, null, sortOrder)
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
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
        private val myCR: ContentResolver

        init {
            myCR = context!!.contentResolver
        }

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


        fun insertdata(mail: Mail, folder: Int) {
            val values = ContentValues()
            val values1 = ContentValues()

            values.put("MessageId", mail.mailId)
            values.put("Subject", mail.subject)
            values.put("Body", mail.body)
            values.put("Emailfrom", mail.from)
            values.put("Emailto", mail.to)
            values.put("Date", mail.date)
            values.put("Time", mail.time)
            values1.put("MessageId", mail.mailId)
            values1.put("FolderId", folder)
            values1.put("Isread", 0)
            myCR.insert(CONTENT_URI, values)
            myCR.insert(CONTENT_URI1, values1)
            MailData.cleardata()
            getquery()

        }

        fun deletedata(messageid: Int) {

            myCR.delete(CONTENT_URI, "MessageId = $messageid", null)
            myCR.delete(CONTENT_URI1, "MessageId = $messageid", null)
            MailData.cleardata()
            getquery()
        }

        fun updatedata(messageid: Int) {
            val values = ContentValues()
            values.put("FolderId", 5)
            myCR.update(CONTENT_URI1, values, "MessageId = $messageid", null)
            MailData.cleardata()
            getquery()
        }

        fun isread(messageid: Int) {
            val values = ContentValues()
            values.put("Isread", 0)
            myCR.update(CONTENT_URI1, values, "MessageId = $messageid", null)
            MailData.cleardata()
            getquery()
        }

        fun getquery() {
            val projection = arrayOf("MessageId", "FolderId", "Isread")
            val projection1 = arrayOf("MessageId", "Subject", "Body", "Emailfrom", "Emailto", "Date", "Time")
            val projection2 = arrayOf("FolderId", "Foldername")
            val cursor = myCR.query(CONTENT_URI1,
                    projection, null, null, null)

            if (cursor!!.moveToFirst()) {
                do {
                    val messageid = cursor.getString(0).toInt()
                    val folderid = cursor.getString(1).toInt()
                    val cursor1 = myCR.query(CONTENT_URI,
                            projection1, "MessageId = $messageid", null, null)
                    val cursor2 = myCR.query(CONTENT_URI2,
                            projection2, "FolderId = $folderid", null, null)
                    cursor1?.moveToFirst()
                    cursor2?.moveToFirst()
                    val folder = cursor2!!.getString(1)
                    val isread = cursor!!.getInt(2)
                    val mailId: Int = cursor1!!.getString(0).toInt()
                    val subject: String = cursor1!!.getString(1)
                    val body: String = cursor1!!.getString(2)
                    val from: String = cursor1!!.getString(3)
                    val to: String = cursor1!!.getString(4)
                    val date: String = cursor1!!.getString(5)
                    val time: String = cursor1!!.getString(6)
                    val mail = Mail(mailId, from, to, subject, body, date, time)
                    val obj = Mapvalues(mail,folder, isread)
                    MailData.additems(obj)


                } while (cursor.moveToNext())
                cursor.close()
            }
        }
    }

}
