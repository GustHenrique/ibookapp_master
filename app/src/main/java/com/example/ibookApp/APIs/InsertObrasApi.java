package com.example.ibookApp.APIs;

import static com.example.ibookApp.functions.Constants.BASE_URL_API;

import android.os.AsyncTask;

import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.functions.UserSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class InsertObrasApi {
    private static final String BASE_URL = BASE_URL_API;

    public interface InsertUsuarioListener {
        void onInsertBookReceived(boolean success);
    }

    public static class InsertUsuarioAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private String title, subtitle, synopsis, author,editora,dataPublicacao,dataFinalizacao,isbn,
                paginas,image,traducao,tipo,avarageRating,statusObra,categorias;
        private InsertObrasApi.InsertUsuarioListener listener;

        public InsertUsuarioAsyncTask(String title, String subtitle, String synopsis, String author, String editora, String dataPublicacao, String dataFinalizacao, String isbn, String paginas, String image, String traducao, String tipo, String avarageRating, String statusObra, String categorias, InsertObrasApi.InsertUsuarioListener listener) {
            this.title = title;
            this.subtitle = subtitle;
            this.synopsis = synopsis;
            this.author = author;
            this.editora = editora;
            this.dataPublicacao = dataPublicacao;
            this.dataFinalizacao = dataFinalizacao;
            this.isbn = isbn;
            this.paginas = paginas;
            this.image = image;
            this.traducao = traducao;
            this.tipo = tipo;
            this.avarageRating = avarageRating;
            this.statusObra = statusObra;
            this.categorias = categorias;
            this.listener = listener;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Obras/AdicionarObra").newBuilder();
            try {
                String dataPublicacaoFormatadaString = null;
                String dataFinalizacaoFormatadaString = null;
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/M/yyyy");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                try {
                    Date dataPublicacaoc = inputFormat.parse(dataPublicacao);
                    Date dataFinalizacaoc = inputFormat.parse(dataFinalizacao);
                    dataPublicacaoFormatadaString = outputFormat.format(dataPublicacaoc);
                    dataFinalizacaoFormatadaString = outputFormat.format(dataFinalizacaoc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                UsuarioDTO userLogado = UserSingleton.getInstance().getUser();

                Integer pags = null;
                Double rate = null;
                if (paginas != null){
                    pags = Integer.parseInt(paginas);
                }
                if (paginas != null){
                    rate = Double.parseDouble(avarageRating);
                }
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("title", title);
                jsonBody.put("subtitle", subtitle);
                jsonBody.put("synopsis", synopsis);
                jsonBody.put("author", author);
                jsonBody.put("editora", editora);
                jsonBody.put("dataPublicacao", dataPublicacaoFormatadaString);
                jsonBody.put("dataFinalizacao", dataFinalizacaoFormatadaString);
                jsonBody.put("isbn", isbn);
                jsonBody.put("paginas", pags);
                jsonBody.put("image", image);
                jsonBody.put("traducao", traducao);
                jsonBody.put("tipo", tipo);
                jsonBody.put("avarageRating", rate);
                jsonBody.put("statusObra", statusObra);
                jsonBody.put("categorias", categorias);
                jsonBody.put("usuid", userLogado.getId());

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
