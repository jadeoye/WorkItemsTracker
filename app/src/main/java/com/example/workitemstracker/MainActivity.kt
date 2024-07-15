package com.example.workitemstracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    val defaultPriority: String = "-- Select One --"
    val defaultActivity: String = "-- Select One --"

    var selectedPriority: String = defaultPriority
    var selectedActivity: String = defaultActivity

    lateinit var prioritySpinner: Spinner
    lateinit var activitySpinner: Spinner
    lateinit var estimateEntry: EditText
    lateinit var remainingEntry: EditText
    lateinit var completedEntry: EditText

    val priorities = arrayOf(defaultPriority, "Low", "Medium", "High", "Urgent")
    val activities = arrayOf(
        defaultActivity,
        "Deployment",
        "Design",
        "Development",
        "Documentation",
        "Requirements",
        "Testing"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prioritySpinner = findViewById(R.id.prioritySpinner)
        activitySpinner = findViewById(R.id.activitySpinner)

        estimateEntry = findViewById(R.id.estimateEntry)
        remainingEntry = findViewById(R.id.remainingEntry)
        completedEntry = findViewById(R.id.completedEntry)

        val resetButton: Button = findViewById(R.id.resetButton)
        val saveButton: Button = findViewById(R.id.saveButton)

        prioritySpinner.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, priorities)
        activitySpinner.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activities)

        prioritySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedPriority = priorities.get(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        activitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedActivity = activities.get(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        resetButton.setOnClickListener { view ->
            resetEntries(showToastMessage = true)
        }

        saveButton.setOnClickListener { view ->
            if (selectedPriority == defaultPriority) {
                notify("Please select a Priority")
                return@setOnClickListener
            }

            if (selectedActivity == defaultActivity) {
                notify("Please select an Activity")
                return@setOnClickListener
            }

            if (!validateEntry(estimateEntry.text.toString(), "Estimate"))
                return@setOnClickListener

            if (!validateEntry(remainingEntry.text.toString(), "Remaining"))
                return@setOnClickListener

            if (!validateEntry(completedEntry.text.toString(), "Completed"))
                return@setOnClickListener

            resetEntries(showToastMessage = false)
            notify("Progress Tracked")
        }
    }

    private fun notify(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun validateEntry(entry: String, name: String): Boolean {
        if (entry.length <= 0 || entry.toInt() < 0) {
            notify("${name} cannot be less than 0")
            return false
        }
        return true
    }

    private fun resetEntries(showToastMessage: Boolean = false) {
        selectedActivity = defaultActivity
        selectedPriority = defaultPriority

        activitySpinner.setSelection(0, true)
        prioritySpinner.setSelection(0, true)

        estimateEntry.text.clear()
        remainingEntry.text.clear()
        completedEntry.text.clear()

        if (showToastMessage)
            notify("Entries have been Reset")
    }
}