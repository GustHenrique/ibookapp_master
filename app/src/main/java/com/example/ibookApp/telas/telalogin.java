package com.example.ibookApp.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ibookApp.APIs.AuthApiClient;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.UserSingleton;

import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class telalogin extends AppCompatActivity {
    TextView tvNaoTemCadastro, tvUsuEmail, tvUsuSenha, tvRecuperarSenha;
    String email, senha;
    Button btnAcessar;
    private RecyclerView ibookRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telalogin);
        tvNaoTemCadastro = (TextView) findViewById(R.id.txtCriarConta);
        tvRecuperarSenha = (TextView) findViewById(R.id.txtRecuperarSenha);
        tvUsuEmail = (TextView) findViewById(R.id.txtEmailLogin);
        tvUsuSenha = (TextView) findViewById(R.id.txtSenhaLogin);
        btnAcessar = (Button) findViewById(R.id.btnAcessar);

        tvNaoTemCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent naoTemCadastro = new Intent(getApplicationContext(), telacadastro.class);
                startActivity(naoTemCadastro);
            }
        });

        tvRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recuperarSenha = new Intent(getApplicationContext(), telarecuperarsenha.class);
                startActivity(recuperarSenha);
            }
        });

        btnAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = tvUsuEmail.getText().toString();
                senha = tvUsuSenha.getText().toString();
                fazerAutenticacao(email, senha);
            }
        });
    }
    public void fazerAutenticacao(String email, String senha) {
        if (!email.isEmpty() && !senha.isEmpty()){
            AuthApiClient.AuthApiAsyncTask task = new AuthApiClient.AuthApiAsyncTask(email, senha, new AuthApiClient.AuthApiListener() {
                @Override
                public void onAuthApiReceived(UsuarioDTO usuario) {
                    // Lógica para lidar com o resultado da API
                    if (usuario != null) {
                        tvUsuEmail.setText("");
                        tvUsuSenha.setText("");
                        Toast.makeText(telalogin.this,"Autenticado com sucesso!", Toast.LENGTH_LONG).show();
                        UserSingleton.getInstance().setUser(usuario);
                        Intent Acessar = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(Acessar);
                    } else {
                        Toast.makeText(telalogin.this, "Email ou senha inválido!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            task.execute();
        }
        else{
            Toast.makeText(telalogin.this, "Campos Obrigátorios em Branco!!", Toast.LENGTH_SHORT).show();
        }
    }

}