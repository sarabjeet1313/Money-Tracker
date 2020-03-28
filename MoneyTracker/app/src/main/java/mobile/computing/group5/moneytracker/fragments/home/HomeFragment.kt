package mobile.computing.group5.moneytracker.fragments.home

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_home.*
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.model.*
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    var type = ""
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private val requestingLocationUpdates = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback



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

        val geocoder = Geocoder(context, Locale.getDefault())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val obj: Address = addresses[0]
                    var add:String = obj.locality
                    Log.v("IGA", "Address$add")
                    locationText.text=add

                }
            }
        }

        onLoad()

        val listOfMonths = arrayOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )

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

        button_save.setOnClickListener {

            val description = input_description.text.toString()
            val amount = input_amount.text.toString()
            var date = dateText.text.toString()
            var location = locationText.text.toString()

            if(description == "" || amount == ""){
                Toast.makeText(context, "Please enter the fields", Toast.LENGTH_SHORT).show()
            }else if(type == ""){
                Toast.makeText(context, "Please select the type", Toast.LENGTH_SHORT).show()
            }else{

                if(date == "Today"){
                    val sdf = SimpleDateFormat("MMMM dd, yyyy")
                    date = sdf.format(Date())
                }

                val db = DatabaseHelper(activity?.applicationContext!!, null)

                db.insertData(Transaction(description, amount.toFloat(), date, type, location))

                onLoad()

                Toast.makeText(context, "Saved!!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun onLoad() {
        val db = DatabaseHelper(activity?.applicationContext!!, null)
        var income = 0.0
        var expense = 0.0

        val data = db.readData()

        for(i in 0 until data.size){
            Log.i("message", data[i].type)
            if(data[i].type == "Income"){
                income += data[i].amount
            }else{
                expense += data[i].amount
            }
        }

        AmountIncome.text = "$ " + String.format("%.2f", (income)).toDouble().toString()
        AmountExpense.text = "$ " + String.format("%.2f", expense).toDouble().toString()
        AmountBalance.text = "$ " + String.format("%.2f", (income-expense)).toDouble().toString()

        val balance = AmountBalance.text.toString().split(" ").toTypedArray()

        when {
            balance[1].toFloat() < 0.0 -> {
                AmountBalance.setTextColor(Color.parseColor("#ff0000"))
            }
            balance[1].toFloat() > 0.0 -> {
                AmountBalance.setTextColor(Color.parseColor("#46A049"))
            }
            else -> {
                AmountBalance.setTextColor(Color.parseColor("#000000"))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!hasLocationPermissions()) {
            requestPermissions()
        } else {
            getAddress()
        }
    }

    private fun hasLocationPermissions(): Boolean {
        return context?.let { checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED &&
                context?.let { checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } == PackageManager.PERMISSION_GRANTED
    }

    private fun getAddress(){

        val geocoder = Geocoder(context, Locale.getDefault())

        fusedLocationClient.lastLocation.addOnSuccessListener {location ->
            if (location != null) {
                val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val obj: Address = addresses[0]
                var add: String = obj.getAddressLine(0)
                add= add.substringBefore(",")

                Log.v("IGA", "Address$add")
                locationText.text=add
            }else{
                createLocationRequest()
                startLocationUpdates()
            }
        }
    }

    private fun requestPermissions() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != REQUEST_PERMISSIONS_REQUEST_CODE) return
        if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getAddress()
        }
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}