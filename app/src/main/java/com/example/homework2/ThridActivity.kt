package com.example.homework2



import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.second_activity.*
import kotlinx.android.synthetic.main.thrid_activity.*
import org.jetbrains.anko.db.*
import java.text.SimpleDateFormat
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

//            val c= Calendar.getInstance()
//            mYear=c.get(Calendar.YEAR)
//            mMonth=c.get(Calendar.MONTH)
//            mDay=c.get(Calendar.DATE)

        btn_submit.setOnClickListener{
            var calorie:Int = tx_calorie.text.toString().toInt()
            println("year :"+year1)
            println("month :"+month1)
            println("day :"+day1)
            //把获取到的数据写入数据库
            var result=database.use {
                insert("Customer2",
                   "year" to year1,
                    "month" to month1,
                    "day" to day1,
                    "calorie" to calorie
               )
           }
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
            //销毁该页，就是不保存该页面在栈中（等同于手机的返回键）
            finish()
        }

        calendarView.setOnDateChangeListener(
            CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
                Toast.makeText(
                 this,
                    year.toString() + "年" + month + "月" + dayOfMonth + "日",
                    Toast.LENGTH_SHORT
                ).show()
                getNow()
                year1=year
                month1=month
                day1=dayOfMonth

            }
        )
    }

    fun getNow() {
        var tms = Calendar.getInstance()
        year1= tms.get(Calendar.YEAR)
        month1=tms.get(Calendar.MONTH)
        day1= tms.get(Calendar.HOUR_OF_DAY)
    }



}