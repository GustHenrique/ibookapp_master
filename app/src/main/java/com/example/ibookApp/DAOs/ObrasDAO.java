package com.example.ibookApp.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ibookApp.APIs.ibookApi;
import com.example.ibookApp.DTOs.ObraDTO;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.DbHelper;
import com.example.ibookApp.functions.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ObrasDAO {
    private DbHelper con;
    private SQLiteDatabase db, dbReader;

    public ObrasDAO(Context context){
        con = new DbHelper(context);
        db = con.getWritableDatabase();
        dbReader = con.getReadableDatabase();
    }

    public void inserirObra(ObraDTO obras){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.O_STATUS, obras.getObstatus());
        contentValues.put(Constants.O_AUTOR, obras.getObautor());
        contentValues.put(Constants.O_AVALIACAO, obras.getObavaliacao());
        contentValues.put(Constants.O_CATEGORIAS, obras.getObcategoria());
        contentValues.put(Constants.O_EDITORA, obras.getObeditora());
        contentValues.put(Constants.O_IMAGE, obras.getObimage());
        contentValues.put(Constants.O_TITULO, obras.getObtitulo());
        contentValues.put(Constants.O_SINOPSE, obras.getObsinopse());
        contentValues.put(Constants.O_ISBN, obras.getObisbn());
        db.insert(Constants.TABLE_NAME_OBRAS, null, contentValues);
    }

    public void carregarObras()  {

    }
    public ArrayList<obrasDTO> carregarObrasMaisComentadas(){
        //create arrayList
        ArrayList<obrasDTO> arrayList = new ArrayList<>();

        return arrayList;
    }

}
