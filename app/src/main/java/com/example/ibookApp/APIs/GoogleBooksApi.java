package com.example.ibookApp.APIs;

import android.os.AsyncTask;

import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.functions.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;

public class GoogleBooksApi {
    private static final String GOOGLE_BOOKS_API_ENDPOINT = "https://www.googleapis.com/books/v1/volumes";
    private static final int RESULTS_PER_PAGE = 40; // Número de resultados por página

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
            int startIndex = 0; // Inicia o índice da primeira página como 0
            List<obrasDTO> obrasList = new ArrayList<obrasDTO>();

            boolean hasNextPage = true;
            while (hasNextPage) {
                String url = GOOGLE_BOOKS_API_ENDPOINT + "?q=" + query + "&startIndex=" + startIndex + "&maxResults=" + RESULTS_PER_PAGE;

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String jsonString = responseBody.string();
                        JSONObject jsonObject = new JSONObject(jsonString);
                        JSONArray itemsArray = jsonObject.getJSONArray("items");

                        for (int i = 0; i < itemsArray.length(); i++) {
                            StringBuilder stringBuilder = new StringBuilder();
                            StringBuilder stKeys = new StringBuilder();
                            obrasDTO obrasDTO = new obrasDTO(null, null, null, null, null, null, null, null, null, null, null, null, null, null,null,null,null);
                            JSONObject itemObject = itemsArray.getJSONObject(i);
                            JSONObject volumeInfoObject = itemObject.getJSONObject("volumeInfo");
                            Iterator<String> keys = volumeInfoObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                stKeys.append(key);
                                if (keys.hasNext()) {
                                    stKeys.append(", ");
                                }
                            }
                            String chaves = stKeys.toString();

                            if (chaves.contains("title")){
                                obrasDTO.setTitle(volumeInfoObject.getString("title"));
                            }
                            if(chaves.contains("authors")){
                                JSONArray authorsArray = volumeInfoObject.optJSONArray("authors");
                                if (authorsArray != null) {
                                    stringBuilder = new StringBuilder();
                                    for (int j = 0; j < authorsArray.length(); j++) {
                                        stringBuilder.append(authorsArray.getString(j) + ", ");
                                    }
                                }
                                if (authorsArray.length() > 1){
                                    int lastIndex = stringBuilder.lastIndexOf(",");
                                    if (lastIndex >= 0) {
                                        stringBuilder.delete(lastIndex, lastIndex + 1);
                                    }
                                    obrasDTO.setAuthor(stringBuilder.toString());
                                }
                            }

                            if (chaves.contains("subtitle")){
                                obrasDTO.setSubtitle(volumeInfoObject.getString("subtitle"));
                            }

                            if (chaves.contains("publishedDate")){
                                obrasDTO.setDataPublicacao(volumeInfoObject.getString("publishedDate"));
                                obrasDTO.setDataFinalizacao(volumeInfoObject.getString("publishedDate"));
                            }

                            if (chaves.contains("description")){
                                obrasDTO.setDescricao(volumeInfoObject.getString("description"));
                            }

                            if (chaves.contains("publisher")){
                                obrasDTO.setEditora(volumeInfoObject.getString("publisher"));
                            }
                            if (chaves.contains("pageCount")){
                                obrasDTO.setPaginas(volumeInfoObject.getString("pageCount"));
                            }
                            if(chaves.contains("categories")){
                                JSONArray categoriesArray = volumeInfoObject.optJSONArray("categories");
                                if (categoriesArray != null) {
                                    stringBuilder = new StringBuilder();
                                    for (int j = 0; j < categoriesArray.length(); j++) {
                                        stringBuilder.append(categoriesArray.getString(j) + ", ");
                                    }
                                }
                                if (categoriesArray.length() > 1){
                                    int lastIndex = stringBuilder.lastIndexOf(",");
                                    if (lastIndex >= 0) {
                                        stringBuilder.delete(lastIndex, lastIndex + 1);
                                    }
                                    obrasDTO.setAuthor(stringBuilder.toString());
                                }
                            }
                            if (chaves.contains("imageLinks")){
                                obrasDTO.setImage(volumeInfoObject.getJSONObject("imageLinks").getString("thumbnail"));
                            }
                            if (chaves.contains("language")){
                                obrasDTO.setTraducao(volumeInfoObject.getString("language"));
                            }
                            obrasDTO.setType("BOOK");
                            obrasList.add(obrasDTO);
                        }

                        int totalItems = jsonObject.getInt("totalItems");
                        if (startIndex + RESULTS_PER_PAGE >= totalItems) {
                            hasNextPage = false; // Verifica se não há mais páginas a serem buscadas
                        } else {
                            startIndex += RESULTS_PER_PAGE; // Incrementa o índice para a próxima página
                        }
                    } else {
                        hasNextPage = false;
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    hasNextPage = false;
                }
            }
            //Utils.inserirLista(obrasList.subList(0, 50));
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