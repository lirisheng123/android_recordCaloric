package com.example.homework2

import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.RowParser



class  Customer(var year:Int?,var month:Int?,var day:Int?,var calorie:Int?){
    override fun toString(): String {
        return "{year:"+year+",month:"+month+",day:"+day+",calorie:"+calorie+"}"
    }
}

class Customers:MapRowParser<Customer>{
    override fun parseRow(columns: Map<String, Any?>): Customer {
        return Customer(columns["year"].toString().toInt(),columns["month"].toString().toInt(),columns["day"] .toString().toInt(),columns["calorie"] .toString().toInt())
//       TODO("not implemented") To change body of created functions use File | Settings | File Templates.
//    public inline fun TODO(): Nothing = throw NotImplementedError()

    }
}

fun main(args: Array<String>){
//    var customer:Customer=Customer();
}
