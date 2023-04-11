package com.example.ku_calendar2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ku_calendar2.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    var datas:ArrayList<MyData> = ArrayList()
    lateinit var adapter:MyDataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initRecyclerView()
    }
    fun initData() {
        val scan = Scanner(resources.openRawResource(R.raw.info))
        while (scan.hasNextLine()){
            val month = scan.next()
            val date = scan.next()
            val schedule = scan.nextLine().substring(1)
            datas.add(MyData(month, date, schedule, false))
        }
        Log.d("datas", "$datas")
    }
    fun initRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 7)
        adapter = MyDataAdapter(datas)
        adapter.itemClickListener = object : MyDataAdapter.OnItemClickListener {
            override fun onItemClick(data: MyData, adapterPosition: Int) {
                if (data.schedule != "") {
                    data.isOpen = !(data.isOpen)
                    adapter.notifyItemRangeChanged(0, adapterPosition + 7)
                }
            }
        }
        binding.recyclerView.adapter = adapter
    }
}