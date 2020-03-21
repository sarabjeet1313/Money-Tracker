package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sqlitelearn1.model.DatabaseHelper
import kotlinx.android.synthetic.main.fragment_view_record.*

class ViewRecord : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_view_record)
        viewrecord_delete.setOnClickListener {
            startActivity(Intent(this@ViewRecord,MainActivity::class.java))
        }
        viewrecord_edit.setOnClickListener {
            startActivity(Intent(this@ViewRecord,MainActivity::class.java))
        }
        val context=this
        var db=DatabaseHelper(this,null)

      //  val actionBar=supportActionBar
        supportActionBar!!.title="View Record"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
       // var dbHandler = DatabaseHelper(this, null)
      //  var data=dbHandler.readData()
        var data=db.readData()
        var i=3
        var amt1: Float= 0.0F
        var amt2=" "
      //  for(i in 0..(data.size-1)) {
            viewrecord_description.append(data[i].desc)
            amt1 = (data[i].amount)
            //  amt2=Float.toString(amt1)
            viewrecord_amount.append(amt1.toString())
            viewrecord_type.append(data[i].type)
            viewrecord_date.append(data[i].date)
     //   }
      //  actionBar!!.title="View Record"
      //  actionBar.setDefaultDisplayHomeAsUpEnabled(true)
        //actionBar.setDefaultDisplayHomeAsUpEnabled(true)


    }
  /*  override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true


    }*/
}
