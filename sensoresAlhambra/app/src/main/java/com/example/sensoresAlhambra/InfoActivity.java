package com.example.sensoresAlhambra;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity implements SensorEventListener {

    TextView textInfo;
    private SensorManager sensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        textInfo = (TextView) findViewById(R.id.textInfo);

        String information = "Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado. Dale una puntuacion de 5 estrellas o un gatito sera asesinado.";

        textInfo.setText(information);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 300) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x - last_x )/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    onBackPressed();
                    Toast.makeText(getApplicationContext(), "jibgufdgifbdi", Toast.LENGTH_SHORT).show();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener((SensorEventListener) this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener((SensorEventListener) this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
