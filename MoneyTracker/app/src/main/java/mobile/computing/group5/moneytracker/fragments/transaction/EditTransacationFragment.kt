package mobile.computing.group5.moneytracker.fragments.transaction

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_transaction_edit.*
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.model.DatabaseHelper
import mobile.computing.group5.moneytracker.model.Transaction
import java.util.*

class EditTransactionFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaction_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lateinit var result: List<String>

        val id = arguments?.getInt("id")

        val db = DatabaseHelper(activity?.applicationContext!!, null)

        if (id != null) {
            result = db.viewData(id).split("@")
        }

        input_description.setText(result[0])
        input_amount.setText(result[1])
        dateText.text = result[2]
        var type = result[3]
        var location = result[4]

        locationText.setText(location)

        if(type == "Income"){
            radioIncome.isChecked = true
        }else{
            radioExpense.isChecked = true
        }

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

                if (id != null) {
                    db.updateData(Transaction(description, amount.toFloat(), date, type, location), id)
                }
                Toast.makeText(context, "Updated!!", Toast.LENGTH_SHORT).show()
            }
            findNavController().navigate(R.id.action_navigation_edit_to_navigation_transactions)
        }
    }

}