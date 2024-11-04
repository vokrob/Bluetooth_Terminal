package com.vokrob.bluetooth_terminal

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vokrob.bluetooth_terminal.databinding.ActivityControlBinding

class ControlActivity : AppCompatActivity() {
    private lateinit var binding: ActivityControlBinding
    private lateinit var actListLauncher: ActivityResultLauncher<Intent>
    lateinit var btConnection: BtConnection
    private var listItem: ListItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        onBtListResult()
        init()
        binding.apply {
            bA.setOnClickListener {
                btConnection.sendMessage("A")
            }
            bB.setOnClickListener {
                btConnection.sendMessage("B")
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.green_dark)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        btConnection = BtConnection(btAdapter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.control_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.list) {
            actListLauncher.launch(Intent(this, BtListActivity::class.java))
        } else if (item.itemId == R.id.connect) {
            listItem.let {
                btConnection.connect(it?.mac!!)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun onBtListResult() {
        actListLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    listItem = it.data?.getSerializableExtra(BtListActivity.DEVICE_KEY) as ListItem
                }
            }
    }
}





















