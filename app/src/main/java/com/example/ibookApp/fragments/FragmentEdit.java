package com.example.ibookApp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ibookApp.R;
import com.example.ibookApp.functions.Utils;
import com.example.ibookApp.telas.MainActivity;
import com.example.ibookApp.telas.telalogin;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentEdit extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentEdit.
     */
    public static FragmentEdit newInstance(String param1, String param2) {
        FragmentEdit fragment = new FragmentEdit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    TextView tvCadastroObraCategorias;
    boolean[] selectedCategorias;
    ArrayList<Integer> categoriasList = new ArrayList<>();
    String[] categoriaArray = {"item1", "item1", "item1", "item1", "item1"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit, container, false);
        tvCadastroObraCategorias = (TextView)rootView.findViewById(R.id.txtCadastroObraCategorias);
        tvCadastroObraCategorias.setKeyListener(null);
        selectedCategorias = new boolean[categoriaArray.length];

        tvCadastroObraCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Categoria Selecionada");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(categoriaArray, selectedCategorias, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i, boolean b) {
                        if (b){
                            categoriasList.add(i);
                            Collections.sort(categoriasList);
                        }
                        else{
                            categoriasList.remove(i);
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j=0; j<categoriasList.size(); j++){
                            stringBuilder.append(categoriaArray[categoriasList.get(j)]);

                            if (j != categoriasList.size()-1){
                                stringBuilder.append(", ");
                            }
                        }
                        tvCadastroObraCategorias.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Limpar Todos", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        for (int j=0; j< selectedCategorias.length; j++){
                            selectedCategorias[j] = false;
                            categoriasList.clear();
                            tvCadastroObraCategorias.setText("");
                        }
                    }
                });
                builder.show();
            }
        });


        return rootView;
    }

    public void logout(){
        Utils.logout();
        Intent acessar = new Intent(getActivity(), telalogin.class);
        startActivity(acessar);
    }
}