package com.electrotas.electrotasbt.core.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.electrotas.electrotasbt.R;

/**
 * 
 * @author
 * AndresP
 */
public class DBHelper extends SQLiteOpenHelper {
	
    //Sentencia SQL para crear la tabla de Usuarios
    private static final String NOMBRE_DB = "VECTIOCIAT.db";
    private static final int VERSION = 4;
    private final String[] SQLCreate;
    
    //Usar este constructor para la base de datos NOMBRE_DB (bd por default)
    public DBHelper(Context contexto) {
        super(contexto, NOMBRE_DB, null, VERSION);
        SQLCreate = contexto.getResources().getStringArray(R.array.TABLAS);
    }

//    //Usar este constructor para multiples bases de datos
//    public DBHelper(Context contexto, String nombre, CursorFactory factory, int resSQL, int version) {
//        super(contexto, nombre, factory, version);
//        SQLCreate = contexto.getResources().getStringArray(resSQL);
//    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        
        for (int i = 0; i < SQLCreate.length; i++) {
        	Log.v("Tablas", SQLCreate[i]);
            db.execSQL(SQLCreate[i]);
        }
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        
//    	Log.v("Tablas UPDATE", SQLCreate[6]);
//    	db.execSQL(SQLCreate[6]);
//    	Log.v("Tablas UPDATE", "Ejecuto todo bien!");
        
    }
}