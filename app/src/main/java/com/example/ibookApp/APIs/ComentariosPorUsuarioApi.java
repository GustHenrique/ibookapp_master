package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.DTOs.ComentarioDTO;
import com.example.ibookApp.functions.UserSingleton;

import org.json.JSONArray;
import org.json.JSONObject;
;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ComentariosPorUsuarioApi {
    private static final String BASE_URL = BASE_URL_API;

    public interface BookListListener {
        void onBookListReceived(List<ComentarioDTO> bookList);
    }

    public static void getBookList(BookListListener listener) {
        new BookListAsyncTask(listener).execute();
    }

    private static class BookListAsyncTask extends AsyncTask<Void, Void, List<ComentarioDTO>> {
        private String query;
        private BookListListener listener;

        public BookListAsyncTask(BookListListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<ComentarioDTO> doInBackground(Void... voids) {
            UsuarioDTO userLogado = UserSingleton.getInstance().getUser();
            OkHttpClient client = new OkHttpClient();
            List<ComentarioDTO> comentarioList = new ArrayList<ComentarioDTO>();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Comentario/TodosComentariosUsu/" + userLogado.getId()).newBuilder();

            Request request = new Request.Builder()
                    .url(urlBuilder.build())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    String jsonString = responseBody.string();
                    JSONArray jsonArray = new JSONArray(jsonString);
                    ComentarioDTO comentarioDTO = new ComentarioDTO(null,null,null,null,null);
                    if (jsonArray != null){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            comentarioDTO = new ComentarioDTO(null,null,null,null,null);
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String dataComentario = jsonObject.getString("dataComentario");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date date = dateFormat.parse(dataComentario);

                            long timeInMillis = date.getTime();

                            java.sql.Date sqlDate = new java.sql.Date(timeInMillis);

                            comentarioDTO.setCobid(jsonObject.getString("comid"));
                            comentarioDTO.setObid(jsonObject.getString("obid"));
                            comentarioDTO.setUsuid(jsonObject.getString("usuid"));
                            comentarioDTO.setDataComentario(sqlDate);
                            comentarioDTO.setCobcomentario(jsonObject.getString("comentario"));
                            comentarioList.add(comentarioDTO);
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return comentarioList;
        }

        @Override
        protected void onPostExecute(List<ComentarioDTO> bookList) {
            if (listener != null) {
                listener.onBookListReceived(bookList);
            }
        }
    }
}