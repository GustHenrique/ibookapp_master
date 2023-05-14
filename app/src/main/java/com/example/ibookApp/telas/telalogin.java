package com.example.ibookApp.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ibookApp.AdapterContact;
import com.example.ibookApp.DAOs.UsuarioDAO;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.UserSingleton;

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
    private AdapterContact adapterContact;
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
                try {
                    fazerLogin();
                } catch (NoSuchPaddingException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (IllegalBlockSizeException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (BadPaddingException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void fazerLogin() throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        email = tvUsuEmail.getText().toString();
        senha =  tvUsuSenha.getText().toString();
        if (!email.isEmpty() && !senha.isEmpty()){
            UsuarioDAO UsuarioDAO = new UsuarioDAO(this);
            UsuarioDTO usuario = UsuarioDAO.autenticarUsuario(email, senha);
            if (usuario.getUsuid() != null){
                tvUsuEmail.setText("");
                tvUsuSenha.setText("");
                Toast.makeText(this,"Autenticado com sucesso!", Toast.LENGTH_LONG).show();
                UserSingleton.getInstance().setUser(usuario);
                Intent Acessar = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(Acessar);
            }
            else{
                Toast.makeText(this,"Email ou senha inválidos!", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this,"Todos os campos são obrigatórios!", Toast.LENGTH_LONG).show();
        }
    }
}