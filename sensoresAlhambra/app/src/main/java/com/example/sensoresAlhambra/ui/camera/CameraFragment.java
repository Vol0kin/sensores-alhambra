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


/**
 * Fragmento de cámara (source: GoogleGlass o similar)
 */
public class CameraFragment extends Fragment {
    /**
     * Superficie donde se mostrará la cámara
     */
    SurfaceView surfaceView;

    /**
     * Recibir información de la cámara
     */
    CameraSource cameraSource;

    /**
     * Detector de códigos QR
     */
    BarcodeDetector barcodeDetector;

    /**
     * Variable que contiene la lectura de un QR en formato String
     */
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

    /**
     * Método llamado cuando se pausa la actividad
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Método llamado cuando se reanuda la actividad. Activa la cámara
     */
    @Override
    public void onResume() {
        super.onResume();
        activateCameraReader();
    }

    /**
     * Método llamado cuando se destruye la actividad
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * Activamos el lector de QR en cámara
     * Simula reconocimiento de objetos en tiempo real
     */
    public void activateCameraReader() {
        // Establecer detector de QR
        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.QR_CODE).build();

        // Establecer fuente de la cámara
        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setAutoFocusEnabled(true).build();

        // Establecer superficie
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

        // Establecer acciones del lector de QR
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() { }

            /**
             *
             * Método activado cada vez que recibe un código QR
             * El código QR entrará en cola y se irán analizando uno a uno
             * Luego de leer el código QR paramos la cámara para que no vuelva a entrar el mismo muchas veces en la cola
             * Lanzamos la aplicación con la información y la valoración para el usuario
             * Limpiamos la cola de QR por si existen copias del mismo QR
             * @param detections Cola de QRs
             */
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                // Lectura de elementos detectados
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                // Si se ha deteactado algo, procesar
                if(qrCodes.size() == 1) {
                    surfaceView.post(new Runnable() {
                        @Override
                        public void run() {
                            // Establecer vibración de 50 ms
                            Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(50);

                            // Parar lectura de códigos
                            cameraSource.stop();

                            // Obtener valor leído
                            lecturaQr = qrCodes.valueAt(0).displayValue;

                            // Lanzar BlueprintsActivity
                            Intent intent = new Intent(getContext(), BlueprintsActivity.class);
                            startActivity(intent);

                            // Limpiar todos los códigos QR leídos
                            qrCodes.clear();
                        }
                    });
                }
            }
        });
    }



}