package com.example.ibookApp.APIs;

import android.os.AsyncTask;

import com.example.ibookApp.DTOs.obrasDTO;
import static com.example.ibookApp.functions.Constants.BASE_URL_API;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ibookApi {
    private static final String API_ENDPOINT = BASE_URL_API + "Obras/TodasObras";

    public interface BookListListener {
        void onBookListReceived(List<obrasDTO> bookList);
    }

    public static void getBookList(BookListListener listener) {
        new BookListAsyncTask(listener).execute();
    }

    private static class BookListAsyncTask extends AsyncTask<Void, Void, List<obrasDTO>> {
        private String query;
        private BookListListener listener;

        public BookListAsyncTask(BookListListener listener) {
            this.query = query;
            this.listener = listener;
        }

        @Override
        protected List<obrasDTO> doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            List<obrasDTO> obrasList = new ArrayList<obrasDTO>();
            String url = API_ENDPOINT;

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    String jsonString = responseBody.string();
                    JSONArray jsonArray = new JSONArray(jsonString);
                    obrasDTO obrasDTO = new obrasDTO(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
                    if (jsonArray != null){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            obrasDTO = new obrasDTO(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            obrasDTO.setId(jsonObject.getString("id"));
                            obrasDTO.setTitle(jsonObject.getString("title"));
                            obrasDTO.setSubtitle(jsonObject.getString("subtitle"));
                            obrasDTO.setSynopsis(jsonObject.getString("synopsis"));
                            obrasDTO.setAuthor(jsonObject.getString("author"));
                            obrasDTO.setEditora(jsonObject.getString("editora"));
                            obrasDTO.setDataPublicacao(jsonObject.getString("dataPublicacao"));
                            obrasDTO.setDataFinalizacao(jsonObject.getString("dataFinalizacao"));
                            obrasDTO.setIsbn(jsonObject.getString("isbn"));
                            obrasDTO.setPaginas(jsonObject.getString("paginas"));
                            obrasDTO.setImage(jsonObject.getString("image"));
                            obrasDTO.setTraducao(jsonObject.getString("traducao"));
                            obrasDTO.setType(jsonObject.getString("tipo"));
                            obrasDTO.setAvarageRating(jsonObject.getString("avarageRating"));
                            obrasDTO.setStatus(jsonObject.getString("statusObra"));
                            obrasDTO.setCategorias(jsonObject.getString("categorias"));
                            obrasList.add(obrasDTO);
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return obrasList;
        }

        @Override
        protected void onPostExecute(List<obrasDTO> bookList) {
            if (listener != null) {
                listener.onBookListReceived(bookList);
            }
        }
    }
}