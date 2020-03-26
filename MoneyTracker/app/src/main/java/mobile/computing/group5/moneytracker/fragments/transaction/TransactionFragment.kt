package mobile.computing.group5.moneytracker.fragments.transaction

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_transaction.*
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.fragments.home.HomeViewModel

class TransactionFragment : Fragment() {

    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        transactionViewModel =
            ViewModelProviders.of(this).get(TransactionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_transaction, container, false)
        transactionViewModel.text.observe(this, Observer {
            Log.i("message", it)
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goahead.setOnClickListener {
            var bundle = bundleOf("id" to 1)
            findNavController().navigate(R.id.action_navigation_transactions_to_navigation_view, bundle)
        }

    }


}