package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import com.example.ibookApp.DTOs.UsuarioDTO;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EmailExistenteApiClient {
    private static final String BASE_URL = BASE_URL_API;

    public interface EmailExistenteListener {
        void onEmailExistenteReceived(UsuarioDTO usuario) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException;
    }

    public static class EmailExistenteAsyncTask extends AsyncTask<Void, Void, UsuarioDTO> {
        private String email;
        private EmailExistenteApiClient.EmailExistenteListener listener;

        public EmailExistenteAsyncTask(String email, EmailExistenteApiClient.EmailExistenteListener listener) {
            this.email = email;
            this.listener = listener;
        }

        @Override
        protected UsuarioDTO doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            UsuarioDTO usuario = null;
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Usuario/UsuarioPorEmail").newBuilder();
            urlBuilder.addQueryParameter("email", email);

            Request request = new Request.Builder()
                    .url(urlBuilder.build())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    String jsonString = responseBody.string();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject != null){
                        usuario = new UsuarioDTO(null,null,null,null,null,null);
                        usuario.setId(jsonObject.getString("id"));
                        usuario.setNome(jsonObject.getString("nome"));
                        usuario.setEmail(jsonObject.getString("email"));
                        usuario.setSenha(jsonObject.getString("senha"));
                        usuario.setImagem(jsonObject.getString("imagem"));
                        usuario.setAdministrador(jsonObject.getBoolean("administrador"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return usuario;
        }

        @Override
        protected void onPostExecute(UsuarioDTO usuario) {
            if (listener != null) {
                try {
                    listener.onEmailExistenteReceived(usuario);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchPaddingException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (IllegalBlockSizeException e) {
                    throw new RuntimeException(e);
                } catch (BadPaddingException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
