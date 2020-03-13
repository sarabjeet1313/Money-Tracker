package mobile.computing.group5.moneytracker.fragments.transaction.Adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.translationMatrix
import kotlinx.android.synthetic.main.item_transaction.view.*
import mobile.computing.group5.moneytracker.fragments.transaction.Model.transactions
import mobile.computing.group5.moneytracker.R


class transactionAdapter(private val Transactions: List<transactions>)
    : RecyclerView.Adapter<transactionAdapter.ViewHolder>() {

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

        fun bind(transaction : transactions) {
            itemView.date.text = transaction.date
            itemView.description.text = transaction.description
            itemView.amount.text = "$ ${transaction.amount}"
        }
    }
}