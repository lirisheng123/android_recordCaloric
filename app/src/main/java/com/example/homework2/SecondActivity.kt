package com.example.homework2

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.second_activity.*
import org.jetbrains.anko.db.*
import java.util.*

class SecondActivity:AppCompatActivity(){

    val database: MySqlHelper
        get() = MySqlHelper.getInstance(applicationContext)
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)

        println("跳转进入second页面")
        println("展示添加的记录")


        btn_add.setOnClickListener {
            // 接受mainactivity传过来的数据
            // var result = intent.getStringExtra("key")
            // text.text=result
            val intent = Intent(this, ThridActivity::class.java)
            startActivity(intent)
        }


        var result: List<Customer> = database.use {

            select("Customer1", "year", "month","day","calorie").parseList(Customers())
        }
        println("Customers:" + result)


    }

}