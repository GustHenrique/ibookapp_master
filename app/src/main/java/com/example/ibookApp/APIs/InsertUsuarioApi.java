package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class InsertUsuarioApi {
    private static final String BASE_URL = BASE_URL_API;

    public interface InsertUsuarioListener {
        void onInsertBookReceived(boolean success);
    }

    public static class InsertUsuarioAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private String email, nome, imagem, senha;
        private InsertUsuarioApi.InsertUsuarioListener listener;

        public InsertUsuarioAsyncTask(String email, String senha,String nome, String imagem, InsertUsuarioApi.InsertUsuarioListener listener) {
            this.email = email;
            this.senha = senha;
            this.nome = nome;
            this.imagem = imagem;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Usuario/CadastrarUsuario").newBuilder();
            try {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("nome", nome);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    jsonBody.put("email", email);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    jsonBody.put("senha", senha);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    jsonBody.put("imagem", imagem);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    jsonBody.put("administrador", false);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                RequestBody requestBody = RequestBody.create(
                        okhttp3.MediaType.parse("application/json"),
                        jsonBody.toString()
                );


            /*RequestBody requestBody = new FormBody.Builder()
                    .add("nome", nome)
                    .add("imagem", imagem)
                    .add("email", email)
                    .add("senha", senha)
                    .add("administrador", String.valueOf(false))
                    .build();*/

                Request request = new Request.Builder()
                        .url(urlBuilder.build())
                        .post(requestBody)
                        .build();
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}
