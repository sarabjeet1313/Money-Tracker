package mobile.computing.group5.moneytracker.fragments.aboutUs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import mobile.computing.group5.moneytracker.R

class AboutUsFragment : Fragment() {

    private lateinit var aboutusViewModel: AboutUsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        aboutusViewModel =
            ViewModelProviders.of(this).get(AboutUsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_aboutus, container, false)
        aboutusViewModel.text.observe(this, Observer {
            Log.i("message", it)
        })
        return root
    }

}