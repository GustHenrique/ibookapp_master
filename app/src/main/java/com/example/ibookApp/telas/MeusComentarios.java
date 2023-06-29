package com.example.ibookApp.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ibookApp.APIs.ComentariosPorUsuarioApi;
import com.example.ibookApp.APIs.comentariosPorLivroApi;
import com.example.ibookApp.Adapters.ComentarioAdapter;
import com.example.ibookApp.Adapters.MyComentarioAdapter;
import com.example.ibookApp.Adapters.MyComentarioSearchAdapter;
import com.example.ibookApp.Adapters.ObraAdapter;
import com.example.ibookApp.DTOs.ComentarioDTO;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.fragments.FragmentProfile;
import com.example.ibookApp.functions.ObrasListSingleton;
import com.example.ibookApp.functions.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MeusComentarios extends AppCompatActivity {

    private Button btnLogout, btnHome, btnFazerPrimeiroComentario;
    private EditText lblSearchMeusComentarios;
    private TextView txtSemComentario;
    private RecyclerView rviMyComents;
    private MyComentarioAdapter myComentarioAdapter;
    private MyComentarioSearchAdapter searchAdapter;
    ArrayList<obrasDTO> obrasList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_comentarios);
        btnLogout = findViewById(R.id.btnLogoutDetalhesObra);
        btnHome = findViewById(R.id.btnBackHome);
        rviMyComents = findViewById(R.id.rviMyComents);
        txtSemComentario = findViewById(R.id.txtSemComentario);
        btnFazerPrimeiroComentario = findViewById(R.id.btnFazerPrimeiroComentario);
        lblSearchMeusComentarios = findViewById(R.id.txtSearchMeusComentarios);
        carregarComentarios();

        btnFazerPrimeiroComentario.setOnClickListener(new View.OnClickListener() {
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

        lblSearchMeusComentarios.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não é necessário implementar este método
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Chamado quando o texto na barra de pesquisa é alterado
                String searchText = s.toString();
                // Chame a função de filtro passando o texto digitado
                filterData(searchText);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não é necessário implementar este método
            }
        });
    }

    private void filterData(String searchText) {
        rviMyComents.setLayoutManager(new LinearLayoutManager(this));
        rviMyComents.setAdapter(searchAdapter);
        searchAdapter.filterData(searchText);
    }

    public void carregarComentarios(){
        List<ComentarioDTO> comentarioList = new ArrayList<>();
        List<obrasDTO> obrasNames = new ArrayList<>();
        obrasList = ObrasListSingleton.getInstance().getObrasList();
        ComentariosPorUsuarioApi.getBookList(new ComentariosPorUsuarioApi.BookListListener() {
            @Override
            public void onBookListReceived(List<ComentarioDTO> bookList) {
                comentarioList.addAll(bookList);
                for (obrasDTO obra:obrasList) {
                    for (ComentarioDTO coment : comentarioList){
                        if (coment.getObid().equals(obra.getId())){
                            obrasNames.add(obra);
                        }
                    }
                }

                ordenarComentariosPorData(comentarioList);
                myComentarioAdapter = new MyComentarioAdapter(comentarioList, obrasNames);
                searchAdapter = new MyComentarioSearchAdapter(comentarioList, obrasNames);
                if (comentarioList.size() == 0){
                    txtSemComentario.setVisibility(View.VISIBLE);
                    btnFazerPrimeiroComentario.setVisibility(View.VISIBLE);
                }
                rviMyComents.setAdapter(myComentarioAdapter);
                myComentarioAdapter.setOnItemClickListener(new MyComentarioAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, obrasDTO obra) {
                        Intent intent = new Intent(MeusComentarios.this, telaDetalhesObra.class);
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
        });
    }

    public void ordenarComentariosPorData(List<ComentarioDTO> comentarios) {
        Collections.sort(comentarios, new Comparator<ComentarioDTO>() {
            public int compare(ComentarioDTO c1, ComentarioDTO c2) {
                Date data1 = c1.getDataComentario();
                Date data2 = c2.getDataComentario();
                return data2.compareTo(data1);
            }
        });
    }

    public void logout(){
        Utils.logout();
        Intent acessar = new Intent(getApplicationContext(), telalogin.class);
        startActivity(acessar);
    }
}