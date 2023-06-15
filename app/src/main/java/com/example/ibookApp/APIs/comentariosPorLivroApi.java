package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import com.example.ibookApp.DTOs.ComentarioDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class comentariosPorLivroApi {
    private static final String BASE_URL = BASE_URL_API;
    private static final String API_ENDPOINT = BASE_URL + "Comentario/TodosComentariosObra";

    public interface comentariosPorLivroListener {
        void oncomentariosPorLivroReceived(List<ComentarioDTO> comentariosPorLivro);
    }

    public static void getcomentariosPorLivro(String livroId, comentariosPorLivroListener listener) {
        new comentariosPorLivroAsyncTask(livroId, listener).execute();
    }

    private static class comentariosPorLivroAsyncTask extends AsyncTask<Void, Void, List<ComentarioDTO>> {
        private String livroId;
        private comentariosPorLivroListener listener;

        public comentariosPorLivroAsyncTask(String livroId, comentariosPorLivroListener listener) {
            this.livroId = livroId;
            this.listener = listener;
        }

        @Override
        protected List<ComentarioDTO> doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            List<ComentarioDTO> comentariosList = new ArrayList<>();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(API_ENDPOINT + "/" + livroId).newBuilder();
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    String jsonString = responseBody.string();
                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        java.sql.Date dataComentario = null;
                        try {
                            String dataComentarioString = jsonObject.getString("dataComentario");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            java.util.Date utilDate = null;
                            utilDate = dateFormat.parse(dataComentarioString);
                            dataComentario = new java.sql.Date(utilDate.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String usuid = jsonObject.getString("usuid");
                        String comid = jsonObject.getString("comid");
                        String comentario = jsonObject.getString("comentario");
                        String obid = jsonObject.getString("obid");
                        ComentarioDTO comentarioDTO = new ComentarioDTO(comid,comentario,usuid,obid,dataComentario);
                        comentariosList.add(comentarioDTO);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return comentariosList;
        }

        @Override
        protected void onPostExecute(List<ComentarioDTO> comentariosPorLivro) {
            if (listener != null) {
                listener.oncomentariosPorLivroReceived(comentariosPorLivro);
            }
        }
    }
}
