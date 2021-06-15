package com.ahmadok.sandwichstand

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.util.*

class PlaceOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)


        val customerNameEditText: TextView = findViewById(R.id.customerNameEditText)
        val addPickleButton: Button = findViewById(R.id.addPickleButton)
        val removePickleButton: Button = findViewById(R.id.removePickleButton)
        val cancelOrderButton: Button = findViewById(R.id.cancelOrderButton)
        val saveOrderButton: Button = findViewById(R.id.saveOrderButton)
        val numPickles: TextView = findViewById(R.id.picklesPicked)
        val wantsHummusCheckbox: CheckBox = findViewById(R.id.wantsHummusCheckbox)
        val wantsTahiniCheckbox: CheckBox = findViewById(R.id.wantsTahiniCheckbox)
        removePickleButton.isEnabled = false

        cancelOrderButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_CANCELED, resultIntent)
            this.finish()
        }

        saveOrderButton.setOnClickListener {
            //todo send request to database
            val resultIntent = Intent()
            val gson = Gson()
            val customerName = customerNameEditText.text.toString()
            val pickleNum = numPickles.text.toString().toInt()
            val order: SandwichOrder = SandwichOrder(
                UUID.randomUUID().toString(), customerName, pickleNum,
                wantsHummusCheckbox.isChecked, wantsTahiniCheckbox.isChecked,Status.Waiting
            )
            resultIntent.putExtra("order", gson.toJson(order))
            setResult(Activity.RESULT_OK, resultIntent)
            this.finish()
        }

        addPickleButton.setOnClickListener {
            val curr: String = numPickles.text.toString()
            val num = Integer.parseInt(curr)

            if (num == 0) {
                removePickleButton.isEnabled = true
            }

            if (num + 1 == 10) {
                addPickleButton.isEnabled = false
            }
            numPickles.text = (num + 1).toString()

        }
        removePickleButton.setOnClickListener {
            val curr: String = numPickles.text.toString()
            val num = Integer.parseInt(curr)

            if (num == 10) {
                addPickleButton.isEnabled = true
            }
            if (num - 1 == 0) {
                removePickleButton.isEnabled = false
            }
            numPickles.text = (num - 1).toString()
        }


    }
}