package com.example.homework2



import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.second_activity.*
import kotlinx.android.synthetic.main.thrid_activity.*
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import java.util.*

class ThridActivity:AppCompatActivity(){

    val database: MySqlHelper
        get() = MySqlHelper.getInstance(applicationContext)
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var year1: Int = 0
    private var month1: Int = 0
    private var day1: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.thrid_activity)

        println("跳转进入thrid页面")
        //接受mainactivity传过来的数据
//        var result = intent.getStringExtra("key")
//        text.text=result

        btn_date.setOnClickListener {

            val c= Calendar.getInstance()
            mYear=c.get(Calendar.YEAR)
            mMonth=c.get(Calendar.MONTH)
            mDay=c.get(Calendar.DATE)

            val datePicker=DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener {
                        view, year, month, dayOfMonth ->
                    year1=year
                    month1=month
                    day1=dayOfMonth
                    tx_date.text=year1.toString()+"-"+(month1+1).toString()+"-"+day1.toString()


                },mYear,mMonth,mDay)
            datePicker.show()
        }
        btn_submit.setOnClickListener{
            var calorie:Int = tx_calorie.text.toString().toInt()
//            var  date=tx_date.text.toString()
            println("year :"+year1)
            println("month :"+month1)
            println("day :"+day1)
            //把获取到的数据写入数据库
            var result=database.use {
                delete("Customer1")
                insert("Customer1",
                   "year" to year1,
                    "month" to month1,
                    "day" to day1,
                    "calorie" to calorie
               )
           }
//            println("result"+result)
//            val intent=Intent(this,SecondActivity::class.java)
//            startActivity(intent)
            finish()
        }


    }

}