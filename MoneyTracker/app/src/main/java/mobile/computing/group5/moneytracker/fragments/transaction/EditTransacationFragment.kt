package mobile.computing.group5.moneytracker.fragments.transaction

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_transaction_edit.*
import kotlinx.android.synthetic.main.fragment_transaction_edit.button_calender
import kotlinx.android.synthetic.main.fragment_transaction_edit.dateText
import kotlinx.android.synthetic.main.fragment_transaction_edit.locationText
import kotlinx.android.synthetic.main.fragment_transaction_view.*
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.model.DatabaseHelper
import mobile.computing.group5.moneytracker.model.Transaction
import java.io.ByteArrayOutputStream
import java.util.*

class EditTransactionFragment: Fragment() {

    var tid: Int = 0
    private val CODECAM = 0
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

                if(byteArray != null){
                    db.updateData(Transaction(description, amount.toFloat(), date, type, location,
                        byteArray!!
                    ), tid)
                }else{
                    db.updateData(Transaction(description, amount.toFloat(), date, type, location), tid)
                }

                byteArray?.let { it1 ->
                    Transaction(description, amount.toFloat(), date,
                        type, location,
                        it1
                    )
                }?.let { it2 -> db.updateData(it2, tid) }
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
}