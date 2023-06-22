package com.example.ibookApp.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ibookApp.APIs.AuthApiClient;
import com.example.ibookApp.APIs.InsertComentariosApi;
import com.example.ibookApp.APIs.comentariosPorLivroApi;
import com.example.ibookApp.Adapters.ComentarioAdapter;
import com.example.ibookApp.Adapters.ObraAdapter;
import com.example.ibookApp.Adapters.SearchAdapter;
import com.example.ibookApp.DTOs.ComentarioDTO;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.UserSingleton;
import com.example.ibookApp.functions.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class telaDetalhesObra extends AppCompatActivity {

    private String obid, title, subtitle, synopsis, author, editora, dataPublicacao,dataFinalizacao, isbn, image,
            tipo, statusObra, categorias, usuid, avarageRatingString;
    private int paginas;
    private float avarageRating;
    private TextView tvNome, tvAutor, tvSinopse, tvRate;
    private EditText txtComentar;
    private RatingBar ratingBar;
    private Button btnHome, btnLogout, btnFavorito;
    private ImageView imgObra;
    private ComentarioAdapter comentarioAdapter;
    private RecyclerView rviComents;
    private FloatingActionButton flaBtn;
    ArrayList<ComentarioDTO> comentariosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teladetalhesobra);
        Intent intent = getIntent();
        UsuarioDTO userLogado = UserSingleton.getInstance().getUser();
        usuid = userLogado.getId();
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
        avarageRatingString = intent.getStringExtra("avarageRating");
        statusObra = intent.getStringExtra("statusObra");
        categorias = intent.getStringExtra("categorias");
        flaBtn = findViewById(R.id.fabDetalheObra);
        txtComentar = findViewById(R.id.txtComentarDetalheObra);
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

        carregarComentarios();

        flaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comentar();
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

    public void comentar(){
        if (txtComentar != null){
            String comentario = txtComentar.getText().toString();
            if (!comentario.isEmpty()){
                InsertComentariosApi.InsertComentariosAsyncTask task = new InsertComentariosApi.InsertComentariosAsyncTask(usuid,obid,comentario, new InsertComentariosApi.InsertComentariosListener() {

                    @Override
                    public void onInsertComentariosReceived(boolean success) {

                    }
                });
                task.execute();
                txtComentar.setText("");
                carregarComentarios();
            }
        }
    }

    public void carregarComentarios(){
        comentariosPorLivroApi.getcomentariosPorLivro(obid, new comentariosPorLivroApi.comentariosPorLivroListener() {
            @Override
            public void oncomentariosPorLivroReceived(List<ComentarioDTO> comentariosPorLivro) {
                comentariosList.addAll(comentariosPorLivro);
                comentarioAdapter = new ComentarioAdapter(comentariosList);
                int itemCount = comentarioAdapter.getItemCount();
                int itemHeight = 150;
                int recyclerViewHeight = itemCount * itemHeight;

                ViewGroup.LayoutParams layoutParams = rviComents.getLayoutParams();
                layoutParams.height = recyclerViewHeight;
                rviComents.setLayoutParams(layoutParams);
                rviComents.setAdapter(comentarioAdapter);
            }
        });
    }

    public void enviarNota(float rating){

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

        if (avarageRatingString != null && avarageRatingString != ""){
            avarageRating = Float.parseFloat(avarageRatingString);
            if (avarageRating * 2 > 9.9){
                int rate = 10;
                tvRate.setText(String.valueOf(rate));
            }
            else{
                tvRate.setText(String.valueOf(avarageRating * 2));
            }
            ratingBar.setRating(avarageRating);
        }
        else{
            ratingBar.setRating(2.5f);
        }

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