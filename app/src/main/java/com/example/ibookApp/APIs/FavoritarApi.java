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

public class FavoritarApi {
    private static final String BASE_URL = BASE_URL_API;

    public interface FavoritarListener {
        void onInsertFavReceived(boolean success);
    }

    public static class FavoritarAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private String usuid, obid;
        private FavoritarApi.FavoritarListener listener;

        public FavoritarAsyncTask(String usuid, String obid, FavoritarApi.FavoritarListener listener) {
            this.usuid = usuid;
            this.obid = obid;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Favoritos/AdicionarFavorito").newBuilder();
            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("usuid", usuid);
                jsonBody.put("obid", obid);

                RequestBody requestBody = RequestBody.create(
                        okhttp3.MediaType.parse("application/json"),
                        jsonBody.toString()
                );

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
                listener.onInsertFavReceived(success);
            }
        }
    }
}