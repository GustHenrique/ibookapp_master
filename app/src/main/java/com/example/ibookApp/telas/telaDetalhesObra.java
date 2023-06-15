package com.example.ibookApp.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ibookApp.APIs.comentariosPorLivroApi;
import com.example.ibookApp.Adapters.ComentarioAdapter;
import com.example.ibookApp.Adapters.ObraAdapter;
import com.example.ibookApp.Adapters.SearchAdapter;
import com.example.ibookApp.DTOs.ComentarioDTO;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.Utils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class telaDetalhesObra extends AppCompatActivity {

    private String obid, title, subtitle, synopsis, author, editora, dataPublicacao,dataFinalizacao, isbn, image,
            tipo, statusObra, categorias;
    private int paginas;
    private float avarageRating;
    private TextView tvNome, tvAutor, tvSinopse, tvRate;
    private RatingBar ratingBar;
    private Button btnHome, btnLogout, btnFavorito;
    private ImageView imgObra;
    private ComentarioAdapter comentarioAdapter;
    private RecyclerView rviComents;
    ArrayList<ComentarioDTO> comentariosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teladetalhesobra);
        Intent intent = getIntent();
        obid = intent.getStringExtra("obid");
        title = intent.getStringExtra("title");
        subtitle = intent.getStringExtra("subtitle");
        synopsis = intent.getStringExtra("synopsis");
        author = intent.getStringExtra("author");
        editora = intent.getStringExtra("editora");
        dataPublicacao = intent.getStringExtra("dataPublicacao");
        dataFinalizacao = intent.getStringExtra("dataFinalizacao");
        isbn = intent.getStringExtra("isbn");
        paginas = intent.getIntExtra("paginas", 0);
        image = intent.getStringExtra("image");
        tipo = intent.getStringExtra("tipo");
        avarageRating = intent.getFloatExtra("avarageRating", 0.0f);
        statusObra = intent.getStringExtra("statusObra");
        categorias = intent.getStringExtra("categorias");
        btnHome = findViewById(R.id.btnBackHome);
        btnLogout = findViewById(R.id.btnLogoutDetalhesObra);
        tvNome = findViewById(R.id.txtTitleDetalheObra);
        tvAutor = findViewById(R.id.txtAutorDetalheObra);
        ratingBar = findViewById(R.id.rbCadastroObraAvaliacaoDetalheObra);
        tvRate = findViewById(R.id.txtAvarageRatingDetalheObra);
        btnFavorito = findViewById(R.id.favoritebookDetalheObra);
        tvSinopse = findViewById(R.id.txtSinopseDetalheObra);
        imgObra = findViewById(R.id.imgDetalheObra);
        rviComents = findViewById(R.id.rviComents);

        loadData();
        comentariosPorLivroApi.getcomentariosPorLivro(obid, new comentariosPorLivroApi.comentariosPorLivroListener() {
            @Override
            public void oncomentariosPorLivroReceived(List<ComentarioDTO> comentariosPorLivro) {
                comentariosList.addAll(comentariosPorLivro);
                comentarioAdapter = new ComentarioAdapter(comentariosList);
                rviComents.setAdapter(comentarioAdapter);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                enviarNota(rating);
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
            public void onClick(View view) {
                logout();
            }
        });

        btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnFavorito.isSelected()) {
                    btnFavorito.setSelected(false);
                    btnFavorito.setBackgroundResource(R.drawable.ic_unsave_foreground);
                } else {
                    btnFavorito.setSelected(true);
                    btnFavorito.setBackgroundResource(R.drawable.ic_save_foreground);
                }
            }
        });
    }

    public void enviarNota(float rating){
        if (rating * 2 > 9.9){
            int rate = 10;
            tvRate.setText(String.valueOf(rate));
        }
        else{
            tvRate.setText(String.valueOf(rating * 2));
            ratingBar.setRating(rating);
        }
    }

    public void loadData(){
        if (image != null){
            Glide.with(this)
                    .load(image)
                    .into(imgObra);
        }
        else{
            Glide.with(this)
                    .load(R.drawable.ic_book_foreground)
                    .into(imgObra);
        }

        if(title != null){
            tvNome.setText(title);
        }

        if(author != null){
            tvAutor.setText(author);
        }
        tvRate.setText(String.valueOf(avarageRating * 2));
        ratingBar.setRating(avarageRating);

        if(synopsis != null){
            tvSinopse.setText(synopsis);
        }
    }

    public void logout(){
        Utils.logout();
        Intent acessar = new Intent(getApplicationContext(), telalogin.class);
        startActivity(acessar);
    }
}