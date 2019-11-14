package com.example.sensoresAlhambra.ui.exit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sensoresAlhambra.R;

public class ExitFragment extends Fragment {

    /**
     * Asegura si el usuario necesita salir
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_exit, container, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Estas seguro que quieres salir de esta maravillosa app?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
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