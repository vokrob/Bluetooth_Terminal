package com.vokrob.bluetooth_terminal

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
class ConnectThread(private val device: BluetoothDevice) : Thread() {
    val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    var mSocket: BluetoothSocket? = null

    init {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        } catch (i: IOException) {

        }
    }

    override fun run() {
        try {
            Log.d("MyLog", "Connecting...")

            mSocket?.connect()
            Log.d("MyLog", "Connected")

        } catch (i: IOException) {
            Log.d("MyLog", "Can not connect to device")
            closeConnection()
        }
    }

    fun closeConnection() {
        try {
            mSocket?.close()
        } catch (i: IOException) {

        }
    }
}
























