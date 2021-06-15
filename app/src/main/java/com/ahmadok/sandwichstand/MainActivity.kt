package com.ahmadok.sandwichstand

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import org.w3c.dom.Text
import java.util.*


class MainActivity : AppCompatActivity() {


//    data class TestData(
//        val name: String = "",
//        val pass: String = "" /* very secure believe me hahahaha (:*/
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val db = FirebaseFirestore.getInstance()
//        val data = TestData("Okoosh", "CRFCR")
//
//        db.collection("testColl").add(data)
//            .addOnSuccessListener {
//                Log.i(TAG, "onCreate: Added a thing idk.... ")
//            }
//            .addOnFailureListener {
//                Log.e(TAG, "onCreate: Failed", it)
//            }
//        val uid = "6fE6H4YT0yHdI6p5tqBV"
//        db.collection("testColl").document(uid).delete()
//        db.collection("testColl").addSnapshotListener { value, error ->
//            value!!
//            for (_val in value.documents) {
//                val tstData = _val.toObject(TestData::class.java)
//                Log.i(TAG, "onCreate: Recieved Object: $tstData")
//            }
//        }
//    }
//
//    companion object {
//        private const val TAG = "MainActivity"
//    }

    var enableEditButton = false
    var enablePlaceOrderButton = true
    var displayMessageText = "Hi! Click 'Order Button' in order to order a sandwich"
    var currOrder: SandwichOrder? = null
    var lastUsedName : String? = null

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val gson = Gson()
                currOrder = gson.fromJson(data?.getStringExtra("order"),SandwichOrder::class.java)
                enableEditButton = true
                enablePlaceOrderButton = false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val placeOrderButton: Button = findViewById(R.id.placeOrderButton)

        placeOrderButton.setOnClickListener {
            val intent = Intent(this, PlaceOrderActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    override fun onResume() {
        val editOrderButton: Button = findViewById(R.id.editOrderButton)
        val placeOrderButton: Button = findViewById(R.id.placeOrderButton)
        val displayMessageTextView: TextView = findViewById(R.id.displayMessageTextView)


        displayMessageTextView.text = displayMessageText

        if(currOrder != null){
            val db = FirebaseFirestore.getInstance()
            db.collection("order").add(currOrder!!).addOnSuccessListener {
                displayMessageTextView.text = getString(R.string.display_message, currOrder!!.customerName)
                placeOrderButton.isEnabled = enablePlaceOrderButton
                editOrderButton.isEnabled = enableEditButton
            }.addOnFailureListener {
                displayMessageTextView.text = getString(R.string.order_fail_msg)
            }

        }
        super.onResume()


    }

}
