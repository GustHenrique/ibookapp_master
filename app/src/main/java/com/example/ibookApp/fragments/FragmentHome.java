package com.example.ibookApp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ibookApp.APIs.ibookApi;
import com.example.ibookApp.Adapters.ObraMaisComentadasAdapter;
import com.example.ibookApp.Adapters.SearchAdapter;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.Adapters.ObraAdapter;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.ObrasListSingleton;
import com.example.ibookApp.functions.UserSingleton;
import com.example.ibookApp.functions.Utils;
import com.example.ibookApp.telas.telaDetalhesObra;
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
    private EditText searchEditText;
    private RecyclerView rvibook, rvibookmaiscomentados;
    private ObraAdapter adapterContact;
    private ObraMaisComentadasAdapter adapterContacts;
    private ArrayList<obrasDTO> filteredObrasList;
    private SearchAdapter searchAdapter;
    ArrayList<obrasDTO> obrasList = ObrasListSingleton.getInstance().getObrasList();
    ArrayList<obrasDTO> obrasMaisComentadasList = new ArrayList<>();
    ArrayList<obrasDTO> obrasFeedList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        btnLogout = (Button)rootView.findViewById(R.id.btnLogoutHome);
        rvibook = (RecyclerView)rootView.findViewById(R.id.rviBook);
        rvibookmaiscomentados = (RecyclerView)rootView.findViewById(R.id.recycler_view_horizontal);
        UsuarioDTO userLogado = UserSingleton.getInstance().getUser();
        rvibook.setLayoutManager(new LinearLayoutManager(getContext()));
        filteredObrasList = new ArrayList<>(obrasList);
        searchEditText = (EditText) rootView.findViewById(R.id.txtEmailLogin);
        adapterContact = new ObraAdapter(obrasFeedList);
        searchAdapter = new SearchAdapter(filteredObrasList);
        loadData();
        adapterContacts.setOnItemClickListener(new ObraAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                obrasDTO obra = obrasFeedList.get(position);
                Intent intent = new Intent(getContext(), telaDetalhesObra.class);

                intent.putExtra("obid", obra.getId());
                intent.putExtra("title", obra.getTitle());
                intent.putExtra("subtitle", obra.getSubtitle());
                intent.putExtra("synopsis", obra.getSynopsis());
                intent.putExtra("author", obra.getAuthor());
                intent.putExtra("editora", obra.getEditora());
                intent.putExtra("dataPublicacao", obra.getDataPublicacao());
                intent.putExtra("dataFinalizacao", obra.getDataFinalizacao());
                intent.putExtra("isbn", obra.getIsbn());
                intent.putExtra("paginas", obra.getPaginas());
                intent.putExtra("image", obra.getImage());
                intent.putExtra("tipo", obra.getType());
                intent.putExtra("avarageRating", obra.getAvarageRating());
                intent.putExtra("statusObra", obra.getStatus());
                intent.putExtra("categorias", obra.getCategorias());
                startActivity(intent);
            }
        });
        adapterContact.setOnItemClickListener(new ObraAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                obrasDTO obra = obrasFeedList.get(position);
                Intent intent = new Intent(getContext(), telaDetalhesObra.class);

                intent.putExtra("obid", obra.getId());
                intent.putExtra("title", obra.getTitle());
                intent.putExtra("subtitle", obra.getSubtitle());
                intent.putExtra("synopsis", obra.getSynopsis());
                intent.putExtra("author", obra.getAuthor());
                intent.putExtra("editora", obra.getEditora());
                intent.putExtra("dataPublicacao", obra.getDataPublicacao());
                intent.putExtra("dataFinalizacao", obra.getDataFinalizacao());
                intent.putExtra("isbn", obra.getIsbn());
                intent.putExtra("paginas", obra.getPaginas());
                intent.putExtra("image", obra.getImage());
                intent.putExtra("tipo", obra.getType());
                intent.putExtra("avarageRating", obra.getAvarageRating());
                intent.putExtra("statusObra", obra.getStatus());
                intent.putExtra("categorias", obra.getCategorias());
                startActivity(intent);
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não é necessário implementar este método
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Chamado quando o texto na barra de pesquisa é alterado
                String searchText = s.toString();
                // Chame a função de pesquisa passando o texto digitado
                filterData(searchText);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não é necessário implementar este método
            }
        });

        if (obrasList.size() == 0) {
            ibookApi.getBookList(new ibookApi.BookListListener() {
                @Override
                public void onBookListReceived(List<obrasDTO> bookList) {
                    ObrasListSingleton obrasSingleton = ObrasListSingleton.getInstance();
                    for (obrasDTO obra : bookList) {
                        obrasSingleton.adicionarObra(obra);
                    }
                }
            });
        }

        if (obrasList.size() == 0){
            ibookApi.getBookList(new ibookApi.BookListListener() {
                @Override
                public void onBookListReceived(List<obrasDTO> bookList) {
                    ObrasListSingleton obrasSingleton = ObrasListSingleton.getInstance();
                    for (obrasDTO obra : bookList) {
                        obrasSingleton.adicionarObra(obra);
                    }
                }
            });
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        return rootView;
    }

    private void filterData(String searchText) {
        filteredObrasList.clear();

        for (obrasDTO item : obrasList) {
            if (item.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                filteredObrasList.add(item);
            }
        }
        searchAdapter = new SearchAdapter(filteredObrasList);
        rvibook.setAdapter(searchAdapter);
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                obrasDTO obra = filteredObrasList.get(position);
                Intent intent = new Intent(getContext(), telaDetalhesObra.class);

                intent.putExtra("obid", obra.getId());
                intent.putExtra("title", obra.getTitle());
                intent.putExtra("subtitle", obra.getSubtitle());
                intent.putExtra("synopsis", obra.getSynopsis());
                intent.putExtra("author", obra.getAuthor());
                intent.putExtra("editora", obra.getEditora());
                intent.putExtra("dataPublicacao", obra.getDataPublicacao());
                intent.putExtra("dataFinalizacao", obra.getDataFinalizacao());
                intent.putExtra("isbn", obra.getIsbn());
                intent.putExtra("paginas", obra.getPaginas());
                intent.putExtra("image", obra.getImage());
                intent.putExtra("tipo", obra.getType());
                intent.putExtra("avarageRating", obra.getAvarageRating());
                intent.putExtra("statusObra", obra.getStatus());
                intent.putExtra("categorias", obra.getCategorias());
                startActivity(intent);
            }
        });
    }

    public void logout(){
        Utils.logout();
        Intent acessar = new Intent(getActivity(), telalogin.class);
        startActivity(acessar);
    }

    private void loadData() {
        obrasFeedList = new ArrayList<>();
        int limite = Math.min(obrasList.size(), 10);
        for (int i = 0; i < limite; i++) {
            obrasDTO obra = obrasList.get(i);
            if (obra.getType() == "MANGA"){
                String novoTitulo = obra.getTitle().replace("-", " ");
                obra.setTitle(novoTitulo);
            }
            if (obra.getAuthor().endsWith(",")) {
                String novoAuthor = obra.getAuthor().substring(0, obra.getAuthor().length() - 1).trim();
                obra.setAuthor(novoAuthor);
            }
            obrasFeedList.add(obra);
        }

        obrasMaisComentadasList = new ArrayList<>();
        //List<obrasDTO> obrasMaisComentadas = obrasDAO.carregarObrasMaisComentadas();
        int limiteMaisComentado = Math.min(obrasList.size(), 5);
        for (int i = 0; i < limiteMaisComentado; i++) {
            obrasDTO obraMaisComentadas = obrasList.get(i);
            if (obraMaisComentadas.getType() == "MANGA"){
                String novoTitulo = obraMaisComentadas.getTitle().replace("-", " ");
                obraMaisComentadas.setTitle(novoTitulo);
            }
            if (obraMaisComentadas.getAuthor().endsWith(",")) {
                String novoAuthor = obraMaisComentadas.getAuthor().substring(0, obraMaisComentadas.getAuthor().length() - 1).trim();
                obraMaisComentadas.setAuthor(novoAuthor);
            }
            obrasMaisComentadasList.add(obraMaisComentadas);
        }

        adapterContacts = new ObraMaisComentadasAdapter(obrasMaisComentadasList);
        rvibookmaiscomentados.setAdapter(adapterContacts);

        adapterContact = new ObraAdapter(obrasFeedList);
        rvibook.setAdapter(adapterContact);
    }

}