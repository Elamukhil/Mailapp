package com.example.myapplication.Data

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import com.example.myapplication.Domain.Mail


class MyContentProvider : ContentProvider() {
    companion object {
        // defining authority so that other application can access it
        const val PROVIDER_NAME = "com.example.myapplication.Data.MyContentProvider"
        const val URL_MESSAGES = "content://$PROVIDER_NAME/users"
        const val URL_FOLDER = "content://$PROVIDER_NAME/users1"

        // parsing the content URI
        val CONTENT_URI_MESSAGES = Uri.parse(URL_MESSAGES)
        val CONTENT_URI_FOLDER = Uri.parse(URL_FOLDER)
        const val Messages_uriCode = 1
        const val Folder_uriCode = 2
        var uriMatcher: UriMatcher? = null

        // declaring name of the database
        const val DATABASE_NAME = "Mail"

        // declaring version of the database
        const val DATABASE_VERSION = 1

        // sql query to create the table
        const val TABLE_MESSAGES = "create table Messages " +
                "(MessageId integer primary key,FolderId integer,Subject text,Body text,Emailfrom text, Emailto text,Date date,Time text,Isread integer)"

        const val TABLE_FOLDER = "create table Folder " +
                "(FolderId integer primary key, Foldername text)"

        init {

            // to match the content URI
            // every time user access table under content provider
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            // to access whole table
            uriMatcher!!.addURI(
                    PROVIDER_NAME,
                    "users",
                    Messages_uriCode
            )

            // to access a particular row
            // of the table
            uriMatcher!!.addURI(
                    PROVIDER_NAME,
                    "users/*",
                    Messages_uriCode
            )
            uriMatcher!!.addURI(
                    PROVIDER_NAME,
                    "users1",
                    Folder_uriCode
            )

            // to access a particular row
            // of the table
            uriMatcher!!.addURI(
                    PROVIDER_NAME,
                    "users1/*",
                    Folder_uriCode
            )
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher!!.match(uri)) {
            Messages_uriCode -> "vnd.android.cursor.dir/users"
            Folder_uriCode -> "vnd.android.cursor.dir/users1"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var _uri: Uri? = null
        when (uriMatcher!!.match(uri)) {
            Messages_uriCode -> {
                val rowID = db!!.insert("Messages", "", values)
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_MESSAGES, rowID)
                    context!!.contentResolver.notifyChange(_uri, null)
                }
            }
            Folder_uriCode -> {
                val rowID1 = db!!.insert("Folder", "", values)
                if (rowID1 > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_FOLDER, rowID1)
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
            Messages_uriCode -> {
                rowsDeleted = db!!.delete("Messages",
                        selection,
                        selectionArgs)
            }
            Folder_uriCode -> {
                rowsDeleted = db!!.delete("Folder",
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
            Messages_uriCode -> db!!.update("Messages", values, selection, selectionArgs)
            Folder_uriCode -> db!!.update("Folder", values, selection, selectionArgs)
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
        if (uri == CONTENT_URI_MESSAGES)
            queryBuilder.tables = "Messages"
        if (uri == CONTENT_URI_FOLDER)
            queryBuilder.tables = "Folder"
        val uriType = uriMatcher?.match(uri)

        when (uriType) {
            Messages_uriCode -> queryBuilder.appendWhere("MessageId")
            Folder_uriCode -> queryBuilder.appendWhere("FolderId")


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


        // creating a table in the database
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(TABLE_MESSAGES)
            db.execSQL(TABLE_FOLDER)
            db.execSQL("INSERT INTO Messages VALUES(1,1,'Testing mail','How are you','peter@gmail.com','android@gmail.com','12.04.2021','12.30 pm',1);")
            db.execSQL("INSERT INTO Messages values (2,2,'Welcome message','How are you','ram@gmail.com','android@gmail.com','1.05.2021','1.05 am',0);")
            db.execSQL("INSERT INTO Messages VALUES(3,1,'Event','Event remainder','dom@gmail.com','android@gmail.com','05.04.2021','12.45 pm',1);")
            db.execSQL("INSERT INTO Messages VALUES(4,1,'Job search','Several job matches your profile','raven@gmail.com','android@gmail.com','05.04.2021','12.45 pm',0);")
            db.execSQL("INSERT INTO Messages VALUES(5,5,'Job search','Several job matches your profile','raven@gmail.com','android@gmail.com','05.04.2021','12.45 pm',0);")

            db.execSQL("INSERT INTO Folder VALUES(1,'Inbox');")
            db.execSQL("INSERT INTO Folder VALUES(2,'Drafts');")
            db.execSQL("INSERT INTO Folder VALUES(3,'Sent');")
            db.execSQL("INSERT INTO Folder VALUES(4,'Spam');")
            db.execSQL("INSERT INTO Folder VALUES(5,'Trash');")


        }

        override fun onUpgrade(
                db: SQLiteDatabase,
                oldVersion: Int,
                newVersion: Int
        ) {

            // sql query to drop a table
            // having similar name
            db.execSQL("DROP TABLE IF EXISTS Messages")
            onCreate(db)
        }


    }
}
