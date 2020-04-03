package mobile.computing.group5.moneytracker.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 *  DATABASE for the application.
 *  It consists of different functions that are storing, manipulating, and sending the data across amny fragments.
 */
class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?): SQLiteOpenHelper(context,
    DATABASE_NAME,null, 1){

    companion object {
        const val DATABASE_NAME = "transaction_tracker"
        const val TABLE_NAME = "transaction_table"
        const val COL_Trans_ID = "transaction_ID"
        const val COL_DESC = "Description"
        const val COL_AMOUNT = "Amount"
        const val COL_DATE = "Date"
        const val COL_TYPE = "Type"
        const val COL_LOCATION ="Location"
        const val COL_IMG = "Image"
    }

    //function to create table
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_Trans_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DESC + " VARCHAR(256), " +
                COL_AMOUNT + " DECIMAL(10,2), " +
                COL_DATE + " DATE, " +
                COL_TYPE + " VARCHAR(256), " +
                COL_LOCATION + " VARCHAR(256), " +
                COL_IMG + " BLOB);"
        db?.execSQL(createTable) //querying database to create table
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun clearTable(){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

    //function to insert data into database
    fun insertData(transaction: Transaction) : Boolean{
        val db = this.writableDatabase
        val cv = ContentValues()

        //setting values of columns
        cv.put(COL_DESC, transaction.desc)
        cv.put(COL_AMOUNT, transaction.amount)
        cv.put(COL_DATE, transaction.date)
        cv.put(COL_TYPE, transaction.type)
        cv.put(COL_LOCATION, transaction.location)
        cv.put(COL_IMG, transaction.image)

        val result = db.insert(TABLE_NAME,null, cv) //inserting data in database

        return result != -1.toLong()
    }

    //function to update data
    fun updateData(transaction: Transaction, id: Int) : Boolean{
        val db = this.writableDatabase
        val cv = ContentValues()

        //setting updated values of columns
        cv.put(COL_DESC, transaction.desc)
        cv.put(COL_AMOUNT, transaction.amount)
        cv.put(COL_DATE, transaction.date)
        cv.put(COL_TYPE, transaction.type)
        cv.put(COL_LOCATION, transaction.location)
        cv.put(COL_IMG, transaction.image)

        //updating table data of required record
        val result = db.update(TABLE_NAME,cv, "$COL_Trans_ID=?", arrayOf(id.toString()))

        return result > 0
    }

    //function to delete record from table
    fun deleteData(id : Int){
        val db = this.writableDatabase
        //deleting record from table
        db.delete(TABLE_NAME, "$COL_Trans_ID=?", arrayOf(id.toString()))
    }

    //reading data from database
    fun readData() : MutableList<Transaction> {
        val list : MutableList<Transaction> = ArrayList() //list that will contain objects of type transaction
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME" //query string to fetch all data from database
        val result = db.rawQuery(query, null ) //querying database and getting result

        //parsing result
        if(result.moveToFirst()){
            do{
                val transaction = Transaction()
                transaction.trans_id = result.getString(result.getColumnIndex(COL_Trans_ID)).toInt() //getting transaction id
                transaction.desc = result.getString(result.getColumnIndex(COL_DESC)) //getting description
                transaction.amount = result.getString(result.getColumnIndex(COL_AMOUNT)).toFloat() //getting amount
                transaction.date = result.getString(result.getColumnIndex(COL_DATE)) //getting date
                transaction.type = result.getString(result.getColumnIndex(COL_TYPE))  //getting type
                transaction.location = result.getString(result.getColumnIndex(COL_LOCATION))  //getting location
                transaction.image = result.getBlob(result.getColumnIndex(COL_IMG))
                list.add(transaction) //adding transaction object into list
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list //returning list of transaction objects
    }

    // return data fro specific transction id
    fun viewData(id: Int) : Transaction {
        val transaction = Transaction()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_Trans_ID = $id"
        val result = db.rawQuery(query, null )
        if(result.moveToFirst()){
            transaction.desc = result.getString(result.getColumnIndex(COL_DESC))
            transaction.amount = result.getString(result.getColumnIndex(COL_AMOUNT)).toFloat()
            transaction.date = result.getString(result.getColumnIndex(COL_DATE))
            transaction.type = result.getString(result.getColumnIndex(COL_TYPE))
            transaction.location = result.getString(result.getColumnIndex(COL_LOCATION))
            transaction.image = result.getBlob(result.getColumnIndex(COL_IMG))
        }
        result.close()
        db.close()
        return transaction
    }

    // return total amount where type = expense
    fun getTotalExpense() : Float{
        val db = this.writableDatabase
        val sqlCalculate: String =
            "SELECT SUM($COL_AMOUNT) FROM $TABLE_NAME WHERE $COL_TYPE = 'Expense';"
        val c  = db.rawQuery(sqlCalculate, null)
        c.moveToFirst()
        val sum = c.getFloat(0)
        c.close()
        return sum
    }

    // return total amount where type = income
    fun getTotalIncome() : Float{
        val db = this.writableDatabase
        val sqlCalculate:String =
            "SELECT SUM($COL_AMOUNT) FROM $TABLE_NAME WHERE $COL_TYPE = 'Income';"
        val c  = db.rawQuery(sqlCalculate, null)
        c.moveToFirst()
        val sum = c.getFloat(0)
        c.close()
        return sum
    }

    // return total number of rows or transactions we have
    fun getTotalItems() : Int{
        val db = this.writableDatabase
        val sqlCalculate: String = "SELECT COUNT(*) FROM $TABLE_NAME ;"
        val c  = db.rawQuery(sqlCalculate, null)
        c.moveToFirst()
        val total = c.getInt(0)
        c.close()
        return total
    }
}

