package mobile.computing.group5.moneytracker.fragments.transaction

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_view_image.*
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.model.DatabaseHelper
import mobile.computing.group5.moneytracker.model.Transaction

/**
 * main class for view image fragment to inflate fragment_view_image.xml
 */
class ViewImageFragment : Fragment() {

    var tid: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = mInflater.inflate(R.layout.fragment_view_image, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lateinit var result: Transaction
        tid = arguments?.getInt("id")!!

        // fetch the transaction from database using transaction id
        val db = DatabaseHelper(activity?.applicationContext!!, null)
        result = db.viewData(tid)

        val temp = result.image
        val image = temp?.size?.let { BitmapFactory.decodeByteArray(temp, 0, it) }
        imageView.setImageBitmap(image)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == 16908332){  // back button functionality
            val bundle = bundleOf("id" to tid)
            findNavController().navigate(R.id.action_navigation_view_image_to_navigation_view, bundle)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}