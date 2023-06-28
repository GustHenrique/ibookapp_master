package com.example.ibookApp.fragments;

import android.content.Intent;
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

import com.example.ibookApp.APIs.favoritosPorUsuario;
import com.example.ibookApp.APIs.ibookApi;
import com.example.ibookApp.Adapters.ObraAdapter;
import com.example.ibookApp.Adapters.SearchAdapter;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.DTOs.favoritosDTO;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.FavoritosListSingleton;
import com.example.ibookApp.functions.ObrasListSingleton;
import com.example.ibookApp.functions.UserSingleton;
import com.example.ibookApp.functions.Utils;
import com.example.ibookApp.telas.telaDetalhesObra;
import com.example.ibookApp.telas.telalogin;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearch extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSearch.
     */
    public static FragmentSearch newInstance(String param1, String param2) {
        FragmentSearch fragment = new FragmentSearch();
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
    private EditText searchEditText;
    private RecyclerView rviBookSearch;
    private ObraAdapter adapterContact;
    private Button btnLogout;
    ArrayList<obrasDTO> obrasList = ObrasListSingleton.getInstance().getObrasList();
    UsuarioDTO userLogado = UserSingleton.getInstance().getUser();
    ArrayList<favoritosDTO> favLists = FavoritosListSingleton.getInstance().getFavList();
    private ArrayList<obrasDTO> filteredObrasList;
    private SearchAdapter searchAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        rviBookSearch = (RecyclerView)rootView.findViewById(R.id.rviBookSearch);
        btnLogout = (Button)rootView.findViewById(R.id.btnLogoutDetalhesObra);
        rviBookSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        filteredObrasList = new ArrayList<>(obrasList);
        searchEditText = (EditText) rootView.findViewById(R.id.edtSearchObra);
        adapterContact = new ObraAdapter(obrasList, favLists);
        searchAdapter = new SearchAdapter(filteredObrasList,favLists);

        loadData();

        adapterContact.setOnItemClickListener(new ObraAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                obrasDTO obra = obrasList.get(position);
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
        searchAdapter = new SearchAdapter(filteredObrasList,favLists);
        rviBookSearch.setAdapter(searchAdapter);

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

    private void loadData() {

        favoritosPorUsuario.getfavoritosPorUsuario(userLogado.getId(), new favoritosPorUsuario.favoritosPorUsuarioListener() {
            @Override
            public void onfavoritosPorUsuarioReceived(List<favoritosDTO> favoritosPorUsuario) {
                favLists.addAll(favoritosPorUsuario);
            }
        });


        ArrayList<obrasDTO> obrasFeedList = new ArrayList<>();
        for (int i = 0; i < obrasList.size(); i++) {
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
        adapterContact = new ObraAdapter(obrasFeedList, favLists);
        rviBookSearch.setAdapter(adapterContact);
    }

    public void logout(){
        Utils.logout();
        Intent acessar = new Intent(getActivity(), telalogin.class);
        startActivity(acessar);
    }
}