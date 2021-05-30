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
    var displayMessageTextView : TextView? = null
    var placeOrderButton : Button? = null
    var editOrderButton : Button? = null

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if(result.resultCode == Activity.RESULT_OK){
            val data: Intent ? = result.data
            val userName : String? = data?.getStringExtra("username")
            displayMessageTextView?.text = getString(R.string.display_message, userName)
            placeOrderButton?.isEnabled = false
            editOrderButton?.isEnabled = true
        }
        else{
            displayMessageTextView?.text = getString(R.string.display_message_default)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editOrderButton: Button = findViewById(R.id.editOrderButton)
        val placeOrderButton: Button = findViewById(R.id.placeOrderButton)
        displayMessageTextView = findViewById(R.id.displayMessageTextView)
        editOrderButton.isEnabled = false

        placeOrderButton.setOnClickListener {
            val intent = Intent(this, PlaceOrderActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

}
