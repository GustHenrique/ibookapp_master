package com.example.ibookApp.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ibookApp.APIs.obrasPorUsuarioApi;
import com.example.ibookApp.Adapters.ObraAdapter;
import com.example.ibookApp.Adapters.ObraMaisComentadasAdapter;
import com.example.ibookApp.Adapters.SearchAdapter;
import com.example.ibookApp.DTOs.favoritosDTO;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.fragments.FragmentEdit;
import com.example.ibookApp.fragments.FragmentProfile;
import com.example.ibookApp.functions.FavoritosListSingleton;
import com.example.ibookApp.functions.ObrasListSingleton;
import com.example.ibookApp.functions.Utils;

import java.util.ArrayList;
import java.util.List;

public class LivrosCadastrados extends AppCompatActivity {

    private Button btnLogout, btnHome, btnCadastrarLivro;

    private TextView lblCadastrarLivro;
    private EditText edtSearchLivrosCadastrados;

    private RecyclerView rviObrasCadastradas;

    private ObraAdapter adapterContact;
    private ArrayList<obrasDTO> filteredObrasList;
    private SearchAdapter searchAdapter;

    ArrayList<obrasDTO> obrasList = new ArrayList<>();
    ArrayList<favoritosDTO> favLists = FavoritosListSingleton.getInstance().getFavList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livros_cadastrados);
        btnCadastrarLivro = findViewById(R.id.btnCadastrarObraLivros);
        lblCadastrarLivro = findViewById(R.id.txtSemLivro);
        btnLogout = findViewById(R.id.btnLogoutDetalhesObra);
        edtSearchLivrosCadastrados = findViewById(R.id.txtSearchLivrosCadastrados);
        rviObrasCadastradas = findViewById(R.id.rviObrasCadastradas);
        btnHome = findViewById(R.id.btnBackHome);

        obrasPorUsuarioApi.getBookList(new obrasPorUsuarioApi.BookListListener() {
            @Override
            public void onBookListReceived(List<obrasDTO> bookList) {
                obrasList.addAll(bookList);
                filteredObrasList = new ArrayList<>(obrasList);
                loadData();
                adapterContact.setOnItemClickListener(new ObraAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        obrasDTO obra = obrasList.get(position);
                        Intent intent = new Intent(LivrosCadastrados.this, telaDetalhesObra.class);

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

                edtSearchLivrosCadastrados.addTextChangedListener(new TextWatcher() {
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
            }
        });

        btnCadastrarLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Acessar = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(Acessar);

            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Acessar = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(Acessar);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void filterData(String searchText) {
        filteredObrasList.clear();

        for (obrasDTO item : obrasList) {
            if (item.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                filteredObrasList.add(item);
            }
        }
        searchAdapter = new SearchAdapter(filteredObrasList,favLists);
        rviObrasCadastradas.setAdapter(searchAdapter);
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                obrasDTO obra = filteredObrasList.get(position);
                Intent intent = new Intent(LivrosCadastrados.this, telaDetalhesObra.class);

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
        adapterContact = new ObraAdapter(obrasList, favLists);
        rviObrasCadastradas.setAdapter(adapterContact);

        if (obrasList.size() == 0){
            lblCadastrarLivro.setVisibility(View.VISIBLE);
            btnCadastrarLivro.setVisibility(View.VISIBLE);
        }

    }

    public void logout(){
        Utils.logout();
        Intent acessar = new Intent(getApplicationContext(), telalogin.class);
        startActivity(acessar);
    }
}