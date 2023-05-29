package com.example.ibookApp.functions;

public class Constants {

    // database or db name
    public static final String BASE_URL_API = "http://15.228.241.26/api/";
    public static final String DATABASE_NAME = "IBOOK_DB";
    //database version
    public static final int DATABASE_VERSION = 4;

    // table name
    public static final String TABLE_NAME = "USUARIO";
    public static final String TABLE_NAME_OBRAS = "OBRAS";
    public static final String TABLE_NAME_COMENTARIOS = "COMENTARIOS";

    // table column or field name
    public static final String C_ID = "USUID";
    public static final String C_IMAGE = "USUIMAGE";
    public static final String C_NAME = "USUNOME";
    public static final String C_PASS = "USUSENHA";
    public static final String C_EMAIL = "USUEMAIL";
    public static final String C_ADMIN = "USUADMIN";

    public static final String O_ID = "OBID";
    public static final String O_TITULO = "OBTITULO";
    public static final String O_EDITORA = "OBEDITORA";
    public static final String O_AVALIACAO = "OBAVALIACAO";
    public static final String O_AUTOR = "OBAUTOR";
    public static final String O_CATEGORIAS = "OBCATEGORIAS";
    public static final String O_IMAGE = "OBIMAGE";
    public static final String O_SINOPSE = "OBSINOPSE";
    public static final String O_ISBN = "OBISBN";
    public static final String O_STATUS = "OBSTATUS";
    public static final String CO_ID = "COID";
    public static final String CO_COMENTARIO = "COMENTARIO";
    public static final String CO_USUID = "USUID";
    public static final String CO_OBID = "OBID";

    // query for create table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_IMAGE + " TEXT, "
            + C_NAME + " TEXT, "
            + C_PASS + " TEXT, "
            + C_EMAIL + " TEXT"
            + C_ADMIN + " BOOL"
            + " );";

    public static final String CREATE_TABLE_OBRAS = "CREATE TABLE " + TABLE_NAME_OBRAS + "( "
            + O_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + O_TITULO + " TEXT, "
            + O_EDITORA + " TEXT, "
            + O_AVALIACAO + " TEXT, "
            + O_ISBN + " TEXT, "
            + O_AUTOR + " TEXT,"
            + O_CATEGORIAS + " TEXT,"
            + O_IMAGE + " TEXT,"
            + O_SINOPSE + " TEXT,"
            + O_STATUS + " TEXT"
            + " );";

    public static final String CREATE_TABLE_COMENTARIOS = "CREATE TABLE " + TABLE_NAME_COMENTARIOS + "( "
            + CO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CO_COMENTARIO + " TEXT, "
            + CO_USUID + " INTEGER, "
            + CO_OBID + " INTEGER, "
            + "FOREIGN KEY ("+ CO_USUID + ") REFERENCES USUARIO("+ C_ID + "), "
            + "FOREIGN KEY ("+ CO_OBID + ") REFERENCES OBRAS("+ O_ID + ")"
            + " );";


    // Create database helper class for CRUD Query And Database Creation


}
