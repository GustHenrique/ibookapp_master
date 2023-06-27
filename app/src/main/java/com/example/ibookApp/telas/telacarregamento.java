package com.example.ibookApp.telas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.example.ibookApp.APIs.ibookApi;
import com.example.ibookApp.APIs.ibookMaisComentadasApi;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.ObrasListSingleton;
import com.example.ibookApp.functions.ObrasMaisComentadasListSingleton;

import java.util.List;

public class telacarregamento extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1500;
    private ProgressBar mLoadingSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telacarregamento);

        mLoadingSpinner = (ProgressBar) findViewById(R.id.loadingSpinner);
        mLoadingSpinner.setVisibility(View.VISIBLE);
        ibookApi.getBookList(new ibookApi.BookListListener() {
            @Override
            public void onBookListReceived(List<obrasDTO> bookList) {
                ObrasListSingleton obrasSingleton = ObrasListSingleton.getInstance();
                for (obrasDTO obra : bookList) {
                    obrasSingleton.adicionarObra(obra);
                }
            }
        });

        ibookMaisComentadasApi.getBookList(new ibookMaisComentadasApi.ibookMaisListListener() {
            @Override
            public void onBookListReceived(List<obrasDTO> bookList) {
                ObrasMaisComentadasListSingleton obrasMaisComentadasListSingleton = ObrasMaisComentadasListSingleton.getInstance();
                for (obrasDTO obra : bookList) {
                    obrasMaisComentadasListSingleton.adicionarObra(obra);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(telacarregamento.this, telalogin.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}