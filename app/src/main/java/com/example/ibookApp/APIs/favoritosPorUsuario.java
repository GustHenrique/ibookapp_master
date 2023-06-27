package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import com.example.ibookApp.DTOs.favoritosDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class favoritosPorUsuario {
    private static final String API_ENDPOINT = BASE_URL_API + "Favoritos/TodosFavoritosUsu";

    public interface favoritosPorUsuarioListener {
        void onfavoritosPorUsuarioReceived(List<favoritosDTO> favoritosPorUsuario);
    }

    public static void getfavoritosPorUsuario(String usuid, favoritosPorUsuarioListener listener) {
        new favoritosPorUsuarioAsyncTask(usuid, listener).execute();
    }

    private static class favoritosPorUsuarioAsyncTask extends AsyncTask<Void, Void, List<favoritosDTO>> {
        private String usuid;
        private favoritosPorUsuarioListener listener;

        public favoritosPorUsuarioAsyncTask(String usuid, favoritosPorUsuarioListener listener) {
            this.usuid = usuid;
            this.listener = listener;
        }

        @Override
        protected List<favoritosDTO> doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            List<favoritosDTO> favoritosList = new ArrayList<>();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(API_ENDPOINT).newBuilder();
            urlBuilder.addQueryParameter("usuid", usuid);
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
                        String id = jsonObject.getString("id");
                        String jsonUsuid  = jsonObject.getString("usuid");
                        String obid = jsonObject.getString("obid");
                        favoritosDTO favoritos = new favoritosDTO(id,jsonUsuid,obid);
                        favoritosList.add(favoritos);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return favoritosList;
        }

        @Override
        protected void onPostExecute(List<favoritosDTO> favoritosPorUsuario) {
            if (listener != null) {
                listener.onfavoritosPorUsuarioReceived(favoritosPorUsuario);
            }
        }
    }
}
