package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import com.example.ibookApp.DTOs.AvaliacaoDTO;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AdicionarAvaliarObrasApi {
    private static final String BASE_URL = BASE_URL_API;

    public interface AdicionarAvaliarObrasAListener {
        void onInsertObrasReceived();
    }

    public static class AdicionarAvaliarObrasAsyncTask extends AsyncTask<Void, Void, Void> {
        private String usuid, obid, avarageRating;
        private AdicionarAvaliarObrasApi.AdicionarAvaliarObrasAListener listener;

        public AdicionarAvaliarObrasAsyncTask(String usuid, String obid, String avarageRating, AdicionarAvaliarObrasApi.AdicionarAvaliarObrasAListener listener) {
            this.obid = obid;
            this.usuid = usuid;
            this.avarageRating = avarageRating;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Avaliacao/AdicionarAvaliacao").newBuilder();

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
            try {
                jsonBody.put("avarageRating", avarageRating);
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
