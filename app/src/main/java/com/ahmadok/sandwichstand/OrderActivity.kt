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

class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)


        val customerNameEditText: TextView = findViewById(R.id.customerNameEditText)
        val placeOrderTitle: TextView = findViewById(R.id.placeOrderTitle)
        val addPickleButton: Button = findViewById(R.id.addPickleButton)
        val removePickleButton: Button = findViewById(R.id.removePickleButton)
        val cancelOrderButton: Button = findViewById(R.id.cancelOrderButton)
        val saveOrderButton: Button = findViewById(R.id.saveOrderButton)
        val numPickles: TextView = findViewById(R.id.picklesPicked)
        val wantsHummusCheckbox: CheckBox = findViewById(R.id.wantsHummusCheckbox)
        val wantsTahiniCheckbox: CheckBox = findViewById(R.id.wantsTahiniCheckbox)
        val gson = Gson()
        removePickleButton.isEnabled = false

        cancelOrderButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_CANCELED, resultIntent)
            this.finish()
        }
        if(intent.action == "edit") {
            val order: SandwichOrder = gson.fromJson(
                intent.getStringExtra("order"),
                SandwichOrder::class.java
            )
            placeOrderTitle.text = getString(R.string.edit_order_title)
            saveOrderButton.text = getString(R.string.confirm_changes_button)

            customerNameEditText.text = order.customerName
            numPickles.text = order.pickles.toString()
            val pickles = numPickles.text.toString().toInt()
            wantsHummusCheckbox.isChecked = order.hummus
            wantsTahiniCheckbox.isChecked = order.tahini
            addPickleButton.isEnabled = pickles != 10
            removePickleButton.isEnabled = pickles != 0



            saveOrderButton.setOnClickListener {
                order.customerName = customerNameEditText.text.toString()
                order.pickles = numPickles.text.toString().toInt()
                order.hummus = wantsHummusCheckbox.isChecked
                order.tahini = wantsHummusCheckbox.isChecked
                val resultIntent = Intent()
                resultIntent.putExtra("order", gson.toJson(order))
                setResult(Activity.RESULT_OK, resultIntent)
                this.finish()



            }
        }
        else if(intent.action == "place"){
            placeOrderTitle.text = getString(R.string.enter_order_details_title)
            saveOrderButton.text = getString(R.string.save_order_button)
            customerNameEditText.text = intent.getStringExtra("name")

            saveOrderButton.setOnClickListener {
                val resultIntent = Intent()
                val customerName = customerNameEditText.text.toString()
                val pickleNum = numPickles.text.toString().toInt()
                val order = SandwichOrder(
                    UUID.randomUUID().toString(), customerName, pickleNum,
                    wantsHummusCheckbox.isChecked, wantsTahiniCheckbox.isChecked, Status.waiting
                )
                resultIntent.putExtra("order", gson.toJson(order))
                setResult(Activity.RESULT_OK, resultIntent)
                this.finish()
            }
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