package com.example.sensoresAlhambra.ui.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.sensoresAlhambra.BlueprintsActivity;
import com.example.sensoresAlhambra.InfoActivity;
import com.example.sensoresAlhambra.R;
import com.example.sensoresAlhambra.ui.navigation.NavigationFragment;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Arrays;

public class CameraFragment extends Fragment {
    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    TextView textView;
    private CameraViewModel cameraViewModel;
    public static String lecturaQr;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        cameraViewModel =
                ViewModelProviders.of(this).get(CameraViewModel.class);
        View root = inflater.inflate(R.layout.fragment_camera, container, false);

        surfaceView = (SurfaceView) root.findViewById(R.id.camerapreview);


        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        //cameraSource.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        activateCameraReader();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void activateCameraReader(){
        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource= new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(640,480).setAutoFocusEnabled(true).build();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                if(ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                try {
                    cameraSource.start(holder);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();


                if(qrCodes.size() >= 1) {
                    surfaceView.post(new Runnable() {
                        @Override
                        public void run() {
                        Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(50);
                        cameraSource.stop(); // ADD THIS. THIS WILL STOP CAMERA
                        //barcodeDetector.release();

                        lecturaQr = qrCodes.valueAt(qrCodes.size() - 1).displayValue;
                        Toast.makeText(getContext(), lecturaQr, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), BlueprintsActivity.class);
                        startActivity(intent);
                        qrCodes.clear();
                        }
                        //Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(qrCodes.valueAt(qrCodes.size()-1).displayValue));
                        //startActivity(myIntent);
                        //qrCodes.clear();

                    });


                }

            }
        });
    }



}