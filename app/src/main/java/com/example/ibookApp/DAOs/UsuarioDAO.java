package com.example.ibookApp.DAOs;

import static com.example.ibookApp.functions.Utils.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ibookApp.functions.Constants;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.DbHelper;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class UsuarioDAO {
    private DbHelper con;
    private SQLiteDatabase db;

    public UsuarioDAO(Context context){
        con = new DbHelper(context);
        db = con.getWritableDatabase();
    }

    public void inserirUsuario(UsuarioDTO usuario){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.C_IMAGE, usuario.getUsuimagem());
        contentValues.put(Constants.C_EMAIL, usuario.getUsuemail());
        contentValues.put(Constants.C_PASS, usuario.getUsusenha());
        contentValues.put(Constants.C_NAME, usuario.getUsunome());
        db.insert(Constants.TABLE_NAME, null, contentValues);
    }

    public void recuperarSenha(UsuarioDTO usuario, String novaSenha) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        ContentValues contentValues = new ContentValues();
        SecretKey secret = generateKey();
        byte[] encryptSenha = encryptMsg(novaSenha, secret);
        novaSenha = bytesToString(encryptSenha);
        contentValues.put(Constants.C_PASS, novaSenha);
        //update data in row, It will return id of record
        db.update(Constants.TABLE_NAME,contentValues,Constants.C_ID+" =? ",new String[]{usuario.getUsuid()} );
    }

    public boolean existeEmailCadastrado(String usuemail){
        UsuarioDTO usuario = new UsuarioDTO(null,null,null,null,null);
        String selectQuery =  "SELECT * FROM "+ Constants.TABLE_NAME + " WHERE " + Constants.C_EMAIL + " =\"" + usuemail + "\"";
        try{
            Cursor cursor = db.rawQuery(selectQuery,null);
            if (cursor.moveToFirst()){
                do {
                    //get data
                    String name =  ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME));
                    String image = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE));
                    String pass = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PASS));
                    String email = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL));
                    String id = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ID));
                    //set data
                    usuario = new UsuarioDTO(email,pass,name,id,image);

                }while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            usuario = new UsuarioDTO(null,null,null,null,null);
        }
        if (usuario.getUsuid() == "" || usuario.getUsuid() == null){
            return true;
        }
        else{
            return false;
        }
    }
    public UsuarioDTO autenticarUsuario(String usuemail, String ususenha) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        SecretKey secret = generateKey();
        byte[] encryptSenha = encryptMsg(ususenha, secret);
        ususenha = bytesToString(encryptSenha);
        UsuarioDTO usuario = new UsuarioDTO(null,null,null,null,null);
        String selectQuery =  "SELECT * FROM "+ Constants.TABLE_NAME + " WHERE " + Constants.C_EMAIL + " =\"" + usuemail + "\"" + " AND " + Constants.C_PASS + " =\"" + ususenha + "\"";
        try{
            Cursor cursor = db.rawQuery(selectQuery,null);
            if (cursor.moveToFirst()){
                do {
                    //get data
                    String name =  ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME));
                    String image = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE));
                    String pass = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PASS));
                    String email = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL));
                    String id = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ID));
                    //set data
                    usuario = new UsuarioDTO(email,pass,name,id,image);

                }while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            usuario = new UsuarioDTO(null,null,null,null,null);
        }
        return usuario;
    }

    public UsuarioDTO retornarUsuarioEmail(String usuemail) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        UsuarioDTO usuario = new UsuarioDTO(null,null,null,null,null);
        String selectQuery =  "SELECT * FROM "+ Constants.TABLE_NAME + " WHERE " + Constants.C_EMAIL + " =\"" + usuemail + "\"";
        try{
            Cursor cursor = db.rawQuery(selectQuery,null);
            if (cursor.moveToFirst()){
                do {
                    //get data
                    String name =  ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME));
                    String image = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE));
                    String pass = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PASS));
                    String email = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL));
                    String id = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ID));
                    //set data
                    usuario = new UsuarioDTO(email,pass,name,id,image);

                }while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            usuario = new UsuarioDTO(null,null,null,null,null);
        }
        return usuario;
    }
}
