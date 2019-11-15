package com.example.sensoresAlhambra.ui.exit;

import android.app.AlertDialog;;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sensoresAlhambra.R;

/**
 * Fragment que representa la opción de salir de la aplicación
 */
public class ExitFragment extends Fragment {

    /**
     * Método que pregunta al usuario si desea o no salir de la aplicación
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return Vista creada
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Obtener vista
        View root = inflater.inflate(R.layout.fragment_exit, container, false);

        // Establecer que tendrá opciones
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Establecer mensaje y opciones positivas (Sí) y negativas (No)
        builder.setMessage("¿Estás seguro que quieres salir de esta maravillosa app?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().onBackPressed();
                    }
                });
        builder.create();
        builder.show();
        return root;
    }
}