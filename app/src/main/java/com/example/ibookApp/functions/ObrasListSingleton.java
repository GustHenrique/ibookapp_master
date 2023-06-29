package com.example.ibookApp.functions;

import com.example.ibookApp.DTOs.obrasDTO;

import java.util.ArrayList;

public class ObrasListSingleton {
    private static ObrasListSingleton instance;
    private ArrayList<obrasDTO> obrasList;

    private ObrasListSingleton() {
        // Construtor privado para evitar a criação direta de instâncias
        obrasList = new ArrayList<>();
    }

    public static ObrasListSingleton getInstance() {
        if (instance == null) {
            // Se a instância ainda não foi criada, cria uma nova
            instance = new ObrasListSingleton();
        }
        return instance;
    }

    public ArrayList<obrasDTO> getObrasList() {
        return obrasList;
    }

    public void adicionarObra(obrasDTO obra) {
        obrasList.add(obra);
    }

    public void atualizarObra(obrasDTO obraAtualizada) {
        for (int i = 0; i < obrasList.size(); i++) {
            obrasDTO obra = obrasList.get(i);
            if (obra.getId().equals(obraAtualizada.getId())) {
                obrasList.set(i, obraAtualizada);
                break;
            }
        }
    }

    public void resetInstance() {
        instance = null;
    }

}

