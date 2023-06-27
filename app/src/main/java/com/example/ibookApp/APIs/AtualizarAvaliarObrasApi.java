package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AtualizarAvaliarObrasApi {
    private static final String BASE_URL = BASE_URL_API;

    public interface AtualizarAvaliarObrasAListener {
        void onInsertObrasReceived();
    }

    public static class AtualizarAvaliarObrasAsyncTask extends AsyncTask<Void, Void, Void> {
        private String usuid, obid, avarageRating;
        private AtualizarAvaliarObrasApi.AtualizarAvaliarObrasAListener listener;

        public AtualizarAvaliarObrasAsyncTask(String usuid, String obid, String avarageRating, AtualizarAvaliarObrasApi.AtualizarAvaliarObrasAListener listener) {
            this.obid = obid;
            this.usuid = usuid;
            this.avarageRating = avarageRating;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Avaliacao/AtualizarAvaObra").newBuilder();
            urlBuilder.addQueryParameter("usuid", usuid);
            urlBuilder.addQueryParameter("obid", obid);
            urlBuilder.addQueryParameter("avarageRating", avarageRating);

            try {
                Request request = new Request.Builder()
                        .url(urlBuilder.build())
                        .post(new FormBody.Builder().build())
                        .build();

                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                // Processar a resposta da API, se necessário

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
