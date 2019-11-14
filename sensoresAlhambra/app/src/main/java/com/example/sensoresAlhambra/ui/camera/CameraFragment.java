package com.example.sensoresAlhambra.ui.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.sensoresAlhambra.BlueprintsActivity;
import com.example.sensoresAlhambra.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;



// Fragmento de cámara (source: GoogleGlass o similar)
public class CameraFragment extends Fragment {
    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    public static String lecturaQr;


    /**
     * Creación de la vista
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return Devuelve vista del padre
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_camera, container, false);

        surfaceView = (SurfaceView) root.findViewById(R.id.camerapreview);


        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
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


    /**
     *     Activamos el lector de QR en cámara
     *     Simula reconozimiento de objetos en tiempo real
     */

    public void activateCameraReader(){

        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource= new CameraSource.Builder(getContext(), barcodeDetector)
                .setAutoFocusEnabled(true).build();


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
            /**
             *
             */
            public void release() {

            }




            /**
             *
             *Función activada cada vez que recibe un código QR
             *  El código QR entrará en cola y se irán analizando uno a uno
             *  Luego de leer el código QR paramos la cámara para que no vuelva a entrar el mismo muchas veces en la cola
             *  Lanzamos la aplicación con la información y la valoración para el usuario
             *  Limpiamos la cola de QR por si existen copias del mismo QR
             * @param detections Cola de QRs
             */
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

                        lecturaQr = qrCodes.valueAt(qrCodes.size() - 1).displayValue;
                        Intent intent = new Intent(getContext(), BlueprintsActivity.class);
                        startActivity(intent);
                        qrCodes.clear();
                        }

                    });


                }

            }
        });
    }



}