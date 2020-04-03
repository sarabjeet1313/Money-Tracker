package mobile.computing.group5.moneytracker.model

// data class or model for the application
class Transaction{
    var trans_id : Int = 0
    var desc : String = ""
    var amount : Float = 0.0f
    var date : String = "Today"
    var type : String = "Expense"
    var location : String = ""
    var image: ByteArray? = null
    
    constructor( trans_id: Int, desc: String, amount: Float, date: String, type: String, location: String, image: ByteArray){
        this.trans_id = trans_id
        this.desc = desc
        this.amount = amount
        this.date = date
        this.type = type
        this.location = location
        this.image = image
    }
    constructor( desc: String, amount: Float, date: String, type: String, location: String, image: ByteArray){
        this.desc = desc
        this.amount = amount
        this.date = date
        this.type = type
        this.location = location
        this.image = image
    }
    constructor( desc: String, amount: Float, date: String, type: String, location: String){
        this.desc = desc
        this.amount = amount
        this.date = date
        this.type = type
        this.location = location
    }
    constructor( desc: String, amount: Float, date: String, type: String){
        this.desc = desc
        this.amount = amount
        this.date = date
        this.type = type
    }

    constructor(){}

}