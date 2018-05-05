package com.lab81.sligamer.detectingshakejerkgesture

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


/**
 * Created by Justin Freres on 4/10/2018.
 * DetectingShakeJerkGesture *
 * Plugin Support with kotlin_version = '1.2.41'
 */
// Credit to James Bateman Cool way to compute how much shake to gforce was used.
//https://github.com/jrbateman/ShakeExperiment/blob/master/app/src/main/java/com/cornez/shakeexperiment/ShakeDetector.java

class ShakeDetector : SensorEventListener {

    // DECLARE VARIABLES
    private var mTimeOfLastShake: Long = 0

    private val SHAKE_THRESHOLD = 25f
    private val SHAKE_TIME_LAPSE = 200   //IN MILLISECONDS 500


    // OnShakeListener THAT WILL BE NOTIFIED WHEN A SHAKE IS DETECTED
    private var mShakeListener: OnShakeListener

    // CONSTRUCTOR SETS THE SHAKE LISTENER
    constructor(shakeListener: OnShakeListener)
    {
        mShakeListener = shakeListener
    }
    /**
     * Called when the accuracy of the registered sensor has changed.  Unlike
     * onSensorChanged(), this is only called when this accuracy value changes.
     *
     *
     * See the SENSOR_STATUS_* constants in
     * [SensorManager][android.hardware.SensorManager] for details.
     *
     * @param accuracy The new accuracy of this sensor, one of
     * `SensorManager.SENSOR_STATUS_*`
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    /**
     * Called when there is a new sensor event.  Note that "on changed"
     * is somewhat of a misnomer, as this will also be called if we have a
     * new reading from a sensor with the exact same sensor values (but a
     * newer timestamp).
     *
     *
     * See [SensorManager][android.hardware.SensorManager]
     * for details on possible sensor types.
     *
     * See also [SensorEvent][android.hardware.SensorEvent].
     *
     *
     * **NOTE:** The application doesn't own the
     * [event][android.hardware.SensorEvent]
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the [SensorEvent][android.hardware.SensorEvent].
     */
     override fun onSensorChanged(sensorEvent: SensorEvent) {

        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {

            //TASK 1: COLLECT SENSOR VALUES ON ALL THREE AXIS
            val x = sensorEvent.values[0]
            val y = sensorEvent.values[1]
            val z = sensorEvent.values[2]

            //TASK 2: CONVERT EACH ACCELEROMETER MEASUREMENT INTO
            //  A G-FORCE MEASUREMENT BY NEUTRALIZING GRAVITY.
            val gForceX = x - SensorManager.GRAVITY_EARTH
            val gForceY = y - SensorManager.GRAVITY_EARTH
            val gForceZ = z - SensorManager.GRAVITY_EARTH

            //TASK 3: COMPUTE  G-FORCE AS A DIRECTIONLESS MEASUREMENT
            // NOTE: G-FORCE WILL BE APPROXIMATELY 1 WHEN
            //       THERE IS NO SHAKING MOVEMENT.
            val vector = Math.pow(gForceX.toDouble(), 2.0) + Math.pow(gForceY.toDouble(), 2.0) + Math.pow(gForceZ.toDouble(), 2.0)
            val gForce = Math.sqrt(vector).toFloat()

            //TASK 4: DETERMINE IF THE G-FORCE IS ENOUGH TO REGISTER AS A SHAKE

            if (gForce > SHAKE_THRESHOLD) {
                //IGNORE CONTINUOUS SHAKES - CHECK THAT 500 MILLISECONDS HAVE LAPSED
                val now = System.currentTimeMillis()
                if (mTimeOfLastShake + SHAKE_TIME_LAPSE > now) {
                    return
                }
                mTimeOfLastShake = now

                //THE LISTENER REGISTERED A SHAKE
                mShakeListener.onShake()
            }

        }
    }

    interface OnShakeListener {
        fun onShake()
    }

}
