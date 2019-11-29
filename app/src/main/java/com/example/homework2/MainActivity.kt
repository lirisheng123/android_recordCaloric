package com.example.homework2

import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


import java.util.*
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import java.text.DateFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.icu.text.DateFormat.getDateInstance
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.thrid_activity.*
import org.jetbrains.anko.db.select
import java.time.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    val database: MySqlHelper
        get() = MySqlHelper.getInstance(applicationContext)
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var beginDate:Date=Date()
    private var endDate:Date=Date()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("进入主页面")
        btn_edit.setOnClickListener(this)
        btn_begin.setOnClickListener {
            val c = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DATE)
            val datePicker = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                    c.set(year,month,dayOfMonth)
                    beginDate=c.time
                    btn_begin.text =
                        year.toString() + "-" + (month+1).toString() + "-" + dayOfMonth.toString()


                }, mYear, mMonth, mDay
            )
            datePicker.show()
        }
        btn_end.setOnClickListener {
            val c = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DATE)
            val datePicker = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    c.set(year,month,dayOfMonth)
                    endDate=c.time
                    btn_end.text =
                        year.toString() + "-" +( month+1).toString() + "-" + dayOfMonth.toString()

                }, mYear, mMonth, mDay
            )
            datePicker.show()
        }
        btn_query.setOnClickListener {

            println("beginDate:" + beginDate)
            println("endDate:" + endDate)
            //读出数据库中的数据，并把其展示在图形上
            var result = database.use {
                select("Customer1", "year", "month","day","calorie").parseList(Customers())
            }
            println("mainactivity"+result)
            var c=Calendar.getInstance()
            var date:Date=Date()
//            var dataPoins: Array<DataPoint?> = arrayOfNulls<DataPoint>(1)
            var dataPoints:MutableList<DataPoint> =mutableListOf<DataPoint>()
            var count=0
            for (customer: Customer in result) {
                println("customer:"+customer)
                if(customer.year!=0&&customer.month!=0&&customer.day!=0) {
                    c.set(customer.year!!,(customer.month!!.minus(1)),customer.day!!)
                    date = c.time
                }
                println("Date:"+date)
                if (beginDate <= date && date <= endDate) {
                    count++
                    println("rangeDate:"+date)
                    var dataPoint: DataPoint = DataPoint(date, customer.calorie!!.toDouble())
                    dataPoints.add(dataPoint)
                }
            }
            var series:LineGraphSeries<DataPoint?> =getSeries(beginDate,endDate,dataPoints)
            graph.addSeries(series)

        }
//        calendar.set(2011,2,1)
//        var date:Date=calendar.time
//        println(calendar.time)
//        println(date)

    }

    override fun onClick(v: View?) {
        val intent = Intent(this, SecondActivity::class.java)

        //传参数给secondactivity，在另一个页面通过“key”来获取“ok”值
//        intent.putExtra("key","ok")
        startActivity(intent)
    }

    fun getSeries(
        beginDate: Date,
        endDate: Date,
        dataPoints: MutableList<DataPoint>
    ): LineGraphSeries<DataPoint?> {

         val calendar = Calendar.getInstance()
//        val d1= LocalDate.parse(string, DateTimeFormatter.ISO_DATE)
          val d1 = calendar.time
//       val d1:Date=Date()
//        d1.time=transToTimeStamp("2019-01-02")
//        println("d1:"+d1)
//        calendar.add(Calendar.DATE, 1)
//        val d2 = calendar.getTime()
//        calendar.add(Calendar.DATE, 1)
//        val d3 = calendar.getTime()
//
//        val sdf=SimpleDateFormat("YY-MM-DD")
//        val d4:Date?= sdf.parse("2019-10-11")
//        println("d4:"+d4)
//
//          println("d1:"+d1)
//        println("d2:"+d2)
//        println("d3:"+d3)
        println("beginDate"+beginDate)
        println("endDate:"+endDate)
//        var datePoint=DataPoint(d1,20.0)
//        println(datePoint)
//        println(dataPoints[0])
//         val series = LineGraphSeries(arrayOf(datePoint))
        var array:Array<DataPoint?> = arrayOfNulls(dataPoints.size)
        for(index in 0..dataPoints.size-1){
              array[index]=dataPoints[index]
        }
        val series = LineGraphSeries(array)
//        val series = LineGraphSeries(arrayOf(dataPoints[0]))


// set date label formatter
        graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this)
        graph.gridLabelRenderer.numHorizontalLabels = 3 // only 4 because of the space

        //注意这里，问题很大
        graph.getGridLabelRenderer().setHumanRounding(false)

// set manual x bounds to have nice steps
        graph.viewport.setMinX(beginDate.time.toDouble())
        graph.viewport.setMaxX(endDate.time.toDouble())
        graph.viewport.isXAxisBoundsManual = true
//        graph.addSeries(getSeries())

        return series
    }


}
