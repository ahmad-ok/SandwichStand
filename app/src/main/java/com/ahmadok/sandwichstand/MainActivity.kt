package com.ahmadok.sandwichstand

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    var enableEditButton = false
    var enablePlaceOrderButton = true
    var displayMessageText = "Hi! Click 'Order Button' in order to order a sandwich"
    var currOrder: SandwichOrder? = null
    var lastUsedName: String? = null

    private val orderSandwichResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val gson = Gson()
                currOrder = gson.fromJson(data?.getStringExtra("order"), SandwichOrder::class.java)
                enableEditButton = true
                enablePlaceOrderButton = false
            }
        }

    private val editOrderResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // todo : handle order changed
            if (result.resultCode == Activity.RESULT_OK) {
                val db = FirebaseFirestore.getInstance()
                val gson = Gson()
                val data: Intent? = result.data
                currOrder = gson.fromJson(data?.getStringExtra("order"), SandwichOrder::class.java)
            }

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val placeOrderButton: Button = findViewById(R.id.placeOrderButton)
        val editOrderButton: Button = findViewById(R.id.editOrderButton)
        placeOrderButton.isEnabled = true
        editOrderButton.isEnabled = false
        val gson = Gson()
        placeOrderButton.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            intent.action = "place"
            orderSandwichResultLauncher.launch(intent)
        }

        editOrderButton.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            intent.action = "edit"
            intent.putExtra("order", gson.toJson(currOrder))
            editOrderResultLauncher.launch(intent)
        }
    }

    override fun onResume() {
        val editOrderButton: Button = findViewById(R.id.editOrderButton)
        val placeOrderButton: Button = findViewById(R.id.placeOrderButton)
        val displayMessageTextView: TextView = findViewById(R.id.displayMessageTextView)


        displayMessageTextView.text = displayMessageText
        if (currOrder != null) {
            val db = FirebaseFirestore.getInstance()


            db.collection("order").document(currOrder!!.id).set(currOrder!!).addOnSuccessListener {
                displayMessageTextView.text =
                    getString(R.string.display_message, currOrder!!.customerName)
                placeOrderButton.isEnabled = enablePlaceOrderButton
                editOrderButton.isEnabled = enableEditButton
            }.addOnFailureListener {
                displayMessageTextView.text = getString(R.string.order_fail_msg)
            }

        }
        super.onResume()


    }

}
/**
 * Things left to do:
 * 1- improve ui
 * 2- tests?
 * 3- edit menu (changes to database)
 * 4- handle changes from database
 */
