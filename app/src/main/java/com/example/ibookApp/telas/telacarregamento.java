package com.example.ibookApp.telas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.example.ibookApp.R;

public class telacarregamento extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1500;
    private ProgressBar mLoadingSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telacarregamento);

        mLoadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);
        mLoadingSpinner.setVisibility(View.VISIBLE);

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