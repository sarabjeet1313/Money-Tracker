package mobile.computing.group5.moneytracker.fragments.transaction.Adapter

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_transaction.view.*
import kotlinx.android.synthetic.main.item_transaction.view.amount
import kotlinx.android.synthetic.main.item_transaction.view.description
import kotlinx.android.synthetic.main.item_transaction.view.type
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.fragments.transaction.Model.transactions_list

class transaction_listAdapter(private val Transactions: List<transactions_list>)
    : RecyclerView.Adapter<transaction_listAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(holder)
    }

    override fun getItemCount(): Int {
        return Transactions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = Transactions[position]
        holder.bind(transaction)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(transaction : transactions_list) {
            itemView.date.text = transaction.date
            itemView.description.text = transaction.description
            itemView.amount.text = "$ ${transaction.amount}"

            if(transaction.type == "Income"){
                itemView.type.text = "+"
                itemView.type.setTextColor(Color.parseColor("#46A049"))
            }else{
                itemView.type.text = "-"
                itemView.type.setTextColor(Color.parseColor("#F44336"))
            }


            itemView.setOnClickListener {

                val bundle: Bundle? = Bundle()
                if (bundle != null) {
                    Log.d("dbg", "In adapter")
                    bundle.putInt("id", transaction.id)
                }

                findNavController(itemView).navigate(R.id.action_navigation_transactions_to_navigation_view, bundle)
            }
        }
    }
}