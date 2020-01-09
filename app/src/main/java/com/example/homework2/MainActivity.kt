package com.example.homework2

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.util.*
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import android.os.Build
import androidx.annotation.RequiresApi
import com.jjoe64.graphview.helper.StaticLabelsFormatter
import org.jetbrains.anko.db.*
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity(), View.OnClickListener {

    val database: MySqlHelper
        get() = MySqlHelper.getInstance(applicationContext)
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var beginDate: Date = Date()
    private var endDate: Date = Date()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置加载的展示页面
        setContentView(R.layout.activity_main)
        println("进入主页面")

        //设置点击事件，会后面重写的onClick（）函数
        btn_edit.setOnClickListener(this)

        //点击该按钮，弹出一个日期选择器
        btn_begin.setOnClickListener {
            val c = Calendar.getInstance()
            //设置默认的日期
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DATE)
            val datePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    //获取到选择的日期
                    c.set(year, month, dayOfMonth)
                    beginDate = c.time
                    btn_begin.text =
                        year.toString() + "-" + (month + 1).toString() + "-" + dayOfMonth.toString()
                }, mYear, mMonth, mDay
            )
            //把该日期组件展示出来
            datePicker.show()
        }

        btn_end.setOnClickListener {
            val c = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DATE)
            val datePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    c.set(year, month, dayOfMonth)
                    endDate = c.time
                    btn_end.text =
                        year.toString() + "-" + (month + 1).toString() + "-" + dayOfMonth.toString()

                }, mYear, mMonth, mDay
            )
            datePicker.show()
        }

        btn_query.setOnClickListener {
            println("beginDate:" + beginDate)
            println("endDate:" + endDate)
            //读出数据库中的数据，并把其展示在图形上
            var result = database.use {
                select("Customer2", "id", "year", "month", "day", "calorie").parseList(Customers())
            }
            println("mainactivity" + result)
            var c = Calendar.getInstance()
            var date: Date = Date()
            var dataPoints: MutableList<DataPoint> = mutableListOf<DataPoint>()
            for (customer: Customer in result) {
                println("customer:" + customer)
                if (customer.year != 0 && customer.month != 0 && customer.day != 0) {
                    c.set(customer.year!!, (customer.month!!), customer.day!!)
                    date = c.time
                }
                println("Date:" + date)
                if (beginDate <= date && date <= endDate) {
                    println("rangeDate:" + date)
                    var dataPoint: DataPoint = DataPoint(date, customer.calorie!!.toDouble())
                    dataPoints.add(dataPoint)
                }
            }
            var series: LineGraphSeries<DataPoint?> = getSeries(beginDate, endDate, dataPoints)
            graph.addSeries(series)

        }
    }

    //重写的点击事件
    override fun onClick(v: View?) {
        //跳转到SecondActivity页面
        val intent = Intent(this, SecondActivity::class.java)
        //传参数给secondactivity，在另一个页面通过“key”来获取“ok”值
        //intent.putExtra("key","ok")
        startActivity(intent)
    }

    fun getSeries(
        beginDate: Date,
        endDate: Date,
        dataPoints: MutableList<DataPoint>
    ): LineGraphSeries<DataPoint?> {

        val calendar = Calendar.getInstance()
        println("beginDate" + beginDate)
        println("endDate:" + endDate)
        var array: Array<DataPoint?> = arrayOfNulls(dataPoints.size)
        for (index in 0..dataPoints.size - 1) {
            array[index] = dataPoints[index]
        }
        val series = LineGraphSeries(array)

        graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this)
        graph.gridLabelRenderer.numHorizontalLabels = 3 // only 4 because of the space
        graph.gridLabelRenderer.numVerticalLabels = 3
        //注意这里
        graph.getGridLabelRenderer().setHumanRounding(false)
         //set manual x bounds to have nice steps
        graph.viewport.setMinX(beginDate.time.toDouble())
        graph.viewport.setMaxX(endDate.time.toDouble())
        graph.viewport.isXAxisBoundsManual = true
        return series

    }

    fun Date.stringFormat(formatType:String):String{
        return SimpleDateFormat(formatType).format(this)
    }

}
