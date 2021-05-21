package com.example.sqllitedemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null,DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"
        private val TABLE_CONTACTS = "EmployeeTable"

        private val KEY_ID = "_id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val KEY_ALAMAT = "alamat"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE $TABLE_CONTACTS('_id' INTEGER PRIMARY KEY,'name'TEXT,'email'TEXT,'alamat'TEXT)")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun addEmployee(emp: EmpModelClass) : Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.name)
        contentValues.put(KEY_EMAIL, emp.email)
        contentValues.put(KEY_ALAMAT, emp.alamat)

        //inserting employee details using insert query
        val succes = db.insert(TABLE_CONTACTS, null,contentValues)
        db.close()
        return succes
    }

    fun deleteEmployee(emp: EmpModelClass) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID,emp.id)

        val succes = db.delete(TABLE_CONTACTS, KEY_ID + "=" + emp.id, null)
        db.close()
        return succes
    }

    fun updateEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME,emp.name)
        contentValues.put(KEY_EMAIL,emp.email)
        contentValues.put(KEY_ALAMAT, emp.alamat)

        val succses = db.update(TABLE_CONTACTS,contentValues, KEY_ID + "="+ emp.id,null)

        db.close()
        return succses
    }

    //function to read the records
    fun viewEmployee() : ArrayList<EmpModelClass> {
        val empList : ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val selecQuery = "SELECT * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase

        var cursor : Cursor? = null

        try {
            cursor = db.rawQuery(selecQuery,null)
        }catch (e: SQLiteException){
         db.execSQL(selecQuery)
            return ArrayList()
        }
        var id : Int
        var name : String
        var email : String
        var alamat : String

        if (cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name= cursor.getString(cursor.getColumnIndex(KEY_NAME))
                email= cursor.getString(cursor.getColumnIndex(KEY_EMAIL))
                alamat= cursor.getString(cursor.getColumnIndex(KEY_ALAMAT))

                val emp = EmpModelClass(id = id, name = name, email = email, alamat = alamat)
                empList.add(emp)
            }while (cursor.moveToNext())
        }
        return empList
    }

}