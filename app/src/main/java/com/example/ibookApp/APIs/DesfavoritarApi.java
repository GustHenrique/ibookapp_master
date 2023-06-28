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

public class DesfavoritarApi {
    private static final String BASE_URL = BASE_URL_API;

    public interface DesfavoritarListener {
        void onDeleteFavReceived(boolean success);
    }

    public static class DesavoritarAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private String usuid, obid;
        private DesfavoritarApi.DesfavoritarListener listener;

        public DesavoritarAsyncTask(String usuid, String obid, DesfavoritarApi.DesfavoritarListener listener) {
            this.usuid = usuid;
            this.obid = obid;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Favoritos/DeletarFavorito").newBuilder();

            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("usuid", usuid);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                jsonBody.put("obid", obid);
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

                if (response.isSuccessful()) {
                    // Processar a resposta de sucesso, se necessário
                    return true;
                } else {
                    // Processar a resposta de erro, se necessário
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            // Notificar o ouvinte sobre o resultado da operação
            if (listener != null) {
                listener.onDeleteFavReceived(success);
            }
        }
    }
}