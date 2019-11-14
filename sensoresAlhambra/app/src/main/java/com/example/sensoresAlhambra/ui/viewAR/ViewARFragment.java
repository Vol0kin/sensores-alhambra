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
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.sensoresAlhambra.InfoActivity;
import com.example.sensoresAlhambra.R;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.InputStream;

public class ViewARFragment extends Fragment implements SensorEventListener{
    public static String imagen = "image.jpg";
    private VrPanoramaView mVRPanoramaView;
    private SensorManager sensorManager;
    public boolean loadImageSuccessful;
    private ImageButton buttonPanoView;


    /**
     * Rotación de la cabeza en grados
     */
    private float[] mHeadRotation = new float[2];

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 200;


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

        // Cargamos la foto elegida
        loadPhotoSphere("image.jpg");

        // Creamos un listener
        mVRPanoramaView.setEventListener(new ActivityEventListener());
        mVRPanoramaView.getHeadRotation(mHeadRotation);

        // Creamos botón
        buttonPanoView = root.findViewById(R.id.botonPanoView);
        buttonPanoView.setVisibility(View.INVISIBLE);

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
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener((SensorEventListener) this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onDestroy() {
        mVRPanoramaView.shutdown();
        super.onDestroy();
    }


    /**
     * Carga la foto en el manager
     * @param foto
     */
    private void loadPhotoSphere(String foto) {
        VrPanoramaView.Options options = new VrPanoramaView.Options();
        InputStream inputStream = null;
        AssetManager assetManager = this.getContext().getAssets();

        try {
            inputStream = assetManager.open(foto);
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
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            // Chequea cada cuanto actualizamos las imagenes
            if ((curTime - lastUpdate) > 250) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(y - last_y )/ diffTime * 10000;

                // Establecemos un mínimo de velocidad y comprobamos la imagen
                if (speed > SHAKE_THRESHOLD && !imagen.equals("image.jpg")) {
                    Intent intent = new Intent(getContext(), InfoActivity.class);
                    startActivity(intent);
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }


        // Actualizamos el vector y comprobamos hacia donde estamos mirando
        mVRPanoramaView.getHeadRotation(mHeadRotation);
        // Actualizamos (si hace falta)
        updateReticule();
    }

    /**
     * Comprobamos la rotación en los diferentes ejes.
     * Dependiendo de los valores, comprovamos que imagen está cargada y,
     * si se necesita, cargamos una nueva
     */
    private void updateReticule() {
        if(mHeadRotation[1] > -60 && mHeadRotation[1] < -20 && mHeadRotation[0] > -20 && mHeadRotation[0] < 20){
            if (!imagen.equals("image2.jpg")) {
                imagen = "image2.jpg";
                loadPhotoSphere("image2.jpg");
            }
        }
        else if(mHeadRotation[1] > 0 && mHeadRotation[1] < 30 && mHeadRotation[0] > -20 && mHeadRotation[0] < 20){
            if (!imagen.equals("image3.jpg")) {
                imagen = "image3.jpg";
                loadPhotoSphere("image3.jpg");
            }
        }
        else{
            if (!imagen.equals("image.jpg")) {
                imagen = "image.jpg";
                loadPhotoSphere("image.jpg");
            }
        }
    }

    /**
     * Listener del visor
     */
    private class ActivityEventListener extends VrPanoramaEventListener {
        /**
         * Esta función es llamada luego de cargar la imagen
         */
        @Override
        public void onLoadSuccess() {
            loadImageSuccessful = true;
        }

        /**
         * Es llamada si existe un error asíncrono (normalmente al cargar)
         */
        @Override
        public void onLoadError(String errorMessage) {
            loadImageSuccessful = false;
            Toast toast1 =
                    Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT);
            toast1.show();
        }
    }




}