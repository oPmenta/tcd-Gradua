package com.example.gradua.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "gradua.db"
        private const val DATABASE_VERSION = 2 // Incrementei a versão para forçar atualização (cuidado: isso apaga dados antigos no onUpgrade abaixo)
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_SCHOOL = "school" // Alterado de phone para school
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_EXAM_TYPE = "exam_type"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_SCHOOL + " TEXT," // Alterado
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_EXAM_TYPE + " TEXT" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(name: String, school: String, email: String, pass: String, examType: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_SCHOOL, school) // Alterado
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PASSWORD, pass)
        values.put(COLUMN_EXAM_TYPE, examType)

        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    fun checkUser(email: String, pass: String): Boolean {
        val db = this.readableDatabase
        val columns = arrayOf(COLUMN_ID)
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, pass)
        val cursor: Cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null)
        val count = cursor.count
        cursor.close()
        db.close()
        return count > 0
    }

    // Nova função para pegar os dados do Perfil
    @SuppressLint("Range")
    fun getUserDetails(email: String): Map<String, String>? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, null, "$COLUMN_EMAIL = ?", arrayOf(email), null, null, null)

        var userDetails: Map<String, String>? = null
        if (cursor.moveToFirst()) {
            userDetails = mapOf(
                "name" to cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                "school" to cursor.getString(cursor.getColumnIndex(COLUMN_SCHOOL)),
                "email" to cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                "examType" to cursor.getString(cursor.getColumnIndex(COLUMN_EXAM_TYPE))
            )
        }
        cursor.close()
        db.close()
        return userDetails
    }
}