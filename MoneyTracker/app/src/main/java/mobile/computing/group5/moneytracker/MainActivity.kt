package mobile.computing.group5.moneytracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        nav_view.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.navigation_home -> {
//                    Log.i("Activity","I am in HOME")
//                    true
//                }
//                R.id.navigation_transactions -> {
//                    Log.i("Activity","I am in Transactions")
//                    true
//                }
//                R.id.navigation_more -> {
//                    Log.i("Activity","I am in More")
//                    true
//                }
//                else -> false
//
//            }
//        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_transactions, R.id.navigation_more
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
}