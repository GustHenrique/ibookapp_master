package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import com.example.ibookApp.DTOs.UsuarioDTO;

import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UsuarioPorIdApiClient {
    private static final String BASE_URL = BASE_URL_API;

    public interface UsuarioPorIdApiListener {
        void onUsuarioPorIdApiReceived(UsuarioDTO usuario);
    }

    public static class UsuarioPorIdAsyncTask extends AsyncTask<Void, Void, UsuarioDTO> {
        private String id;
        private UsuarioPorIdApiClient.UsuarioPorIdApiListener listener;

        public UsuarioPorIdAsyncTask(String id, UsuarioPorIdApiListener listener) {
            this.id = id;
            this.listener = listener;
        }

        @Override
        protected UsuarioDTO doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            UsuarioDTO usuario = null;

            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Usuario/ConsultarPorId/" + id).newBuilder();

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
                listener.onUsuarioPorIdApiReceived(usuario);
            }
        }
    }
}
