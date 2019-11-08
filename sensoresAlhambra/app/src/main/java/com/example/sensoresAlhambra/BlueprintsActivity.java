package com.example.sensoresAlhambra;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sensoresAlhambra.ui.camera.CameraFragment;

import java.io.IOException;

public class BlueprintsActivity extends AppCompatActivity {
    TextView tituloPlano, plantaPlano;
    ImageView imagenPlano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blueprints);

        if (CameraFragment.lecturaQr.equals("fuente")){
            imagenPlano = (ImageView) findViewById(R.id.imagenPlano);
            try {
                imagenPlano.setImageDrawable(Drawable.createFromStream( getAssets().open("fuente.jpg"),null) );
            } catch (IOException e) {
                e.printStackTrace();
            }

            tituloPlano = (TextView) findViewById(R.id.tituloPlano);
            tituloPlano.setText("fuente");
            plantaPlano = (TextView) findViewById(R.id.plantaPlano);
            String information = "Planta baja";
            plantaPlano.setText(information);
        }
        else if (CameraFragment.lecturaQr.equals("palacio")){
            imagenPlano = (ImageView) findViewById(R.id.imagenPlano);
            try {
                imagenPlano.setImageDrawable(Drawable.createFromStream( getAssets().open("palacio.jpg"),null) );
            } catch (IOException e) {
                e.printStackTrace();
            }

            tituloPlano = (TextView) findViewById(R.id.tituloPlano);
            tituloPlano.setText("Palacio de Carlos V");
            plantaPlano = (TextView) findViewById(R.id.plantaPlano);
            String information = "Planta 2";
            plantaPlano.setText(information);
        }
    }
}
