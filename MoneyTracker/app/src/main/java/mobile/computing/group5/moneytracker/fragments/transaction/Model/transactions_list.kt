package mobile.computing.group5.moneytracker.fragments.transaction.Model

/**
 * data class or local model for transactions
 */
data class transactions_list (

    var date: String,
    var description: String,
    var amount: String,
    var id: Int,
    var type: String
)