package mobile.computing.group5.moneytracker.fragments.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_transaction_view.*
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.model.DatabaseHelper

class ViewTransactionFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaction_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lateinit var result: List<String>

        val id = arguments?.getInt("id")

        val db = DatabaseHelper(activity?.applicationContext!!, null)

        if (id != null) {
            result = db.viewData(id).split("@")
        }

        description.text = result[0]
        amount.text = result[1]
        dateText.text = result[2]
        type.text = result[3]

        if(result[4] == ""){
            locationText.text = "No location selected"
        }else{
            locationText.text = result[4]
        }

        button_edit.setOnClickListener {
            val bundle = bundleOf("id" to id)
            findNavController().navigate(R.id.action_navigation_view_to_navigation_edit, bundle)
        }

        button_delete.setOnClickListener {

            if (id != null) {
                db.deleteData(id)
            }
            Toast.makeText(context, "Transaction Deleted", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_navigation_view_to_navigation_transactions)
        }

    }

}