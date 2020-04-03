package mobile.computing.group5.moneytracker.fragments.home

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_home.*
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.model.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * main class for home fragment to inflate the fragment_home.xml
 */
class HomeFragment : Fragment() {

    // local private variables initialization
    private val LOCATION_CODE = 34
    private val CAMERA_CODE = 21
    private val CODECAM = 0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var path: Bitmap? = null
    private var byteArray: ByteArray? = null
    var type = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = context?.let { it1 ->
            LocationServices.getFusedLocationProviderClient(
                it1
            )
        }!!

        // handling the location functionality
        val geocoder = Geocoder(context, Locale.getDefault())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val obj: Address = addresses[0]
                    val add:String = obj.locality
                    locationText.text = add
                }
            }
        }

        onLoad()

        // handling the calendar functionality
        val listOfMonths = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // open up the calendar on click of calendar icon
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

        // open up camera on click of camera icon
        button_image.setOnClickListener{
            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_CODE)
            }else{
                openCamera()
            }
        }

        // handling the income and expense option
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

        // handling click listener on delete button
        button_delete.setOnClickListener {
            byteArray = null
            imageText.text = "No image selected"
            button_image.setImageBitmap(null)
            button_image.setBackgroundResource(R.drawable.ic_camera_alt_black_24dp)
            button_delete.visibility = View.INVISIBLE
        }

        // handling click listener on save button
        button_save.setOnClickListener {

            val description = input_description.text.toString()
            val amount = input_amount.text.toString()
            var date = dateText.text.toString()
            val location = locationText.text.toString()

            // error handling for specific fields.
            if(description == "" || amount == ""){
                Toast.makeText(context, "Please enter the fields", Toast.LENGTH_SHORT).show()
            }else if(type == ""){
                Toast.makeText(context, "Please select the type", Toast.LENGTH_SHORT).show()
            }else{

                if(date == "Today"){
                    val sdf = SimpleDateFormat("MMMM dd, yyyy")
                    date = sdf.format(Date())
                }

                // handling database insertion operation.
                val db = DatabaseHelper(activity?.applicationContext!!, null)

                if(byteArray == null){
                    db.insertData(Transaction(description, amount.toFloat(), date, type, location))
                }else{
                    db.insertData(Transaction(description, amount.toFloat(), date, type, location, byteArray!!))
                }

                onLoad()

                // success status to user.
                Toast.makeText(context, "Saved!!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    // overriding onStart to get permission and address.
    override fun onStart() {
        super.onStart()
        if (!hasLocationPermissions()) {
            requestPermissions()
        } else {
            getAddress()
        }
    }

    // overriding onPause to stop location updates.
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    // overriding onRequestPermissionsResult to allow Camera and Location services.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if(requestCode == CAMERA_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }else if(requestCode == LOCATION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAddress()
            }
        }else{
            return
        }
    }

    // overriding onActivityResult to execute camera functionalities
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

    // private method to handle open camera context.
    private fun openCamera(){
        val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(callCameraIntent,CODECAM)
    }

    // private method to execute after loading.
    private fun onLoad() {
        val db = DatabaseHelper(activity?.applicationContext!!, null)
        var income = 0.0
        var expense = 0.0

        val data = db.readData()

        for(i in 0 until data.size){
            if(data[i].type == "Income"){
                income += data[i].amount
            }else{
                expense += data[i].amount
            }
        }

        // updating the summary content.
        AmountIncome.text = "$ " + String.format("%.2f", (income)).toDouble().toString()
        AmountExpense.text = "$ " + String.format("%.2f", expense).toDouble().toString()
        AmountBalance.text = "$ " + String.format("%.2f", (income-expense)).toDouble().toString()

        val balance = AmountBalance.text.toString().split(" ").toTypedArray()

        when {
            balance[1].toFloat() < 0.0 -> {
                AmountBalance.setTextColor(Color.parseColor("#F44336"))
            }
            balance[1].toFloat() > 0.0 -> {
                AmountBalance.setTextColor(Color.parseColor("#46A049"))
            }
            else -> {
                AmountBalance.setTextColor(Color.parseColor("#000000"))
            }
        }

        input_description.setText("")
        input_amount.setText("")
        radioExpense.isChecked = false
        radioIncome.isChecked = false
        dateText.text = "Today"
        byteArray = null
        imageText.text = "No image selected"
        button_image.setImageBitmap(null)
        button_image.setBackgroundResource(R.drawable.ic_camera_alt_black_24dp)
        button_delete.visibility = View.INVISIBLE
    }

    // private method to check location permissions
    private fun hasLocationPermissions(): Boolean {
        return context?.let { checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED &&
                context?.let { checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } == PackageManager.PERMISSION_GRANTED
    }

    // private method to fetch address
    private fun getAddress(){

        val geocoder = Geocoder(context, Locale.getDefault())

        fusedLocationClient.lastLocation.addOnSuccessListener {location ->
            if (location != null) {
                val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val obj: Address = addresses[0]
                var add: String = obj.getAddressLine(0)
                add= add.substringBefore(",")
                locationText.text = add
            }else{
                createLocationRequest()
                startLocationUpdates()
            }
        }
    }

    // private method for requesting location permissions
    private fun requestPermissions() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_CODE)
    }

    // private method to create location request
    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    // private method to start location update
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    // private method to stop location update
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}