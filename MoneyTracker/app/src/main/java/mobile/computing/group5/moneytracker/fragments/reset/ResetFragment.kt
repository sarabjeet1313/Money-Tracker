package mobile.computing.group5.moneytracker.fragments.reset

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_reset.*
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.model.DatabaseHelper


class ResetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = mInflater.inflate(R.layout.fragment_reset, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_yes.setOnClickListener {
            val db = DatabaseHelper(activity?.applicationContext!!, null)
            var message = ""
            message = if(db.readData().size == 0){
                "No transaction present!!"
            }else{
                db.clearTable()
                "Transactions Cleared!!"
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_navigation_reset_to_navigation_home)
        }

        button_no.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_reset_to_navigation_settings)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == 16908332){
            findNavController().navigate(R.id.action_navigation_reset_to_navigation_settings)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}