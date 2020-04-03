package mobile.computing.group5.moneytracker.fragments.transaction

import android.content.Context
import android.graphics.BitmapFactory
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
import kotlinx.android.synthetic.main.fragment_transaction_view.*
import kotlinx.android.synthetic.main.fragment_view_image.*
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.model.DatabaseHelper
import mobile.computing.group5.moneytracker.model.Transaction

/**
 *  main class for view transaction fragment to inflate fragment_transaction_view.xml
 */
class ViewTransactionFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = mInflater.inflate(R.layout.fragment_transaction_view, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lateinit var result: Transaction

        val tid = arguments?.getInt("id")
        Log.i("message", tid.toString())

        val db = DatabaseHelper(activity?.applicationContext!!, null)

        // if id is not null, fetch the transaction from database using transaction id
        if (tid != null) {
            result = db.viewData(tid)
        }

        description.text = result.desc
        amount.text = result.amount.toString()
        dateText.text = result.date
        type.text = result.type
        val temp = result.image
        val location = result.location

        // handling image
        if(temp != null){
            imageText.text = "Image Selected"
            val image = temp?.size?.let { BitmapFactory.decodeByteArray(temp, 0, it) }
            button_image.setImageBitmap(image)
        }

        // handling location
        if(location == ""){
            locationText.text = "No location selected"
        }else{
            locationText.text = location
        }

        // handling edit click listener
        button_edit.setOnClickListener {
            val bundle = bundleOf("id" to tid)
            findNavController().navigate(R.id.action_navigation_view_to_navigation_edit, bundle)
        }

        // handling delete click listener
        button_delete.setOnClickListener {

            if (tid != null) {
                db.deleteData(tid)
            }
            Toast.makeText(context, "Transaction Deleted", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_navigation_view_to_navigation_transactions)
        }

        // handling click listener on image
        button_image.setOnClickListener {
            val bundle = bundleOf("id" to tid)
            findNavController().navigate(R.id.action_navigation_view_to_navigation_view_image, bundle)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == 16908332){     // back button functionality
            findNavController().navigate(R.id.action_navigation_view_to_navigation_transactions)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}