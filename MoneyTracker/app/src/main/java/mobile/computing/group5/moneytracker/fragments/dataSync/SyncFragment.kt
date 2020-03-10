package mobile.computing.group5.moneytracker.fragments.dataSync

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import mobile.computing.group5.moneytracker.R

class SyncFragment : Fragment() {

    private lateinit var syncViewModel: SyncViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        syncViewModel =
            ViewModelProviders.of(this).get(SyncViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_datasync, container, false)
        syncViewModel.text.observe(this, Observer {
            Log.i("message", it)
        })
        return root
    }

}