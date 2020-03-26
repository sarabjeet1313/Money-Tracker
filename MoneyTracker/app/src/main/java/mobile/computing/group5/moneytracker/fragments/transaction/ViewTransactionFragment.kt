package mobile.computing.group5.moneytracker.fragments.transaction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_transaction.*
import kotlinx.android.synthetic.main.fragment_transaction_view.*
import mobile.computing.group5.moneytracker.R

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

        var id = arguments?.getInt("id")

        var bundle = bundleOf("id" to id)

        Log.i("message", id.toString())

        button_edit.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_view_to_navigation_edit, bundle)
        }

        button_delete.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_view_to_navigation_transactions)
        }

    }

}