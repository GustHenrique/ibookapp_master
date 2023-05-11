package com.example.ibookApp.functions;

public class Constants {

    // database or db name
    public static final String DATABASE_NAME = "IBOOK_DB";
    //database version
    public static final int DATABASE_VERSION = 1;

    // table name
    public static final String TABLE_NAME = "USUARIO";

    // table column or field name
    public static final String C_ID = "USUID";
    public static final String C_IMAGE = "USUIMAGE";
    public static final String C_NAME = "USUNOME";
    public static final String C_PASS = "USUSENHA";
    public static final String C_EMAIL = "USUEMAIL";

    // query for create table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_IMAGE + " TEXT, "
            + C_NAME + " TEXT, "
            + C_PASS + " TEXT, "
            + C_EMAIL + " TEXT"
            + " );";


    // Create database helper class for CRUD Query And Database Creation


}
