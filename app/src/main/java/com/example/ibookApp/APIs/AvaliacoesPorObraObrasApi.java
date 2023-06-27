package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import com.example.ibookApp.DTOs.AvaliacaoDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AvaliacoesPorObraObrasApi {
    private static final String BASE_URL = BASE_URL_API;

    public interface AvaliarObrasAListener {
        void onInsertObrasReceived(List<AvaliacaoDTO> avaliacaoList);
    }

    public static class AvaliarObrasAsyncTask extends AsyncTask<Void, Void, List<AvaliacaoDTO>> {
        private String obid;
        private AvaliacoesPorObraObrasApi.AvaliarObrasAListener listener;

        public AvaliarObrasAsyncTask(String obid, AvaliacoesPorObraObrasApi.AvaliarObrasAListener listener) {
            this.obid = obid;
            this.listener = listener;
        }

        @Override
        protected List<AvaliacaoDTO> doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            List<AvaliacaoDTO> avaliacaoList = new ArrayList<>();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Avaliacao/TodosAvaliacoesObra").newBuilder();
            urlBuilder.addQueryParameter("obid", obid);
            try {
                Request request = new Request.Builder()
                        .url(urlBuilder.build())
                        .build();
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    String jsonString = responseBody.string();
                    JSONArray jsonArray = new JSONArray(jsonString);
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO(null,null,null,null);
                            avaliacaoDTO.setId(jsonObject.getString("id"));
                            avaliacaoDTO.setUsuid(jsonObject.getString("usuid"));
                            avaliacaoDTO.setObid(jsonObject.getString("obid"));
                            avaliacaoDTO.setAvaliacao(jsonObject.getString("avaliacao"));
                            avaliacaoList.add(avaliacaoDTO);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null; // Retorna null em caso de erro
            }
            return avaliacaoList;
        }

        @Override
        protected void onPostExecute(List<AvaliacaoDTO> avaliacaoList) {
            if (listener != null) {
                listener.onInsertObrasReceived(avaliacaoList);
            }
        }
    }
}

