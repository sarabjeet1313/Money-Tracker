package com.example.sqlitelearn1.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?): SQLiteOpenHelper(context,
    DATABASE_NAME,null, 1){

    companion object {

        val DATABASE_NAME = "moneytracker"
        val TABLE_NAME = "money_table"
        val COL_Trans_ID = "Trans_ID"
        val COL_DESC = "Desc"
        val COL_AMOUNT = "Amount"
        val COL_DATE = "Date"
        val COL_TYPE = "Type"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_Trans_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DESC + " VARCHAR(256), " +
                COL_AMOUNT + " DECIMAL(10,2), " +
                COL_DATE + " DATE, " +
                COL_TYPE + " VARCHAR(256));"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {


    }

    fun insertData(money: money) : Boolean{
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_DESC, money.desc)
        cv.put(COL_AMOUNT, money.amount)
        cv.put(COL_DATE, money.date)
        cv.put(COL_TYPE, money.type)
        var result = db.insert(TABLE_NAME,null, cv)
        if(result == -1.toLong()){
            Log.i("message", "failed")
            return true
        }else{
            Log.i("message", "success   ")
            return false
        }
    }

    fun editData(money: money) : Boolean{
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_DESC, money.desc)
        cv.put(COL_AMOUNT, money.amount)
        cv.put(COL_DATE, money.date)
        cv.put(COL_TYPE, money.type)
        var result = db.update(TABLE_NAME,cv, "$COL_Trans_ID=?", arrayOf(money.trans_id.toString()))
        return result>0
    }

    fun deleteData(id : Int){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_Trans_ID=?", arrayOf(id.toString()))
    }

    fun readData() : MutableList<money> {
       var list : MutableList<money> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM " + TABLE_NAME
        val result = db.rawQuery(query, null )
        if(result.moveToFirst()){
            do{
                var money = money()
                money.trans_id= result.getString(result.getColumnIndex(COL_Trans_ID)).toInt()
                money.desc= result.getString(result.getColumnIndex(COL_DESC))
                money.amount= result.getString(result.getColumnIndex(COL_AMOUNT)).toFloat()
                money.date= result.getString(result.getColumnIndex(COL_DATE))
                money.type= result.getString(result.getColumnIndex(COL_TYPE))
                list.add(money)
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }
}

