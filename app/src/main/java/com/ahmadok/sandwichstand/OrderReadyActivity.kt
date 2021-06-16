package com.ahmadok.sandwichstand

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class OrderReadyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_ready)
        val gotItButton : Button = findViewById(R.id.gotItButton)

        gotItButton.setOnClickListener {
            this.finish()
        }
    }
}