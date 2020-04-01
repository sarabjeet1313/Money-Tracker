package mobile.computing.group5.moneytracker.fragments.transaction

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_transaction_edit.*
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.model.DatabaseHelper
import mobile.computing.group5.moneytracker.model.Transaction
import java.io.ByteArrayOutputStream
import java.util.*

class EditTransactionFragment: Fragment() {

    var tid: Int = 0
    private val CODECAM = 0
    private val CAMERA_CODE = 21
    private var path: Bitmap? = null
    var byteArray: ByteArray? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = mInflater.inflate(R.layout.fragment_transaction_edit, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lateinit var result: Transaction

        tid = arguments?.getInt("id")!!
        val db = DatabaseHelper(activity?.applicationContext!!, null)
        result = db.viewData(tid)

        input_description.setText(result.desc)
        input_amount.setText(result.amount.toString())
        dateText.text = result.date
        var type =  result.type
        byteArray = result.image
        val location = result.location
        locationText.setText(location)

        Log.i("message", byteArray.toString())

        if(byteArray != null){
            imageText.text = "Image Selected"
            val image = byteArray?.size?.let { BitmapFactory.decodeByteArray(byteArray, 0, it) }
            button_image.setImageBitmap(image)
        }

        if(type == "Income"){
            radioIncome.isChecked = true
        }else{
            radioExpense.isChecked = true
        }

        val listOfMonths = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        button_calender.setOnClickListener{
            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        val date = listOfMonths[monthOfYear] + " " + dayOfMonth + ", " + year
                        dateText.text = date
                    },
                    year,
                    month,
                    day
                )
            }
            dpd?.show()
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->

            type = when {
                R.id.radioIncome == checkedId -> {
                    "Income"
                }
                R.id.radioExpense == checkedId -> {
                    "Expense"
                }
                else -> {
                    ""
                }
            }
        }

        button_image.setOnClickListener{
            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_CODE)
            }else{
                openCamera()
            }
        }

        button_delete.setOnClickListener {
            byteArray = null
            imageText.text = "No image selected"
            button_image.setImageBitmap(null)
            button_image.setBackgroundResource(R.drawable.ic_camera_alt_black_24dp)
            button_delete.visibility = View.INVISIBLE
        }

        button_update.setOnClickListener {

            val description = input_description.text.toString()
            val amount = input_amount.text.toString()
            val date = dateText.text.toString()
            val location = locationText.text.toString()

            if(description == "" || amount == ""){
                Toast.makeText(context, "Please enter the fields", Toast.LENGTH_SHORT).show()
            }else if(type == ""){
                Toast.makeText(context, "Please select the type", Toast.LENGTH_SHORT).show()
            }else{

                val db = DatabaseHelper(activity?.applicationContext!!, null)

                if(byteArray == null){
                    db.updateData(Transaction(description, amount.toFloat(), date, type, location), tid)
                }else{
                    db.updateData(Transaction(description, amount.toFloat(), date, type, location, byteArray!!), tid)
                }

                Toast.makeText(context, "Updated!!", Toast.LENGTH_SHORT).show()
            }
            findNavController().navigate(R.id.action_navigation_edit_to_navigation_transactions)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == 16908332){
            val bundle = bundleOf("id" to tid)
            findNavController().navigate(R.id.action_navigation_edit_to_navigation_view, bundle)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if(requestCode == CAMERA_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }else{
            return
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CODECAM ->{
                if(resultCode== Activity.RESULT_OK && data !=null){
                    path = data.extras!!.get("data") as Bitmap
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    path!!.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    byteArray= byteArrayOutputStream.toByteArray()
                    imageText.text = "Image captured"
                    button_image.setImageBitmap(path)
                    button_delete.visibility = View.VISIBLE
                }
            }
            else -> {
                Log.i("message", "Failure")
            }
        }
    }

    private fun openCamera(){
        val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(callCameraIntent,CODECAM)
    }
}