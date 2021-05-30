package com.ahmadok.sandwichstand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editOrderButton : Button = findViewById(R.id.editOrderButton)
        val placeOrderButton : Button = findViewById(R.id.placeOrderButton)
        editOrderButton.isEnabled = false

        placeOrderButton.setOnClickListener {

        }


    }
}