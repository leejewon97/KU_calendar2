package com.example.ku_calendar2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.ku_calendar2.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    var datas:ArrayList<MyData> = ArrayList()
    var dates:ArrayList<String> = ArrayList()
    lateinit var dataAdapter:MyDataAdapter
    lateinit var spinnerAdapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initRecyclerView()
        initSpinner()
    }

    fun initSpinner() {
        spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ArrayList<String>())
        spinnerAdapter.add("9")
        spinnerAdapter.add("10")
        spinnerAdapter.add("11")
        spinnerAdapter.add("12")
        spinnerAdapter.add("1")
        spinnerAdapter.add("2")
        binding.spinner.adapter = spinnerAdapter

        val smoothScroller: RecyclerView.SmoothScroller by lazy {
            object : LinearSmoothScroller(this) {
                override fun getVerticalSnapPreference() = SNAP_TO_START
            }
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val month = parent?.getItemAtPosition(position).toString()
                val targetPosition = calculatePosition(month)
                smoothScroller.targetPosition = targetPosition

                Handler().postDelayed(Runnable {
                    binding.recyclerView.layoutManager?.startSmoothScroll(smoothScroller) }, 100)
                dataAdapter.notifyItemRangeChanged(0, targetPosition + 41)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun calculatePosition(month: String) = dates.indexOf("${month}_1")

    fun initData() {
        val scan = Scanner(resources.openRawResource(R.raw.info))
        while (scan.hasNextLine()){
            val month = scan.next()
            val date = scan.next()
            val schedule = scan.nextLine().substring(1)
            datas.add(MyData(month, date, schedule, false, Color.LTGRAY))
            dates.add("${month}_${date}")
        }
        Log.d("datas", "$datas")
    }

    fun initRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 7)
        dataAdapter = MyDataAdapter(datas, binding.spinner)
        dataAdapter.itemClickListener = object : MyDataAdapter.OnClickListener {
            override fun onItemClick(data: MyData, adapterPosition: Int) {
                if ((data.schedule != "") && (data.color != Color.LTGRAY)) {
                    data.isOpen = !(data.isOpen)
                    dataAdapter.notifyItemRangeChanged(0, adapterPosition + 7)
                }
            }

            override fun onButtonClick(data: MyData) {
                val allDayMillis: Long = Calendar.getInstance().run {
                    set(2023, data.month.toInt() - 1, data.date.toInt())
                    timeInMillis
                }
                val intent = Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, allDayMillis)
                    .putExtra(CalendarContract.Events.TITLE, data.schedule)
                startActivity(intent)
                Log.d("calendar intent", data.toString())
            }
        }
        binding.recyclerView.adapter = dataAdapter
    }
}