package com.example.homework2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.second_activity.*
import org.jetbrains.anko.db.*
import java.util.*
import android.widget.LinearLayout

class SecondActivity:AppCompatActivity(){

    val database: MySqlHelper
        get() = MySqlHelper.getInstance(applicationContext)
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var llContentView: LinearLayout? = null
    private var iETContentHeight:Int = 0  // EditText控件高度
    private var fDimRatio = 1.0f // 尺寸比例（实际尺寸/xml文件里尺寸）
    private var listIBTNDel:LinkedList<Button> =LinkedList()
    private var btnIDIndex=-1
    private var savedInstanceState1: Bundle?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState1=savedInstanceState
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)

        println("跳转进入second页面")

        btn_add.setOnClickListener {
            // 接受mainactivity传过来的数据
            // var result = intent.getStringExtra("key")
            // text.text=result
            val intent = Intent(this, ThridActivity::class.java)
            startActivity(intent)
           finish()

        }

        var result: List<Customer> = database.use {

            select("Customer2", "id","year", "month","day","calorie").parseList(Customers())
        }
        initCtrl()
        println("Customers:" + result)
        for (customer:Customer in result){
            addContent(customer)
        }

    }

    public fun initCtrl() {
        llContentView = this.findViewById(R.id.content_view) as LinearLayout
        listIBTNDel = LinkedList<Button>()

    }

    fun  addContent(customer: Customer) {
        // 1.创建外围LinearLayout控件
        var layout: LinearLayout = LinearLayout(this)
        var lLayoutlayoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        // 设置margin
        lLayoutlayoutParams.setMargins(0, (fDimRatio * 5).toInt(), 0, 0);
        layout.setLayoutParams(lLayoutlayoutParams)
        // 设置属性
        layout.setPadding( (fDimRatio * 5).toInt(), (fDimRatio * 5).toInt(),
            (fDimRatio * 5).toInt(),(fDimRatio * 5).toInt())
        layout.setOrientation(LinearLayout.HORIZONTAL)

        // 2.创建内部EditText控件
        var etContent: TextView = TextView(this)
        var etParam: LinearLayout.LayoutParams =  LinearLayout.LayoutParams(800, ViewGroup.LayoutParams.MATCH_PARENT)
        etContent.setLayoutParams(etParam)
        // 设置属性
        etContent.setText(customer.year.toString()+"-"+(customer.month!!.plus(1)).toString()+"-"+customer.day.toString()+"    "+customer.calorie)
        // 将EditText放到LinearLayout里
        layout.addView(etContent)

        // 5.创建“-”按钮
        var btnDelete: Button = Button(this)
        var btnDeleteAddParam:LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        btnDelete.setLayoutParams(btnDeleteAddParam)
        btnDelete.setText("删除")
        btnDelete.setId(customer.id!!)
        btnDelete.setOnClickListener({
                deleteContent(it)
        })
        // 将“-”按钮放到RelativeLayout里
        layout.addView(btnDelete)
        btnIDIndex++
        println(" btnIDIndex"+btnIDIndex)
        listIBTNDel.add(btnDelete)

        // 7.将layout同它内部的所有控件加到最外围的llContentView容器里
        llContentView!!.addView(layout, btnIDIndex)
    }

    public fun deleteContent(v: View?) {
        if (v == null) {
            return
        }
        // 判断第几个“-”按钮触发了事件
        var iIndex = -1
        println("buttonid:"+v.id)
        var btn_id=v.id.toString().toInt()
        var id="id="+btn_id
        var result=database.use{
            delete("Customer2",id )
        }
        for (i in listIBTNDel.indices) {
            if (listIBTNDel[i] === v) {
                iIndex = i
                break
            }
        }
        if (iIndex >= 0) {
            listIBTNDel.removeAt(iIndex)
            // 从外围llContentView容器里删除第iIndex控件
            llContentView!!.removeViewAt(iIndex)
        }
    }

}