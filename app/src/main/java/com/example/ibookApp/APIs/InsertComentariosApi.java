package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class InsertComentariosApi {
    private static final String BASE_URL = BASE_URL_API;

    public interface InsertComentariosListener {
        void onInsertComentariosReceived(boolean success);
    }

    public static class InsertComentariosAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private String usuid, obid, comentario;
        private InsertComentariosApi.InsertComentariosListener listener;

        public InsertComentariosAsyncTask(String usuid, String obid, String comentario, InsertComentariosApi.InsertComentariosListener listener) {
            this.usuid = usuid;
            this.obid = obid;
            this.comentario = comentario;
            this.listener = listener;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Comentario/AdicionarComentario").newBuilder();
            try {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dataAtual = dateFormat.format(calendar.getTime());

                JSONObject jsonBody = new JSONObject();

                jsonBody.put("usuid", usuid);
                jsonBody.put("obid", obid);
                jsonBody.put("comentario", comentario);
                jsonBody.put("dataComentario", dataAtual);

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
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}
