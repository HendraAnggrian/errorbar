package com.example.errorbar

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.hendraanggrian.material.errorbar.Errorbar
import com.hendraanggrian.material.errorbar.addCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var lengthShortItem: MenuItem
    private lateinit var lengthLongItem: MenuItem
    private lateinit var lengthIndefiniteItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        lengthShortItem = menu.findItem(R.id.lengthShortItem)
        lengthLongItem = menu.findItem(R.id.lengthLongItem)
        lengthIndefiniteItem = menu.findItem(R.id.lengthIndefiniteItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.make -> {
                val errorbar = Errorbar.make(frameLayout, "No internet connection", length)
                    .setAction("Retry") {
                        Snackbar.make(frameLayout, "Clicked", Snackbar.LENGTH_SHORT).show()
                    }
                    .addCallback {
                        onShown {
                            Toast.makeText(this@MainActivity, "shown", Toast.LENGTH_SHORT).show()
                        }
                        onDismissed { _, event ->
                            Toast.makeText(this@MainActivity, "dismissed event: $event", Toast.LENGTH_SHORT).show()
                        }
                    }
                errorbar.show()
            }
            else -> item.isChecked = true
        }
        return super.onOptionsItemSelected(item)
    }

    private inline val length
        get() = when {
            lengthShortItem.isChecked -> Errorbar.LENGTH_SHORT
            lengthLongItem.isChecked -> Errorbar.LENGTH_LONG
            else -> Errorbar.LENGTH_INDEFINITE
        }
}