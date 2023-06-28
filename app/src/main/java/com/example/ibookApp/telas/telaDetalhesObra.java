package com.example.ibookApp.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ibookApp.APIs.AdicionarAvaliarObrasApi;
import com.example.ibookApp.APIs.AtualizarAvaliarObrasApi;
import com.example.ibookApp.APIs.AvaliacoesPorObraObrasApi;
import com.example.ibookApp.APIs.DesfavoritarApi;
import com.example.ibookApp.APIs.FavoritarApi;
import com.example.ibookApp.APIs.InsertComentariosApi;
import com.example.ibookApp.APIs.RatingObrasApi;
import com.example.ibookApp.APIs.comentariosPorLivroApi;
import com.example.ibookApp.Adapters.ComentarioAdapter;
import com.example.ibookApp.DTOs.AvaliacaoDTO;
import com.example.ibookApp.DTOs.ComentarioDTO;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.DTOs.favoritosDTO;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.FavoritosListSingleton;
import com.example.ibookApp.functions.ObrasListSingleton;
import com.example.ibookApp.functions.UserSingleton;
import com.example.ibookApp.functions.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class telaDetalhesObra extends AppCompatActivity {

    private String obid, title, subtitle, synopsis, author, editora, dataPublicacao,dataFinalizacao, isbn, image,
            tipo, statusObra, categorias, usuid, avarageRatingString;
    private int paginas;
    private float avarageRating;
    private TextView tvNome, tvAutor, tvSinopse, tvRate;
    private EditText txtComentar;
    private RatingBar ratingBar;
    private Button btnHome, btnLogout, btnFavorito,btnAvaliarObra;
    private ImageView imgObra;
    private ComentarioAdapter comentarioAdapter;
    private RecyclerView rviComents;
    private FloatingActionButton flaBtn;
    ArrayList<ComentarioDTO> comentariosList = new ArrayList<>();
    obrasDTO obra = new obrasDTO(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null, null);
    ArrayList<favoritosDTO> favLists = FavoritosListSingleton.getInstance().getFavList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teladetalhesobra);
        Intent intent = getIntent();
        UsuarioDTO userLogado = UserSingleton.getInstance().getUser();
        usuid = userLogado.getId();
        obid = intent.getStringExtra("obid");
        title = intent.getStringExtra("title");
        subtitle = intent.getStringExtra("subtitle");
        synopsis = intent.getStringExtra("synopsis");
        author = intent.getStringExtra("author");
        editora = intent.getStringExtra("editora");
        dataPublicacao = intent.getStringExtra("dataPublicacao");
        dataFinalizacao = intent.getStringExtra("dataFinalizacao");
        isbn = intent.getStringExtra("isbn");
        paginas = intent.getIntExtra("paginas", 0);
        image = intent.getStringExtra("image");
        tipo = intent.getStringExtra("tipo");
        avarageRatingString = intent.getStringExtra("avarageRating");
        statusObra = intent.getStringExtra("statusObra");
        categorias = intent.getStringExtra("categorias");
        flaBtn = findViewById(R.id.fabDetalheObra);
        txtComentar = findViewById(R.id.txtComentarDetalheObra);
        btnHome = findViewById(R.id.btnBackHome);
        btnLogout = findViewById(R.id.btnLogoutDetalhesObra);
        tvNome = findViewById(R.id.txtTitleDetalheObra);
        tvAutor = findViewById(R.id.txtAutorDetalheObra);
        ratingBar = findViewById(R.id.rbCadastroObraAvaliacaoDetalheObra);
        tvRate = findViewById(R.id.txtAvarageRatingDetalheObra);
        btnFavorito = findViewById(R.id.favoritebookDetalheObra);
        tvSinopse = findViewById(R.id.txtSinopseDetalheObra);
        imgObra = findViewById(R.id.imgDetalheObra);
        rviComents = findViewById(R.id.rviComents);
        btnAvaliarObra = findViewById(R.id.btnAvaliarObra);
        obra = new obrasDTO(obid,title,subtitle,synopsis,author,editora,dataPublicacao,dataFinalizacao,isbn,String.valueOf(paginas),image,null,tipo,avarageRatingString,statusObra,categorias, usuid);
        ObrasListSingleton obrasListSingleton = ObrasListSingleton.getInstance();
        ratingBar.setIsIndicator(true);
        loadData(1);
        carregarComentarios();

        flaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comentar();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                enviarNota(rating);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Acessar = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(Acessar);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        btnAvaliarObra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(telaDetalhesObra.this);
                TextView title = new TextView(telaDetalhesObra.this);
                title.setText("Avaliação da Obra Selecionada");
                title.setGravity(Gravity.CENTER);
                builder.setCustomTitle(title);

                LinearLayout layout = new LinearLayout(telaDetalhesObra.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CENTER_HORIZONTAL);
                layout.setPadding(0, 5, 0, 0);

                RatingBar ratingBar = new RatingBar(telaDetalhesObra.this);
                ratingBar.setNumStars(5);
                ratingBar.setStepSize(0.5f);
                ratingBar.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                ratingBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.dourada)));

                layout.addView(ratingBar);
                builder.setView(layout);
                AlertDialog.Builder builder1 = builder.setPositiveButton("Avaliar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        float rating = ratingBar.getRating();
                        String ratingString = Float.toString(rating);
                        AvaliacaoDTO avaliacao = new AvaliacaoDTO(null, usuid, obid, ratingString);
                        List<AvaliacaoDTO> listaAvaliacao = new ArrayList<>();
                        AvaliacoesPorObraObrasApi.AvaliarObrasAsyncTask task = new AvaliacoesPorObraObrasApi.AvaliarObrasAsyncTask(obid, new AvaliacoesPorObraObrasApi.AvaliarObrasAListener() {
                            @Override
                            public void onInsertObrasReceived(List<AvaliacaoDTO> avaliacaoList) {
                                listaAvaliacao.addAll(avaliacaoList);
                                boolean jaAvaliou = false;

                                if (listaAvaliacao != null && !listaAvaliacao.isEmpty()) {
                                    for (AvaliacaoDTO objeto : listaAvaliacao) {
                                        if (objeto.getUsuid().equals(usuid)) {
                                            objeto.setAvaliacao(ratingString);
                                            jaAvaliou = true;
                                            AtualizarAvaliarObrasApi.AtualizarAvaliarObrasAsyncTask task1 = new AtualizarAvaliarObrasApi.AtualizarAvaliarObrasAsyncTask(usuid, obid, ratingString, new AtualizarAvaliarObrasApi.AtualizarAvaliarObrasAListener() {
                                                @Override
                                                public void onInsertObrasReceived() {
                                                    int escalaMinima = 0;
                                                    int escalaMaxima = 5;
                                                    int soma = 0;
                                                    for (AvaliacaoDTO avaliacaoMedia : listaAvaliacao) {
                                                        soma += Float.parseFloat(avaliacaoMedia.getAvaliacao());
                                                    }

                                                    int media = soma / listaAvaliacao.size();
                                                    int diferenca = media - escalaMinima;
                                                    int intervalo = escalaMaxima - escalaMinima;
                                                    double percentual = (double) diferenca / intervalo * 100;
                                                    double resultadoFinal = (percentual / 100) * escalaMaxima;

                                                    RatingObrasApi.RatingObrasAsyncTask task4 = new RatingObrasApi.RatingObrasAsyncTask(
                                                            obid, ratingString,
                                                            new RatingObrasApi.RatingObrasAListener() {
                                                                @Override
                                                                public void onInsertBookReceived(boolean success) {
                                                                    if (success) {

                                                                    } else {
                                                                        Toast.makeText(telaDetalhesObra.this, "Ops! Algo deu errado!", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            }
                                                    );
                                                    task4.execute();

                                                    obra.setAvarageRating(String.valueOf(resultadoFinal));
                                                    ObrasListSingleton.getInstance().atualizarObra(obra);
                                                    avarageRating = (float) resultadoFinal;
                                                    avarageRatingString = String.valueOf(avarageRating);
                                                    loadData(2);
                                                }
                                            });
                                            task1.execute();

                                            break;
                                        }
                                    }
                                }

                                if (!jaAvaliou) {
                                    listaAvaliacao.add(avaliacao);
                                    AdicionarAvaliarObrasApi.AdicionarAvaliarObrasAsyncTask task3 = new AdicionarAvaliarObrasApi.AdicionarAvaliarObrasAsyncTask(usuid, obid, ratingString, new AdicionarAvaliarObrasApi.AdicionarAvaliarObrasAListener() {
                                        @Override
                                        public void onInsertObrasReceived() {
                                            int escalaMinima = 0;
                                            int escalaMaxima = 5;
                                            int soma = 0;
                                            for (AvaliacaoDTO avaliacaoMedia : listaAvaliacao) {
                                                soma += Float.parseFloat(avaliacaoMedia.getAvaliacao());
                                            }

                                            int media = soma / listaAvaliacao.size();
                                            int diferenca = media - escalaMinima;
                                            int intervalo = escalaMaxima - escalaMinima;
                                            double percentual = (double) diferenca / intervalo * 100;
                                            double resultadoFinal = (percentual / 100) * escalaMaxima;

                                            RatingObrasApi.RatingObrasAsyncTask task4 = new RatingObrasApi.RatingObrasAsyncTask(
                                                    obid, ratingString,
                                                    new RatingObrasApi.RatingObrasAListener() {
                                                        @Override
                                                        public void onInsertBookReceived(boolean success) {
                                                            if (success) {

                                                            } else {
                                                                Toast.makeText(telaDetalhesObra.this, "Ops! Algo deu errado!", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    }
                                            );
                                            task4.execute();

                                            obra.setAvarageRating(String.valueOf(resultadoFinal));
                                            ObrasListSingleton.getInstance().atualizarObra(obra);
                                            avarageRating = (float) resultadoFinal;
                                            avarageRatingString = String.valueOf(avarageRating);
                                            loadData(2);
                                        }
                                    });
                                    task3.execute();
                                }
                            }
                        });
                        task.execute();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        positiveButton.setTextColor(Color.BLACK);
                        negativeButton.setTextColor(Color.BLACK);
                    }
                });
                dialog.show();
            }
        });



        btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean salvo = false;
                if (favLists != null && !favLists.isEmpty()) {
                    for (favoritosDTO objeto : favLists) {
                        if (objeto.getObid().equals(obid)) {
                            salvo = true;
                            break;
                        }
                        else{
                            salvo = false;
                        }
                    }
                }

                if (salvo) {
                    DesfavoritarApi.DesavoritarAsyncTask task = new DesfavoritarApi.DesavoritarAsyncTask(userLogado.getId(), obid, new DesfavoritarApi.DesfavoritarListener() {
                        @Override
                        public void onDeleteFavReceived(boolean success) {
                            if (success) {
                                FavoritosListSingleton.getInstance().removerFav(userLogado.getId(), obid);
                                btnFavorito.setBackgroundResource(R.drawable.ic_unsave_foreground);
                            } else {
                                btnFavorito.setBackgroundResource(R.drawable.ic_save_foreground);
                            }
                        }
                    });
                    task.execute();
                } else {
                    FavoritarApi.FavoritarAsyncTask task = new FavoritarApi.FavoritarAsyncTask(userLogado.getId(), obid, new FavoritarApi.FavoritarListener() {
                        @Override
                        public void onInsertFavReceived(boolean success) {
                            if (success) {
                                favoritosDTO newFav = new favoritosDTO(null, userLogado.getId(), obid);
                                FavoritosListSingleton.getInstance().adicionarFav(newFav);
                                btnFavorito.setBackgroundResource(R.drawable.ic_save_foreground);
                            } else {
                                btnFavorito.setBackgroundResource(R.drawable.ic_unsave_foreground);
                            }
                        }
                    });
                    task.execute();
                }
            }
        });

        if (favLists != null && !favLists.isEmpty()) {
            for (favoritosDTO objeto : favLists) {
                if (objeto.getObid().equals(obid)) {
                    btnFavorito.setSelected(true);
                    btnFavorito.setBackgroundResource(R.drawable.ic_save_foreground);
                    break;
                }
                else{
                    btnFavorito.setSelected(false);
                    btnFavorito.setBackgroundResource(R.drawable.ic_unsave_foreground);
                }
            }
        }
    }

    public void comentar(){
        if (txtComentar != null){
            String comentario = txtComentar.getText().toString();
            if (!comentario.isEmpty()){
                InsertComentariosApi.InsertComentariosAsyncTask task = new InsertComentariosApi.InsertComentariosAsyncTask(usuid,obid,comentario, new InsertComentariosApi.InsertComentariosListener() {

                    @Override
                    public void onInsertComentariosReceived(boolean success) {

                    }
                });
                task.execute();
                txtComentar.setText("");
                carregarComentarios();
            }
        }
    }

    public void carregarComentarios(){
        comentariosPorLivroApi.getcomentariosPorLivro(obid, new comentariosPorLivroApi.comentariosPorLivroListener() {
            @Override
            public void oncomentariosPorLivroReceived(List<ComentarioDTO> comentariosPorLivro) {
                comentariosList.addAll(comentariosPorLivro);
                ordenarComentariosPorData(comentariosList);
                comentarioAdapter = new ComentarioAdapter(comentariosList);
                int itemCount = comentarioAdapter.getItemCount();
                int itemHeight = 150;
                int recyclerViewHeight = itemCount * itemHeight;

                ViewGroup.LayoutParams layoutParams = rviComents.getLayoutParams();
                layoutParams.height = recyclerViewHeight;
                rviComents.setLayoutParams(layoutParams);
                rviComents.setAdapter(comentarioAdapter);
            }
        });
    }

    public void enviarNota(float rating){

    }

    public void loadData(int i){
        if (i == 1){
            List<AvaliacaoDTO> listaAvaliacao = new ArrayList<>();
            AvaliacaoDTO avaliacao = new AvaliacaoDTO(null, usuid, obid, avarageRatingString);
            AvaliacoesPorObraObrasApi.AvaliarObrasAsyncTask task = new AvaliacoesPorObraObrasApi.AvaliarObrasAsyncTask(obid, new AvaliacoesPorObraObrasApi.AvaliarObrasAListener() {
                @Override
                public void onInsertObrasReceived(List<AvaliacaoDTO> avaliacaoList) {
                    listaAvaliacao.addAll(avaliacaoList);
                    int escalaMinima = 0;
                    int escalaMaxima = 5;
                    double soma = 0;
                    for (AvaliacaoDTO avaliacaoMedia : listaAvaliacao) {
                        soma += Float.parseFloat(avaliacaoMedia.getAvaliacao());
                    }

                    double media = soma / listaAvaliacao.size();
                    double diferenca = media - escalaMinima;
                    int intervalo = escalaMaxima - escalaMinima;
                    double percentual = (double) diferenca / intervalo * 100;
                    double resultadoFinal = (percentual / 100) * escalaMaxima;
                    avarageRating = (float) resultadoFinal;
                    avarageRatingString = String.valueOf(avarageRating);
                    if (avarageRatingString != null && !avarageRatingString.equals("null") && !avarageRatingString.isEmpty() && !avarageRatingString.equals("NaN")) {
                        avarageRating = Float.parseFloat(avarageRatingString);
                        if (avarageRating * 2 > 9.9) {
                            int rate = 10;
                            tvRate.setText(String.valueOf(rate));
                        } else {
                            tvRate.setText(String.valueOf(avarageRating * 2));
                        }
                        ratingBar.setRating(avarageRating);
                    }
                }
            });
            task.execute();
        }
        else{
            if (avarageRatingString != null && !avarageRatingString.equals("null") && !avarageRatingString.isEmpty() && !avarageRatingString.equals("NaN")) {
                avarageRating = Float.parseFloat(avarageRatingString);
                if (avarageRating * 2 > 9.9) {
                    int rate = 10;
                    tvRate.setText(String.valueOf(rate));
                } else {
                    tvRate.setText(String.valueOf(avarageRating * 2));
                }
                ratingBar.setRating(avarageRating);
            }
        }

        if (image != null){
            Glide.with(this)
                    .load(image)
                    .into(imgObra);
        }
        else{
            Glide.with(this)
                    .load(R.drawable.ic_book_foreground)
                    .into(imgObra);
        }

        if(title != null){
            tvNome.setText(title);
        }

        if(author != null){
            tvAutor.setText(author);
        }

        else{
            tvRate.setText(String.valueOf(5));
            ratingBar.setRating(2.5f);
        }

        if(synopsis != null && !synopsis.equals("null") && !synopsis.isEmpty()){
            tvSinopse.setText(synopsis);
        }
        else{
            tvSinopse.setText("Sem sinopse para a Obra.");
        }
    }

    public void ordenarComentariosPorData(List<ComentarioDTO> comentarios) {
        Collections.sort(comentarios, new Comparator<ComentarioDTO>() {
            public int compare(ComentarioDTO c1, ComentarioDTO c2) {
                Date data1 = c1.getDataComentario();
                Date data2 = c2.getDataComentario();
                return data2.compareTo(data1);
            }
        });
    }

    public void logout(){
        Utils.logout();
        Intent acessar = new Intent(getApplicationContext(), telalogin.class);
        startActivity(acessar);
    }
}