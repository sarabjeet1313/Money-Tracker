package mobile.computing.group5.moneytracker.fragments.more

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import mobile.computing.group5.moneytracker.R

class MoreFragment : Fragment() {

    private lateinit var moreViewModel: MoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moreViewModel =
            ViewModelProviders.of(this).get(MoreViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_more, container, false)
        moreViewModel.text.observe(this, Observer {
            Log.i("message", it)
        })
        return root
    }
}