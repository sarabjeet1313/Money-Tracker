package mobile.computing.group5.moneytracker.model

class Transaction{
    var trans_id : Int = 0
    var desc : String = ""
    var amount : Float = 0.0f
    var date : String = "Today"
    var type : String = "Expense"
    var location : String = ""

    
    constructor( trans_id:Int,desc: String, amount: Float, date: String, type: String, location: String){
        this.trans_id=trans_id
        this.desc=desc
        this.amount=amount
        this.date = date
        this.type= type
        this.location=location
    }
    constructor( desc: String, amount: Float, date: String, type: String, location:String){
        this.desc=desc
        this.amount=amount
        this.date = date
        this.type= type
        this.location=location
    }
    constructor(){}

}