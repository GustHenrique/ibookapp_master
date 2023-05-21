package com.example.ibookApp.APIs;
import android.os.AsyncTask;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.functions.Utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KitsuApiManager {
    private static final String KITSU_API_ENDPOINT = "https://kitsu.io/api/edge/manga";
    public interface MangaListListener {
        void onMangaListReceived(List<obrasDTO> mangaList);
    }

    public static void getMangaList(MangaListListener listener) {
        new MangaListAsyncTask(listener).execute();
    }

    private static class MangaListAsyncTask extends AsyncTask<Void, Void, List<obrasDTO>> {
        private MangaListListener listener;

        public MangaListAsyncTask(MangaListListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<obrasDTO> doInBackground(Void... voids) {
            List<obrasDTO> mangaList = new ArrayList<obrasDTO>();
            obrasDTO mangaApiKitsuDTO = new obrasDTO(null, null, null, null, null, null, null, null, null, null, null, null, null, null,null,null,null);
            OkHttpClient client = new OkHttpClient();
            int offset = 0; // Inicializa o deslocamento da página como 0
            int limit = 10; // Define o número de itens por página

            boolean hasNextPage = true;
            while (hasNextPage) {
                String url = KITSU_API_ENDPOINT + "?page[offset]=" + offset + "&page[limit]=" + limit;

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    ResponseBody responseBody = response.body();

                    if (responseBody != null) {
                        String jsonString = responseBody.string();
                        JSONObject jsonObject = new JSONObject(jsonString);
                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject mangaObject = dataArray.getJSONObject(i).getJSONObject("attributes");
                            StringBuilder stKeys = new StringBuilder();
                            Iterator<String> keys = mangaObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                stKeys.append(key);
                                if (keys.hasNext()) {
                                    stKeys.append(", ");
                                }
                            }
                            String chaves = stKeys.toString();

                            if (chaves.contains("createdAt")) {
                                mangaApiKitsuDTO.setDataPublicacao(mangaObject.getString("createdAt"));
                            }

                            if (chaves.contains("slug")) {
                                mangaApiKitsuDTO.setTitle(mangaObject.getString("slug"));
                            }

                            if (chaves.contains("synopsis")) {
                                mangaApiKitsuDTO.setSynopsis(mangaObject.getString("synopsis"));
                            }

                            if (chaves.contains("averageRating")) {
                                mangaApiKitsuDTO.setAvarageRating(mangaObject.getString("averageRating"));
                            }

                            if (chaves.contains("endDate")) {
                                mangaApiKitsuDTO.setDataFinalizacao(mangaObject.getString("endDate"));
                            }

                            if (chaves.contains("status")) {
                                mangaApiKitsuDTO.setStatus(mangaObject.getString("status"));
                            }

                            if (chaves.contains("posterImage")) {
                                JSONObject posterImage = mangaObject.getJSONObject("posterImage");
                                if (posterImage != null && posterImage.has("original")) {
                                    String originalImage = posterImage.getString("original");
                                    if (!originalImage.isEmpty()) {
                                        mangaApiKitsuDTO.setImage(originalImage);
                                    }
                                }
                            }

                            mangaApiKitsuDTO.setType("MANGA");
                            mangaList.add(mangaApiKitsuDTO);
                        }

                        JSONObject linksObject = jsonObject.getJSONObject("links");
                        String nextPageUrl = linksObject.optString("next");
                        hasNextPage = !nextPageUrl.isEmpty();
                        if (hasNextPage) {
                            // Incrementa o deslocamento da página para a próxima solicitação
                            offset += limit;
                        }
                    } else {
                        hasNextPage = false;
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    hasNextPage = false;
                }
            }
            //Utils.inserirLista(mangaList.subList(0, 50));
            return mangaList;
        }

        @Override
        protected void onPostExecute(List<obrasDTO> mangaList) {
            if (listener != null) {
                listener.onMangaListReceived(mangaList);
            }
        }
    }

}

