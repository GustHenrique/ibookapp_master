package com.example.ibookApp.telas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ibookApp.R;

public class telarecuperarsenha extends AppCompatActivity {
    TextView tvEmailRecuperarEmail, tvLembrarSenha;
    Button btnRecuperarEmail;

    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telarecuperarsenha);

        tvEmailRecuperarEmail = (TextView) findViewById(R.id.txtEmailRecuperarEmail);
        tvLembrarSenha = (TextView) findViewById(R.id.txtLembrarSenha);
        btnRecuperarEmail = (Button) findViewById(R.id.btnRecuperarSenha);

        tvLembrarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lembrouConta = new Intent(getApplicationContext(), telalogin.class);
                startActivity(lembrouConta);
            }
        });

        btnRecuperarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}