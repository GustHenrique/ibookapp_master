package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EnviarEmailApi {
    private static final String BASE_URL = BASE_URL_API;

    public interface EnviarEmailObrasAListener {
        void onInsertObrasReceived();
    }

    public static class EnviarEmailObrasAsyncTask extends AsyncTask<Void, Void, Void> {
        private String name, email, newPass;
        private EnviarEmailApi.EnviarEmailObrasAListener listener;

        public EnviarEmailObrasAsyncTask(String name, String email, String newPass, EnviarEmailApi.EnviarEmailObrasAListener listener) {
            this.name = name;
            this.email = email;
            this.newPass = newPass;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Resposta/EnviarEmail").newBuilder();
            urlBuilder.addQueryParameter("name", name);
            urlBuilder.addQueryParameter("email", email);
            urlBuilder.addQueryParameter("newPass", newPass);

            try {
                Request request = new Request.Builder()
                        .url(urlBuilder.build())
                        .post(new FormBody.Builder().build())
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
