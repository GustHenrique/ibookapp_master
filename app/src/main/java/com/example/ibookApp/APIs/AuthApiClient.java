package com.example.ibookApp.APIs;

import android.os.AsyncTask;

import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.DTOs.obrasDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.FormBody;

import java.util.ArrayList;
import java.util.List;

public class AuthApiClient {
    private static final String BASE_URL = "http://15.228.241.26/api/";

    public interface AuthApiListener {
        void onAuthApiReceived(UsuarioDTO usuario);
    }

    public static class AuthApiAsyncTask extends AsyncTask<Void, Void, UsuarioDTO> {
        private String email;
        private String senha;
        private AuthApiClient.AuthApiListener listener;

        public AuthApiAsyncTask(String email, String senha, AuthApiClient.AuthApiListener listener) {
            this.email = email;
            this.senha = senha;
            this.listener = listener;
        }

        @Override
        protected UsuarioDTO doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            UsuarioDTO usuario = null;

            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Usuario/Autenticar").newBuilder();
            urlBuilder.addQueryParameter("email", email);
            urlBuilder.addQueryParameter("senha", senha);

            RequestBody requestBody = new FormBody.Builder()
                    .add("email", email)
                    .add("senha", senha)
                    .build();

            Request request = new Request.Builder()
                    .url(urlBuilder.build())
                    .post(requestBody)
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
                listener.onAuthApiReceived(usuario);
            }
        }
    }
}
