package com.ahmadok.sandwichstand

import android.R.id
import android.app.Activity
import android.content.Intent
import android.net.http.SslCertificate.saveState
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firestore.v1.StructuredQuery.Order
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    var enableEditButton = false
    var enablePlaceOrderButton = true
    var displayMessageText = "Hi! Click 'Order Button' in order to order a sandwich"
    var currOrder: SandwichOrder? = null
    var lastUsedName: String = ""

    private val orderSandwichResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val gson = Gson()
                currOrder = gson.fromJson(data?.getStringExtra("order"), SandwichOrder::class.java)
                lastUsedName = currOrder!!.customerName
                enableEditButton = true
                enablePlaceOrderButton = false
            }
        }

    private val editOrderResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val gson = Gson()
                val data: Intent? = result.data
                currOrder = gson.fromJson(data?.getStringExtra("order"), SandwichOrder::class.java)
                lastUsedName = currOrder!!.customerName

            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val placeOrderButton: Button = findViewById(R.id.placeOrderButton)
        val editOrderButton: Button = findViewById(R.id.editOrderButton)
        val sandwichProgressBar: ProgressBar = findViewById(R.id.sandwichProgressBar)
        val progressTextView: TextView = findViewById(R.id.progressTextView)


        sandwichProgressBar.visibility = View.INVISIBLE
        progressTextView.visibility = View.INVISIBLE
        placeOrderButton.isEnabled = true
        editOrderButton.isEnabled = false
        val gson = Gson()
        placeOrderButton.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            intent.action = "place"
            intent.putExtra("name", lastUsedName)
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
        val sandwichProgressBar: ProgressBar = findViewById(R.id.sandwichProgressBar)
        val progressTextView: TextView = findViewById(R.id.progressTextView)

        displayMessageTextView.text = displayMessageText
        if (currOrder != null) {
            val db = FirebaseFirestore.getInstance()

            db.collection("order").document(currOrder!!.id).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if(currOrder != null) {
                    val newStatus: Status = Status.valueOf(snapshot?.data?.get("status") as String)
                    if (newStatus == Status.`in-progress` && currOrder?.status == Status.waiting) {
                        placeOrderButton.isEnabled = false
                        editOrderButton.isEnabled = false
                        sandwichProgressBar.visibility = View.VISIBLE
                        progressTextView.visibility = View.VISIBLE

                    } else if (newStatus == Status.ready && currOrder?.status == Status.`in-progress` ) {
                        currOrder = null
                        placeOrderButton.isEnabled = true
                        editOrderButton.isEnabled = false
                        sandwichProgressBar.visibility = View.INVISIBLE
                        progressTextView.visibility = View.INVISIBLE
                        val readyIntent = Intent(this, OrderReadyActivity::class.java)
                        startActivity(readyIntent)
                    }
                    currOrder?.status = newStatus
                }
            }


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
 * - tests?
 * - make app work on rotation
 * - app saves progress on exit
 */
