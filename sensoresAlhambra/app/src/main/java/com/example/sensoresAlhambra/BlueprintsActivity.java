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

    private void showImageTextInfo(String fileName, String title, String information) {

        // Obtener ImageView del plano
        imagenPlano = (ImageView) findViewById(R.id.tituloPlano);

        // Intentar establecer imagen
        try {
            imagenPlano.setImageDrawable(Drawable.createFromStream( getAssets().open(fileName),null) );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Establecer titulo
        tituloPlano = (TextView) findViewById(R.id.imagenPlano);
        tituloPlano.setText(title);

        // Establecer informacion
        plantaPlano = (TextView) findViewById(R.id.plantaPlano);
        plantaPlano.setText(information);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blueprints);

        // Variables que contendran informacion sobre la imagen a mostrar
        String fileName, title, information;


        // Dependiendo del resultado del lector QR, actualizamos la imágen y la información
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

        // Mostramos dependiendo de las especificaciones
        this.showImageTextInfo(fileName, title, information);
    }
}
