package mobile.computing.group5.moneytracker.fragments.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_more.*
import mobile.computing.group5.moneytracker.R

class MoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_statistics.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_more_to_navigation_statistics)
        }

//        button_profile.setOnClickListener {
//
//        }
//
//        button_data_sync.setOnClickListener {
//
//        }
//
//        button_settings.setOnClickListener {
//
//        }
//
//        button_about.setOnClickListener {
//
//        }
//
//        button_help.setOnClickListener {
//
//        }
    }

}