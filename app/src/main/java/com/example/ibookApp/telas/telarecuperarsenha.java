package com.example.ibookApp.telas;

import static com.example.ibookApp.functions.Utils.bytesToString;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ibookApp.APIs.AtualizarUsuarioApi;
import com.example.ibookApp.APIs.EmailExistenteApiClient;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.Utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class telarecuperarsenha extends AppCompatActivity {
    TextView tvEmailRecuperarEmail, tvLembrarSenha;
    EditText edtEmailRecuperarEmail;
    Button btnRecuperarEmail;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telarecuperarsenha);

        tvEmailRecuperarEmail = (TextView) findViewById(R.id.txtEmailRecuperarEmail);
        tvLembrarSenha = (TextView) findViewById(R.id.txtLembrarSenha);
        btnRecuperarEmail = (Button) findViewById(R.id.btnRecuperarSenha);
        edtEmailRecuperarEmail = findViewById(R.id.txtEmailRecuperarEmail);

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
                enviarNovaSenha();
            }
        });
    }

    public void enviarNovaSenha(){
        email = edtEmailRecuperarEmail.getText().toString();
        EmailExistenteApiClient.EmailExistenteAsyncTask task = new EmailExistenteApiClient.EmailExistenteAsyncTask(email, new EmailExistenteApiClient.EmailExistenteListener() {
            @Override
            public void onEmailExistenteReceived(UsuarioDTO usuario) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
                if (usuario != null) {
                    UsuarioDTO attUsuario = new UsuarioDTO(usuario.getEmail(), usuario.getSenha(), usuario.getNome(), usuario.getId(), usuario.getImagem(),usuario.getAdministrador());
                    SecretKey secret = Utils.generateKey();
                    String senha = gerarSenha();
                    byte[] encryptSenha = Utils.encryptMsg(senha, secret);
                    String encryptSenhaString = bytesToString(encryptSenha);
                    attUsuario.setSenha(encryptSenhaString);
                    AtualizarUsuarioApi.AtualizarUsuarioObrasAsyncTask task1 = new AtualizarUsuarioApi.AtualizarUsuarioObrasAsyncTask(attUsuario, new AtualizarUsuarioApi.AtualizarUsuarioObrasAListener() {
                        @Override
                        public void onInsertObrasReceived() throws MessagingException {
                            Intent Acessar = new Intent(getApplicationContext(), telalogin.class);
                            startActivity(Acessar);
                            enviarEmail(attUsuario.getEmail(), senha, attUsuario.getNome());
                        }
                    });
                    task1.execute();

                    Toast.makeText(telarecuperarsenha.this, "Senha recuperada com Sucesso!", Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(telarecuperarsenha.this, "Email não cadastrado!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.execute();
    }

    public static String gerarSenha() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder senha = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(caracteres.length());
            senha.append(caracteres.charAt(index));
        }

        return senha.toString();
    }

    public void enviarEmail(String stringReceiverEmail,String newPass, String name) throws MessagingException {
        try {
            String stringSenderEmail = "suporteibookoficial@gmail.com";
            String stringPasswordSenderEmail = "pid4ibook";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("Recuperação de senha iBook");
            mimeMessage.setText("Querido" + name + ",\n" +
                    "\n" +
                    "Você solicitou uma nova senha pelo aplicativo.\n" +
                    "Sua nova senha é: "+ newPass+ "\n" +
                    "\n" +
                    "Atenciosamente,\n" +
                    "Time iBook");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



}