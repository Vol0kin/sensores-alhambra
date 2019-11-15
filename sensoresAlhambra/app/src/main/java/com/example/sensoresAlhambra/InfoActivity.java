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


/**
 * Clase que muestra informacion con un titulo, una imagen, y un texto, sobre una
 * region destacada del VR
 */
public class InfoActivity extends AppCompatActivity implements SensorEventListener {
    /**
     * Texto y título a mostrar
     */
    TextView textInfo, tituloInfo;

    /**
     * Imagen a mostrar
     */
    ImageView imageView;

    /**
     * Gestor de sensores. Utilizado para acceder a ellos y gestionar
     * si se registran o no nuevos valores valores
     */
    private SensorManager sensorManager;

    /**
     * Acelerómetro
     */
    private Sensor senAccelerometer;

    /**
     * Tiempo de la última actualización del acelerómetro
     */
    private long lastUpdate = 0;

    /**
     * Últimos valores X, Y, Z registrados
     */
    private float last_x, last_y, last_z;

    /**
     * Umbral del acelerómetro
     */
    private static final int SHAKE_THRESHOLD = 200;


    /**
     * Creamos la vista principal donde analiza que imagen e información cargar
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Declaración de variables
        setContentView(R.layout.activity_info);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);



        // Obtenemos la imagen cargada en el ViewARFragment y dependiendo de cual sea
        // asignamos una información u otra a los objetos

        if (ViewARFragment.imagen.equals("image2.jpg")){
            // Cargar y seleccionar la imagen
            imageView = (ImageView) findViewById(R.id.imageInfo);
            try {
                imageView.setImageDrawable(Drawable.createFromStream( getAssets().open("fuente.jpg"),null) );
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Seleccionar el título
            tituloInfo = (TextView) findViewById(R.id.titleInfo);
            tituloInfo.setText("Fuente");

            // Seleccionar la descripción
            textInfo = (TextView) findViewById(R.id.floorInfo);
            String information = "f. Manantial de agua que brota de la tierra: fuente de aguas ferruginosas. Construcción en los sitios públicos, como plazas, parques, etc., con caños y surtidores de agua, y que se destina a diferentes usos: la fuente de la Cibeles. Plato grande para servir la comida: sirvió el besugo en una fuente de cristal.\n";
            textInfo.setText(information);
        }// Repite el mismo proceso para otra imagen
        else if (ViewARFragment.imagen.equals("image3.jpg")){
            // Cargar y seleccionar la imagen
            imageView = (ImageView) findViewById(R.id.imageInfo);
            try {
                imageView.setImageDrawable(Drawable.createFromStream( getAssets().open("palacio.jpg"),null) );
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Seleccionar el título
            tituloInfo = (TextView) findViewById(R.id.titleInfo);
            tituloInfo.setText("Palacio de Carlos V");

            // Seleccionar la descripción
            textInfo = (TextView) findViewById(R.id.floorInfo);
            String information = "El palacio de Carlos V es una construcción renacentista situada en la colina de la Alhambra de la ciudad española de Granada, en Andalucía. Desde 1958, es sede del Museo de Bellas Artes de Granada y, desde 1994, también es sede del Museo de la Alhambra.";
            textInfo.setText(information);
        }
    }


    /**
     * Método que actua cuando un sensor se actualiza
     * @param sensorEvent: Sensor actualizado
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        // Comprueba si el sensor es el acelerómetro
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // En caso afirmativo, guarda los valores que recibe
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            // Guarda la hora del sistema actual n milisegundos
            long curTime = System.currentTimeMillis();


            // La próxima señal que se leerá será una con 200 milisegundos de diferencia
            if ((curTime - lastUpdate) > 200) {
                // Actualiza el tiempo de la última llegada
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                // Calcula la velocidad en el eje x teniendo en cuenta la diferencia de tiempos
                float speed = Math.abs(x - last_x )/ diffTime * 10000;


                // Si la velocidad supera el umbral, realiza la acción
                if (speed > SHAKE_THRESHOLD) {
                    finish();
                }

                // Actualiza los valores del sensor
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    /**
     * Acciones cuando la actividad se queda en pausa
     */
    @Override
    public void onPause() {
        super.onPause();

        // Deja de recibir actualizaciones
        sensorManager.unregisterListener(this);
    }

    /**
     * Acciones cuando la actividad se vuelve a reiniciar tras estar en pausa
     */
    @Override
    public void onResume() {
        super.onResume();

        /**
         * Volvemos a registrar los listener para tener sensores para el visor.
         * Establecemos el intervalo entre actualizaciones.
         */

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

    /**
     * Acciones cuando la actividad termina
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Acciones realizadas si se cambiase la precisión de un sensor
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // En caso de que la precisión cambie
    }
}
