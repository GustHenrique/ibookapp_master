package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RatingObrasApi {
    private static final String BASE_URL = BASE_URL_API;

    public interface RatingObrasAListener {
        void onInsertBookReceived(boolean success);
    }

    public static class RatingObrasAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private String obid,avarageRating;
        private RatingObrasApi.RatingObrasAListener listener;

        public RatingObrasAsyncTask(String obid, String avarageRating, RatingObrasApi.RatingObrasAListener listener) {
            this.obid = obid;
            this.avarageRating = avarageRating;
            this.listener = listener;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Obras/AvaliarObra").newBuilder();
            urlBuilder.addQueryParameter("obid", obid);
            urlBuilder.addQueryParameter("avarageRating", avarageRating);
            try {
                Request request = new Request.Builder()
                        .url(urlBuilder.build())
                        .post(new FormBody.Builder().build())
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
