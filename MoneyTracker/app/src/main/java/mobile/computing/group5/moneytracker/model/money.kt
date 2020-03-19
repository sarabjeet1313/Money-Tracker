package mobile.computing.group5.moneytracker.model

class money{
    var trans_id : Int = 0
    var desc : String = ""
    var amount : Float = 0.0f
    var date : String = "Today"
    var type : String = "Expense"

    constructor( trans_id:Int,desc: String, amount: Float, date: String, type: String){
        this.trans_id=trans_id
        this.desc=desc
        this.amount=amount
        this.date = date
        this.type= type
    }
    constructor( desc: String, amount: Float, date: String, type: String){
        this.desc=desc
        this.amount=amount
        this.date = date
        this.type= type
    }
    constructor(){}

}