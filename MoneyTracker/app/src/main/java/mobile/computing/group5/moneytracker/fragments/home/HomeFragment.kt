package mobile.computing.group5.moneytracker.fragments.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*
import mobile.computing.group5.moneytracker.R
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = context?.let { ArrayAdapter.createFromResource(it, R.array.type_arrays_type_home, R.layout.dropdown_item) }
        adapter?.setDropDownViewResource(R.layout.dropdown_item)
        dropdown_type.adapter = adapter

        val MONTHS = arrayOf(
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
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        val date = MONTHS[monthOfYear] + " " + dayOfMonth + ", " + year
                        dateText.text = date
                    },
                    year,
                    month,
                    day
                )
            }
            if (dpd != null) {
                dpd.show()
            }
        }

        button_save.setOnClickListener {

            val description = input_description.text.toString()
            val amount = input_amount.text.toString()
            var date = dateText.text.toString()
            val type = dropdown_type.selectedItem.toString()

            if(description == "" || amount == ""){
                Toast.makeText(context, "Please enter the fields", Toast.LENGTH_SHORT).show()
            }else{

                // Store to database

                if(date == "Today"){
                    var sdf = SimpleDateFormat("MMMM dd, yyyy")
                    date = sdf.format(Date())
                }

                Toast.makeText(context, "Saved!!", Toast.LENGTH_SHORT).show()
            }

        }
    }
}