package com.example.tmc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tmc.beans.User
import com.github.mikephil.charting.charts.PieChart
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register_user.*
import kotlinx.android.synthetic.main.activity_tmc.*

class TMCActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    lateinit var ref: DatabaseReference
    lateinit var trafficLightState: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmc)

        ref = FirebaseDatabase.getInstance().getReference("trafficLight")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()) {
                    trafficLightState = p0.value.toString()
                    trafficLightTextView.text = trafficLightState
                }
            }
        })

        val pieChart: PieChart = PieChart(applicationContext)
        pieChart.centerText = "BRE"


    }
}
