package com.example.ibookApp.telas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

public class telaDetalhesObra extends AppCompatActivity {

    private String obid, title, subtitle, synopsis, author, editora, dataPublicacao,dataFinalizacao, isbn, image,
            tipo, statusObra, categorias;
    private int paginas;
    private float avarageRating;
    private TextView tvTeste;
    private Button btnHome, btnLogout;
    private ImageView imgObra;

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
        imgObra = findViewById(R.id.imgDetalheObra);

        Glide.with(this)
                .load(image)
                .into(imgObra);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
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
    }

    public void logout(){
        Utils.logout();
        Intent acessar = new Intent(getApplicationContext(), telalogin.class);
        startActivity(acessar);
    }
}