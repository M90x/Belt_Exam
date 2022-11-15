package com.example.meshal

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //Remove last entry from recycler view
    private lateinit var removeLRV: FloatingActionButton

    //Intake element declaration
    private lateinit var intakeField: EditText
    private lateinit var intakeBtn: Button

    //Burned element declaration
    private lateinit var burnedField: EditText
    private lateinit var burnedBtn: Button

    //Recycler View declaration
    private var rvList: kotlin.collections.ArrayList<String> = ArrayList()

    //Save input numbers & String in arrayList
    private var saveNum: ArrayList<Int> = ArrayList()
    private var saveStr: ArrayList<String> = ArrayList()

    //Daily Calories View
    private lateinit var dailyCalView: TextView

    private var dailyCalories = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvList = ArrayList()
        saveNum = ArrayList()
        saveStr = ArrayList()

        rvMain.adapter = RecyclerViewAdabter(rvList)
        rvMain.layoutManager = LinearLayoutManager(this)

        //DailyCalories variable
        dailyCalView = findViewById(R.id.dailyCalView)
        dailyCalView.text = ("Daily Calories: $dailyCalories")

        //Intake variables
        intakeField = findViewById(R.id.intakeField)
        intakeBtn = findViewById(R.id.intakeBtn)

        //Burned variables
        burnedField = findViewById(R.id.burnedField)
        burnedBtn = findViewById(R.id.burnedBtn)

        //Remove last entry from recycler view
        removeLRV = findViewById(R.id.removeLRV)

        //Buttons
        intakeBtn.setOnClickListener{ intake() }
        burnedBtn.setOnClickListener{ burned() }
        removeLRV.setOnClickListener{ removeLastRV() }

    }

    //----------------------------------------Functions----------------------------------------


    //Add the amount of calories ----------------------------------------------
    private fun intake(){
        val intakeNum = intakeField.text.toString()

        if(intakeNum.isNotEmpty()){

            if(dailyCalories < 3000) {

                rvList.add("Calorie Intake: $intakeNum")
                saveNum.add(intakeNum.toInt())
                saveStr.add("INTAKE")

                // To make Automatically scroll to the bottom of the Recycler View
                autoScroll()

                dailyCalories += intakeNum.toInt()
                dailyCalView.text = ("Daily Calories: $dailyCalories")

                Toast.makeText(this, "Amount added!", Toast.LENGTH_LONG).show()

                intakeField.text.clear()
                intakeField.clearFocus()
                rvMain.adapter?.notifyDataSetChanged()

            }else{

                Toast.makeText(this, "You have reached the limit 3000", Toast.LENGTH_LONG).show()
            }
        }else{

            Toast.makeText(this, "Please enter the amount of calories!", Toast.LENGTH_LONG).show()
        }
    }


    //Sub the amount of calories ----------------------------------------------
    private fun burned(){
        val burnedNum = burnedField.text.toString()

        if(burnedNum.isNotEmpty()){

            rvList.add("Calorie Burned: $burnedNum")
            saveNum.add(burnedNum.toInt())
            saveStr.add("BURNED")

            // To make Automatically scroll to the bottom of the Recycler View
            autoScroll()

            dailyCalories -= burnedNum.toInt()
            dailyCalView.text = ("Daily Calories: $dailyCalories")

            burnedField.text.clear()
            burnedField.clearFocus()
            rvMain.adapter?.notifyDataSetChanged()

        }else{

            Toast.makeText(this, "Please enter the amount of calories!", Toast.LENGTH_LONG).show()
        }
    }


    // Make Automatically scroll to the bottom of the Recycler View ---------------------------
    private fun autoScroll(){
        rvMain.smoothScrollToPosition(rvList.size-1)
    }


    //Save the last total of daily calories ---------------------------------------------------
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var dailyCal: Int = dailyCalories
        outState.putInt("savedDailyCal", dailyCalories)
    }


    //Restore the last total of daily calories -----------------------------------------------
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        var dailyCalInt : Int = savedInstanceState.getInt("savedDailyCal", 0)
        dailyCalories = dailyCalInt
        dailyCalView.text = ("Daily Calories: $dailyCalories")

    }


    //Remove the last entry of Recycler View list with change total amount -------------------
    private fun removeLastRV(){ //
        rvList.removeLast()
        rvMain.adapter?.notifyDataSetChanged()
        Toast.makeText(this, "The last entry have been removed!!", Toast.LENGTH_LONG).show()

        if (saveStr.last() == "INTAKE")
        {
            dailyCalories -= saveNum.last()
            dailyCalView.text = ("Daily Calories: $dailyCalories")
            saveStr.removeLast()
            saveNum.removeLast()
        }
        else{
            dailyCalories += saveNum.last()
            dailyCalView.text = ("Daily Calories: $dailyCalories")
            saveStr.removeLast()
            saveNum.removeLast()
        }

    }


    //Create dropdown menu --------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //Reset their Recycler View from a dropdown menu, with the total amount --------------
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.resetRV -> {
                rvList.clear()
                saveStr.clear()
                saveNum.clear()
                rvMain.adapter?.notifyDataSetChanged()

                dailyCalories = 0
                dailyCalView.text = ("Daily Calories: $dailyCalories")

                Toast.makeText(applicationContext, "The list and total amount have been reset!!", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}