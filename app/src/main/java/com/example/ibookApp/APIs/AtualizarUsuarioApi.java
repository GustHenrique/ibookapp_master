package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import com.example.ibookApp.DTOs.UsuarioDTO;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AtualizarUsuarioApi {
    private static final String BASE_URL = BASE_URL_API;

    public interface AtualizarUsuarioObrasAListener {
        void onInsertObrasReceived();
    }

    public static class AtualizarUsuarioObrasAsyncTask extends AsyncTask<Void, Void, Void> {
        private UsuarioDTO usuarioDTO;
        private AtualizarUsuarioApi.AtualizarUsuarioObrasAListener listener;

        public AtualizarUsuarioObrasAsyncTask(UsuarioDTO usuario, AtualizarUsuarioApi.AtualizarUsuarioObrasAListener listener) {
            this.usuarioDTO = usuario;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Usuario/AtualizarUsuario").newBuilder();

            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("id", usuarioDTO.getId());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                jsonBody.put("nome", usuarioDTO.getNome());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                jsonBody.put("email", usuarioDTO.getEmail());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                jsonBody.put("senha", usuarioDTO.getSenha());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                jsonBody.put("imagem", usuarioDTO.getImagem());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                jsonBody.put("administrador", usuarioDTO.getAdministrador());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            RequestBody requestBody = RequestBody.create(
                    okhttp3.MediaType.parse("application/json"),
                    jsonBody.toString()
            );

            try {
                Request request = new Request.Builder()
                        .url(urlBuilder.build())
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (listener != null) {
                listener.onInsertObrasReceived();
            }
        }
    }
}
