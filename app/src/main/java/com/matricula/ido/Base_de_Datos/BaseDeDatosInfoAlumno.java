package com.matricula.ido.Base_de_Datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class BaseDeDatosInfoAlumno extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Alumnos.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.NOMBRE + " TEXT," +
                    FeedReaderContract.FeedEntry.APELLIDO + " TEXT," +
                    FeedReaderContract.FeedEntry.IDENTIDAD + " TEXT," +
                    FeedReaderContract.FeedEntry.FECHA_HORA + " TEXT," +
                    FeedReaderContract.FeedEntry.GRADO + " TEXT," +
                    FeedReaderContract.FeedEntry.GRUPO + " TEXT," +
                    FeedReaderContract.FeedEntry.MODALIDAD + " TEXT," +
                    FeedReaderContract.FeedEntry.MODULO + " TEXT," +
                    FeedReaderContract.FeedEntry.JORNADA + " TEXT," +
                    FeedReaderContract.FeedEntry.CONTADOR_GRUPOS + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    public BaseDeDatosInfoAlumno(Context context){
        super(context,DATABASE_NAME ,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public final class FeedReaderContract {
        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        private FeedReaderContract() {}

        /* Inner class that defines the table contents */
        public class FeedEntry implements BaseColumns {
            public static final String TABLE_NAME = "infoAlumno";
            public static final String NOMBRE = "nombres";
            public static final String APELLIDO = "apèllidos";
            public static final String IDENTIDAD = "identidad";
            public static final String GRADO = "grado";
            public static final String GRUPO = "grupos";
            public static final String MODULO = "modulo";
            public static final String MODALIDAD = "modalidad";
            public static final String JORNADA = "jornada";
            public static final String CONTADOR_GRUPOS = "contador_grupos";
            public static final String FECHA_HORA = "fecha_hora";
        }
    }
}
