package com.example.sensoresAlhambra;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sensoresAlhambra.ui.viewAR.ViewARFragment;

import java.io.IOException;

public class InfoActivity extends AppCompatActivity implements SensorEventListener {

    TextView textInfo, tituloInfo;
    ImageView imageView;
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

        if (ViewARFragment.imagen.equals("image2.jpg")){
            imageView = (ImageView) findViewById(R.id.imagenPlano);
            try {
                imageView.setImageDrawable(Drawable.createFromStream( getAssets().open("fuente.jpg"),null) );
            } catch (IOException e) {
                e.printStackTrace();
            }

            tituloInfo = (TextView) findViewById(R.id.tituloPlano);
            tituloInfo.setText("Fuente");
            textInfo = (TextView) findViewById(R.id.plantaPlano);
            String information = "f. Manantial de agua que brota de la tierra:\n" +
                    "fuente de aguas ferruginosas.\n" +
                    "Construcción en los sitios públicos, como plazas, parques, etc., con caños y surtidores de agua, y que se destina a diferentes usos:\n" +
                    "la fuente de la Cibeles.\n" +
                    "Plato grande para servir la comida:\n" +
                    "sirvió el besugo en una fuente de cristal.\n" +
                    "Cantidad de comida que cabe en este plato:\n" +
                    "hizo una fuente de macarrones.\n" +
                    "Origen de algo, causa, principio:\n" +
                    "su renuncia fue una fuente de problemas.\n" +
                    "Aquello de que fluye con abundancia un líquido:\n" +
                    "sus ojos eran una fuente de lágrimas.\n" +
                    "Documento, obra o materiales que sirven de información o de inspiración a un autor:\n" +
                    "el Renacimiento bebió de fuentes grecolatinas.\n" +
                    "impr. Tipo de letra:\n" +
                    "¿qué fuente quieres que use en el documento?";
            textInfo.setText(information);
        }
        else if (ViewARFragment.imagen.equals("image3.jpg")){
            imageView = (ImageView) findViewById(R.id.imagenPlano);
            try {
                imageView.setImageDrawable(Drawable.createFromStream( getAssets().open("palacio.jpg"),null) );
            } catch (IOException e) {
                e.printStackTrace();
            }

            tituloInfo = (TextView) findViewById(R.id.tituloPlano);
            tituloInfo.setText("Palacio de Carlos V");
            textInfo = (TextView) findViewById(R.id.plantaPlano);
            String information = "El palacio de Carlos V es una construcción renacentista situada en la colina de la Alhambra de la ciudad española de Granada, en Andalucía. Desde 1958, es sede del Museo de Bellas Artes de Granada y, desde 1994, también es sede del Museo de la Alhambra.";
            textInfo.setText(information);
        }



    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 200) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x - last_x )/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    onBackPressed();
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
