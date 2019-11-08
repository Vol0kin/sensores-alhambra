package com.example.sensoresAlhambra.ui.maps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import java.util.Arrays;

public class MapsFragment extends Fragment implements SensorEventListener{
    public static String imagen = "image.jpg";

    private MapsViewModel  mapsViewModel;
    private VrPanoramaView mVRPanoramaView;
    private SensorManager sensorManager;
    public boolean loadImageSuccessful;
    private ImageButton buttonPanoView;

    private float[] mHeadRotation = new float[2];

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 200;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapsViewModel =
                ViewModelProviders.of(this).get(MapsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_maps, container, false);
        mVRPanoramaView = (VrPanoramaView) root.findViewById(R.id.vrPanoramaView);
        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        loadPhotoSphere("image.jpg");
        mVRPanoramaView.setEventListener(new ActivityEventListener());
        mVRPanoramaView.getHeadRotation(mHeadRotation);

        buttonPanoView = root.findViewById(R.id.botonPanoView);
        buttonPanoView.setVisibility(View.INVISIBLE);



        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        mVRPanoramaView.pauseRendering();

        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mVRPanoramaView.resumeRendering();

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
        mVRPanoramaView.shutdown();
        super.onDestroy();
    }



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
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }

    // Get readings from accelerometer and magnetometer. To simplify calculations,
    // consider storing these readings as unit vectors.
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 250) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(y - last_y )/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD && !imagen.equals("image.jpg")) {
                    Intent intent = new Intent(getContext(), InfoActivity.class);
                    startActivity(intent);
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }


        mVRPanoramaView.getHeadRotation(mHeadRotation);
        updateReticule();
    }

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

    private class ActivityEventListener extends VrPanoramaEventListener {
        /**
         * Called by pano widget on the UI thread when it's done loading the image.
         */
        @Override
        public void onLoadSuccess() {
            loadImageSuccessful = true;
        }

        /**
         * Called by pano widget on the UI thread on any asynchronous error.
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