package com.lab81.sligamer.detectingshakejerkgesture

import android.content.Context
import android.hardware.Sensor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorManager
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView

/**
 * Created by Justin Freres on 4/10/2018.
 * DetectingShakeJerkGesture *
 * Plugin Support with kotlin_version = '1.2.41'
 */
class MainActivity : AppCompatActivity() {

    // DECLARE VARIABLES
    private var mSensorManager: SensorManager? = null
    private var mSensorAccelerometer: Sensor? = null
    private var mShakeDetector: ShakeDetector? = null

    private lateinit var lightBulb: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TASK 1: SET LAYOUT REFS
        lightBulb = findViewById(R.id.lightOffImageView)

        // TASK 2: REGISTER THE SENSOR MANAGER AND SETUP THE SHAKE DETECTION
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mShakeDetector = ShakeDetector(object : ShakeDetector.OnShakeListener {
            override fun onShake() {
                displayLightOn()
            }
        })
    }

    private fun displayLightOn() {
        // TASK 3: SHAKE TURN LIGHT ON
        lightBulb.setImageResource(R.drawable.lighton)
    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(mShakeDetector, mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager!!.unregisterListener(mShakeDetector, mSensorAccelerometer)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId
        if(id == R.string.action_settings){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
