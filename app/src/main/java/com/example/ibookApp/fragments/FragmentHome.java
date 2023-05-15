package com.example.ibookApp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ibookApp.Adapters.ObraMaisComentadasAdapter;
import com.example.ibookApp.DAOs.ObrasDAO;
import com.example.ibookApp.DTOs.ObraDTO;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.Adapters.ObraAdapter;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.UserSingleton;
import com.example.ibookApp.functions.Utils;
import com.example.ibookApp.telas.telalogin;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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
    ImageView imgTeste;
    private Uri imageUri;
    private RecyclerView rvibook, rvibookmaiscomentados;
    private ObraAdapter adapterContact;
    private ObraMaisComentadasAdapter adapterContacts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        btnLogout = (Button)rootView.findViewById(R.id.btnLogoutHome);
        rvibook = (RecyclerView)rootView.findViewById(R.id.rviBook);
        rvibookmaiscomentados = (RecyclerView)rootView.findViewById(R.id.recycler_view_horizontal);
        UsuarioDTO userLogado = UserSingleton.getInstance().getUser();

        rvibook.setLayoutManager(new LinearLayoutManager(getContext()));

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        loadData();
        // Inflate the layout for this fragment
        return rootView;
    }

    public void logout(){
        Utils.logout();
        Intent acessar = new Intent(getActivity(), telalogin.class);
        startActivity(acessar);
    }

    private void loadData() {
        ObrasDAO obrasDAO = new ObrasDAO(getContext());
        ArrayList<ObraDTO> obrasList = new ArrayList<>();
        List<ObraDTO> obras = obrasDAO.carregarObras();
        for (ObraDTO obra : obras) {
            obrasList.add(obra);
        }

        ArrayList<ObraDTO> obrasMaisComentadasList = new ArrayList<>();
        List<ObraDTO> obrasMaisComentadas = obrasDAO.carregarObrasMaisComentadas();
        for (ObraDTO obraMaisComentadas : obrasMaisComentadas) {
            obrasMaisComentadasList.add(obraMaisComentadas);
        }

        adapterContacts = new ObraMaisComentadasAdapter(obrasMaisComentadasList);
        rvibookmaiscomentados.setAdapter(adapterContacts);

        adapterContact = new ObraAdapter(obrasList);
        rvibook.setAdapter(adapterContact);
    }
}