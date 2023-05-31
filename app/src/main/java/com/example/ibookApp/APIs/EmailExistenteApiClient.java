package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;
import static com.example.ibookApp.functions.Utils.bytesToString;

import android.os.AsyncTask;

import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.functions.Utils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EmailExistenteApiClient {
    private static final String BASE_URL = BASE_URL_API;

    public interface EmailExistenteListener {
        void onEmailExistenteReceived(UsuarioDTO usuario);
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
                listener.onEmailExistenteReceived(usuario);
            }
        }
    }
}
