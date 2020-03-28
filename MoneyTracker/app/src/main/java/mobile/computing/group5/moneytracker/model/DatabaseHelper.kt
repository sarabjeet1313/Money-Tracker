package mobile.computing.group5.moneytracker.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?): SQLiteOpenHelper(context,
    DATABASE_NAME,null, 1){

    companion object {
        val DATABASE_NAME = "transactiontracker"
        val TABLE_NAME = "transaction_table"
        val COL_Trans_ID = "Trans_ID"
        val COL_DESC = "Desc"
        val COL_AMOUNT = "Amount"
        val COL_DATE = "Date"
        val COL_TYPE = "Type"
        val COL_LOCATION="Location"
    }

    //function to create table
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_Trans_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DESC + " VARCHAR(256), " +
                COL_AMOUNT + " DECIMAL(10,2), " +
                COL_DATE + " DATE, " +
                COL_TYPE + " VARCHAR(256), " +
                COL_LOCATION + " VARCHAR(256));"
        db?.execSQL(createTable) //querying database to create table
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    //function to insert data into database
    fun insertData(transaction: Transaction) : Boolean{
        val db = this.writableDatabase
        var cv = ContentValues()
        //setting values of columns
        cv.put(COL_DESC, transaction.desc)
        cv.put(COL_AMOUNT, transaction.amount)
        cv.put(COL_DATE, transaction.date)
        cv.put(COL_TYPE, transaction.type)
        cv.put(COL_LOCATION, transaction.location)
        var result = db.insert(TABLE_NAME,null, cv) //inserting data in database
        if(result == -1.toLong()){
            Log.i("message", "failed")
            return false  //returning false on failing to store data in database
        }else{
            Log.i("message", "success   ")
            return true //returning true on successfully storing data in database
        }
    }

    //function to update data
    fun updateData(transaction: Transaction, id: Int) : Boolean{
        val db = this.writableDatabase
        var cv = ContentValues()
        //setting updated values of columns
        cv.put(COL_DESC, transaction.desc)
        cv.put(COL_AMOUNT, transaction.amount)
        cv.put(COL_DATE, transaction.date)
        cv.put(COL_TYPE, transaction.type)
        cv.put(COL_LOCATION, transaction.location)
        //updating table data of required record
        var result = db.update(TABLE_NAME,cv, "$COL_Trans_ID=?", arrayOf(id.toString()))
        return result>0
    }

    //function to delete record from table
    fun deleteData(id : Int){
        val db = this.writableDatabase
        //deleting record from table
        db.delete(TABLE_NAME, "$COL_Trans_ID=?", arrayOf(id.toString()))
    }

    //reading data from database
    fun readData() : MutableList<Transaction> {
       var list : MutableList<Transaction> = ArrayList() //list that will contain objects of type transaction
        val db = this.readableDatabase
        val query = "SELECT * FROM " + TABLE_NAME //query string to fetch all data from database
        val result = db.rawQuery(query, null ) //querying database and getting result
        //parsing result
        if(result.moveToFirst()){
            do{
                var transaction = Transaction()
                transaction.trans_id= result.getString(result.getColumnIndex(COL_Trans_ID)).toInt() //getting transaction id
                transaction.desc= result.getString(result.getColumnIndex(COL_DESC)) //getting description
                transaction.amount= result.getString(result.getColumnIndex(COL_AMOUNT)).toFloat() //getting amount
                transaction.date= result.getString(result.getColumnIndex(COL_DATE)) //getting date
                transaction.type= result.getString(result.getColumnIndex(COL_TYPE))  //getting type
                transaction.location= result.getString(result.getColumnIndex(COL_LOCATION))  //getting location
                list.add(transaction) //adding transaction object into list
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list //returning list of transaction objects
    }

    fun viewData(id: Int) : String {
        lateinit var description: String
        lateinit var amount : String
        lateinit var date : String
        lateinit var type : String
        lateinit var location : String
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_Trans_ID = $id"
        val result = db.rawQuery(query, null )
        if(result.moveToFirst()){
            description = result.getString(result.getColumnIndex(COL_DESC))
            amount = result.getString(result.getColumnIndex(COL_AMOUNT))
            date = result.getString(result.getColumnIndex(COL_DATE))
            type = result.getString(result.getColumnIndex(COL_TYPE))
            location= result.getString(result.getColumnIndex(COL_LOCATION))
        }
        result.close()
        db.close()
        return "$description@$amount@$date@$type@$location"
    }

    fun getTotalExpense() : Float{

        val db = this.writableDatabase
        val sqlcalculate:String = "SELECT SUM(" + COL_AMOUNT + ") FROM " +  TABLE_NAME + " WHERE " +  COL_TYPE + " = 'Expense';"

        val c  = db.rawQuery(sqlcalculate, null)
        c.moveToFirst()
        val sum = c.getFloat(0)
        c.close()
        return sum
    }


    fun getTotalIncome() : Float{

        val db = this.writableDatabase
        val sqlcalculate:String = "SELECT SUM(" + COL_AMOUNT + ") FROM " +  TABLE_NAME + " WHERE " +  COL_TYPE + " = 'Income';"

        val c  = db.rawQuery(sqlcalculate, null)
        c.moveToFirst()
        val sum = c.getFloat(0)
        c.close()
        return sum
    }

    fun getTotalItems() : Int{
        val db = this.writableDatabase
        val sqlcalculate:String = "SELECT COUNT(*) FROM " +  TABLE_NAME + " ;"
        val c  = db.rawQuery(sqlcalculate, null)
        c.moveToFirst()
        val total = c.getInt(0)
        c.close()
        return total
    }
}

