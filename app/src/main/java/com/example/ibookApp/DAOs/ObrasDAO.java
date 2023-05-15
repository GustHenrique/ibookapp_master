package com.example.ibookApp.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ibookApp.DTOs.ObraDTO;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.DbHelper;
import com.example.ibookApp.functions.Constants;

import java.io.UnsupportedEncodingException;
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
    public ArrayList<ObraDTO> carregarObras(){
        //create arrayList
        ArrayList<ObraDTO> arrayList = new ArrayList<>();
        //sql command query
        String selectQuery = "SELECT * FROM "+Constants.TABLE_NAME_OBRAS;
        Cursor cursor = dbReader.rawQuery(selectQuery,null);

        // looping through all record and add to list
        if (cursor.moveToFirst()){
            do {
                ObraDTO obra = new ObraDTO(
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_ID)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_TITULO)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_EDITORA)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_AVALIACAO)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_AUTOR)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_CATEGORIAS)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_STATUS)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_SINOPSE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_ISBN))
                );
                arrayList.add(obra);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }

    public ArrayList<ObraDTO> carregarObrasMaisComentadas(){
        //create arrayList
        ArrayList<ObraDTO> arrayList = new ArrayList<>();
        //sql command query
        String selectQuery = "SELECT * FROM "+Constants.TABLE_NAME_OBRAS;
        Cursor cursor = dbReader.rawQuery(selectQuery,null);

        // looping through all record and add to list
        if (cursor.moveToFirst()){
            do {
                ObraDTO obra = new ObraDTO(
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_ID)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_TITULO)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_EDITORA)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_AVALIACAO)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_AUTOR)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_CATEGORIAS)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_STATUS)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_SINOPSE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.O_ISBN))
                );
                arrayList.add(obra);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }

}
