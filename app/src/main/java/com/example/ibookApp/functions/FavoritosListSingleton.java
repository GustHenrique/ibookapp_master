package com.example.ibookApp.functions;

import com.example.ibookApp.DTOs.favoritosDTO;

import java.util.ArrayList;

public class FavoritosListSingleton {
    private static FavoritosListSingleton instance;
    private ArrayList<favoritosDTO> favList;

    private FavoritosListSingleton() {
        favList = new ArrayList<>();
    }

    public static FavoritosListSingleton getInstance() {
        if (instance == null) {
            instance = new FavoritosListSingleton();
        }
        return instance;
    }

    public ArrayList<favoritosDTO> getFavList() {
        return favList;
    }

    public void adicionarObra(favoritosDTO obra) {
        favList.add(obra);
    }

}

