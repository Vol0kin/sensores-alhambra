package com.example.sensoresAlhambra;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sensoresAlhambra.ui.camera.CameraFragment;

import java.io.IOException;

public class BlueprintsActivity extends AppCompatActivity {
    TextView tituloPlano, plantaPlano;
    ImageView imagenPlano;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScale = 1f;
    private Matrix mMatrix = new Matrix();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blueprints);

        // Variables que contendran informacion sobre la imagen a mostrar
        String fileName, title, information;

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

        // Mostrar la informacion
        this.showImageTextInfo(fileName, title, information);

        mScaleGestureDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
    }


    private void showImageTextInfo(String fileName, String title, String information) {

        // Obtener ImageView del plano
        imagenPlano = (ImageView) findViewById(R.id.titleInfo);

        // Intentar establecer imagen
        try {
            imagenPlano.setImageDrawable(Drawable.createFromStream( getAssets().open(fileName),null) );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Establecer titulo
        tituloPlano = (TextView) findViewById(R.id.imageInfo);
        tituloPlano.setText(title);

        // Establecer informacion
        plantaPlano = (TextView) findViewById(R.id.floorInfo);
        plantaPlano.setText(information);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mScaleGestureDetector.onTouchEvent(ev);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScale *= detector.getScaleFactor();
            mScale = Math.max(0.1f, Math.min(mScale, 5.0f));
            mMatrix.setScale(mScale, mScale);
            imagenPlano.setImageMatrix(mMatrix);
            return true;
        }
    }
}
