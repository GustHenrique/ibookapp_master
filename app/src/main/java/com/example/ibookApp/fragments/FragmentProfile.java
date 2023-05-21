package com.example.ibookApp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.UserSingleton;
import com.example.ibookApp.functions.Utils;
import com.example.ibookApp.telas.telalogin;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProfile extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentProfile.
     */
    public static FragmentProfile newInstance(String param1, String param2) {
        FragmentProfile fragment = new FragmentProfile();
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
    Button btnLogout;
    ImageView imgProfile;
    TextView txtNome;
    private Uri imageUri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        btnLogout = (Button)rootView.findViewById(R.id.btnLogoutHome);
        txtNome = (TextView) rootView.findViewById(R.id.txtNomeUsuarioProfile);
        imgProfile = rootView.findViewById(R.id.imgProfile);
        UsuarioDTO userLogado = UserSingleton.getInstance().getUser();
        txtNome.setText("Olá " + userLogado.getUsunome() + "!");
        imageUri = Uri.parse(userLogado.getUsuimagem());
        if (!imageUri.equals(null) || !imageUri.equals("")){
            imgProfile.setImageURI(imageUri);
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    public void logout(){
        Utils.logout();
        Intent acessar = new Intent(getActivity(), telalogin.class);
        startActivity(acessar);
    }
}