package com.example.homework2

import org.jetbrains.anko.db.MapRowParser

class  Customer(var id:Int?,var year:Int?,var month:Int?,var day:Int?,var calorie:Int?){
    override fun toString(): String {
        return "{id:"+id+"year:"+year+",month:"+month+",day:"+day+",calorie:"+calorie+"}"
    }
}

class Customers:MapRowParser<Customer>{
    override fun parseRow(columns: Map<String, Any?>): Customer {
        return Customer(columns["id"].toString().toInt(),columns["year"].toString().toInt(),columns["month"].toString().toInt(),columns["day"] .toString().toInt(),columns["calorie"] .toString().toInt())


    }
}

fun main(args: Array<String>){
}
