package com.example.sensoresAlhambra.ui.viewAR;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sensoresAlhambra.InfoActivity;
import com.example.sensoresAlhambra.R;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.InputStream;

public class ViewARFragment extends Fragment implements SensorEventListener{
    /**
     * Imagen a mostrar en cada momento
     */
    public static String imagen = "image.jpg";

    /**
     * VR asociado a la imagen correspondiente
     */
    private VrPanoramaView mVRPanoramaView;

    /**
     * Gestor de sensores
     */
    private SensorManager sensorManager;


    /**
     * Rotación de la cabeza en grados
     */
    private float[] mHeadRotation = new float[2];

    /**
     * Último instante en el que se ha actualizado el sensor
     */
    private long lastUpdate = 0;

    /**
     * Último valor de Y registrado con el acelerómetro
     */
    private float last_y;

    /**
     * Umbral del acelerómetro
     */
    private static final int SHAKE_THRESHOLD = 200;

    /**
     * Umbral de tiempo entre actualizaciones
     */
    private static final long UPDATE_TIME_THRESHOLD = 250;


    /**
     * Utilizamos la API de Google para cargar una imagen en un visor de 360º
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewar, container, false);
        mVRPanoramaView = (VrPanoramaView) root.findViewById(R.id.vrPanoramaView);

        // Cargamos los sensores
        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);

        // Cargamos la foto inicial
        loadPhotoSphere();

        // Creamos un listener
        mVRPanoramaView.setEventListener(new VrPanoramaEventListener ());
        mVRPanoramaView.getHeadRotation(mHeadRotation);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        mVRPanoramaView.pauseRendering();

        // Parar los listeners
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mVRPanoramaView.resumeRendering();

        /**
         * Volvemos a registrar los listener para tener sensores para el visor.
         * Establecemos el intervalo entre actualizaciones.
         */
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener((SensorEventListener) this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onDestroy() {
        mVRPanoramaView.shutdown();
        super.onDestroy();
    }


    /**
     * Carga la imagen en el manager
     */
    private void loadPhotoSphere() {
        VrPanoramaView.Options options = new VrPanoramaView.Options();
        InputStream inputStream = null;
        AssetManager assetManager = this.getContext().getAssets();

        try {
            inputStream = assetManager.open(imagen);
            options.inputType = VrPanoramaView.Options.TYPE_MONO;
            mVRPanoramaView.loadImageFromBitmap(BitmapFactory.decodeStream(inputStream), options);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // En caso de que la precisión cambie
    }

    /**
     * Actualizamos los valores a partir de los sensores cuando cambian
     * @param sensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float y = sensorEvent.values[1];

            long curTime = System.currentTimeMillis();

            // La próxima señal que se leerá será una con 250 milisegundos de diferencia
            if ((curTime - lastUpdate) > UPDATE_TIME_THRESHOLD) {
                // Actualiza el tiempo de la última llegada
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(y - last_y )/ diffTime * 10000;

                // Establecemos un mínimo de velocidad y comprobamos la imagen
                if (speed > SHAKE_THRESHOLD && !imagen.equals("image.jpg")) {
                    Intent intent = new Intent(getContext(), InfoActivity.class);
                    startActivity(intent);
                }

                // Actualiza último registro del eje Y
                last_y = y;
            }
        }


        // Actualizamos el vector y comprobamos hacia donde estamos mirando
        mVRPanoramaView.getHeadRotation(mHeadRotation);
        // Actualizamos (si hace falta)
        updateReticule();
    }

    /**
     * Comprobamos la rotación en los diferentes ejes.
     * Dependiendo de los valores, comprobamos que imagen está cargada y,
     * si se necesita, cargamos una nueva
     */
    private void updateReticule() {
        if(mHeadRotation[1] > -60 && mHeadRotation[1] < -20 && mHeadRotation[0] > -20 && mHeadRotation[0] < 20){
            if (!imagen.equals("image2.jpg")) {
                imagen = "image2.jpg";
                loadPhotoSphere();
            }
        }
        else if(mHeadRotation[1] > 0 && mHeadRotation[1] < 30 && mHeadRotation[0] > -20 && mHeadRotation[0] < 20){
            if (!imagen.equals("image3.jpg")) {
                imagen = "image3.jpg";
                loadPhotoSphere();
            }
        }
        else{
            if (!imagen.equals("image.jpg")) {
                imagen = "image.jpg";
                loadPhotoSphere();
            }
        }
    }
}