package com.example.errorbar

import android.os.Bundle
import android.support.design.widget.errorbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_example.*

class Example2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        setSupportActionBar(toolbar)
        frameLayout.errorbar {
            text = "You have no new emails"
            backdropResource = R.drawable.bg_empty
            contentMarginBottom = resources.getDimension(R.dimen.margin_bottom).toInt()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}