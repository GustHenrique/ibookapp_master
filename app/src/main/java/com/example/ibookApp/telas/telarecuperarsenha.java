package com.example.ibookApp.telas;

import static com.example.ibookApp.functions.Utils.gerarSenha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ibookApp.DAOs.UsuarioDAO;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.R;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
                try {
                    recuperarSenha(view);
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
                } catch (InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void recuperarSenha(View view) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        UsuarioDAO UsuarioDAO = new UsuarioDAO(this);
        email = tvEmailRecuperarEmail.getText().toString();
        if (!email.isEmpty()){
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && !UsuarioDAO.existeEmailCadastrado(email)){
                UsuarioDTO usuario = UsuarioDAO.retornarUsuarioEmail(email);
                if (usuario.getUsuid() != null){
                    String novaSenha = gerarSenha();
                    UsuarioDAO.recuperarSenha(usuario, novaSenha);
                    /*Utils.enviarEmail(email, novaSenha);*/
                }
            }
            else{
                Toast.makeText(this,"E-mail inválido ou não cadastrado!", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this,"Todos os campos são obrigatórios!", Toast.LENGTH_LONG).show();
        }
    }


}