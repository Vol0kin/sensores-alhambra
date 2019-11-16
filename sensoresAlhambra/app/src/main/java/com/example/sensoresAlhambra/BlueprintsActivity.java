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

import com.example.sensoresAlhambra.ui.camera.CameraFragment;

import java.io.IOException;

/**
 * Clase que muestra información para los QR leídos, mostrando un título, una
 * imagen (que será un plano) y un texto (la localización)
 */
public class BlueprintsActivity extends AppCompatActivity implements SensorEventListener {
    /**
     * Título y localización
     */
    TextView tituloPlano, plantaPlano;

    /**
     * Imagen a mostrar
     */
    ImageView imagenPlano;

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
     * Último valor de X registrado
     */
    private float last_x;

    /**
     * Umbral del acelerómetro
     */
    private static final int SHAKE_THRESHOLD = 200;

    /**
     * Umbral de tiempo entre actualizaciones
     */
    private static final long UPDATE_TIME_THRESHOLD = 200;

    /**
     * Método llamado al crear un BlueprintsActivity. Determina qué información debe mostrar
     * en función de la lectura del CameraFragment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blueprints);

        // Variables que contendrán información sobre la imagen a mostrar
        String fileName, title, information;

        // Determinar que información mostrar
        switch(CameraFragment.lecturaQr) {
            case("fuente"):
                fileName = "fuente.jpg";
                title = "Fuente";
                information = "Planta baja";
                break;
            case("palacio"):
                fileName = "palacio.jpg";
                title = "Palacio de Carlos V";
                information = "Planta 2";
                break;
            case("nazaries"):
                fileName = "palacios-nazaries-base.jpg";
                title = "Mapa de los Palacios Nazaríes";
                information = "Plano de los palacios";
                break;
            case("mezquita"):
                fileName = "palacios-nazaries-1.png";
                title = "Posición actual";
                information = "Mezquita";
                break;
            case("hermanas"):
                fileName = "palacios-nazaries-2.png";
                title = "Posición actual";
                information = "Sala de las Dos Hermanas";
                break;
            case("reyes"):
                fileName = "palacios-nazaries-3.png";
                title = "Posición actual";
                information = "Sala de los Reyes";
                break;
            case("embajadores"):
                fileName = "palacios-nazaries-4.png";
                title = "Posición actual";
                information = "Salón de Embajadores";
                break;
            default:
                fileName = "sad_android.png";
                title = "Código QR no soportado";
                information = "Lo sentimos, no estás en la Alhambra";
                break;
        }

        // Mostrar la información
        this.showImageTextInfo(fileName, title, information);

        // Establecer gestor de sensores y acelerómetro
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Método que muestra el plano con su título e información correspondiente
     * @param fileName Nombre del archivo a abrir
     * @param title Título que mostrar
     * @param information Información sobre la localización que mostrar
     */
    private void showImageTextInfo(String fileName, String title, String information) {

        // Obtener ImageView del plano
        imagenPlano = (ImageView) findViewById(R.id.titleInfo);

        // Intentar establecer imagen
        try {
            imagenPlano.setImageDrawable(Drawable.createFromStream( getAssets().open(fileName),null) );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Establecer título
        tituloPlano = (TextView) findViewById(R.id.imageInfo);
        tituloPlano.setText(title);

        // Establecer información
        plantaPlano = (TextView) findViewById(R.id.floorInfo);
        plantaPlano.setText(information);
    }

    /**
     * Método que actúa cuando un sensor se actualiza
     * @param sensorEvent: Sensor actualizado
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        // Comprueba si el sensor es el acelerómetro
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // En caso afirmativo, guarda los valores del eje X que recibe
            float x = sensorEvent.values[0];

            // Guarda la hora del sistema actual en milisegundos
            long curTime = System.currentTimeMillis();


            // La próxima señal que se leerá será una con 200 milisegundos de diferencia
            if ((curTime - lastUpdate) > UPDATE_TIME_THRESHOLD) {
                // Actualiza el tiempo de la última llegada
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                // Calcula la velocidad en el eje x teniendo en cuenta la diferencia de tiempos
                float speed = Math.abs(x - last_x )/ diffTime * 10000;


                // Si la velocidad supera el umbral, realiza la acción
                if (speed > SHAKE_THRESHOLD) {
                    finish();

                }

                // Actualiza el último valor registrado
                last_x = x;
            }
        }
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
