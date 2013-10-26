package com.electrotas.electrotasbt.core.data;

import java.util.ArrayList;

import com.electrotas.electrotasbt.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Placa {
	
	private int id;
	private String nombre;
	private String mac;
	
	public Placa(){
	}
	
	public Placa(String m){
		id = -1;
		nombre = null;
		mac = m;
	}
	
	private Placa(Cursor c){
		id = c.getInt(0);
		nombre = c.getString(1);
		mac = c.getString(2);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String n) {
		this.nombre = n;
	}
	public String getMAC() {
		return mac;
	}
	public void setMAC(String m) {
		this.mac = m;
	}
	
	@Override
	public String toString() {
		return new StringBuffer().append(nombre).append(" - ").append(mac).toString();
	}
	
	public long insert(Context ctx) {
		if (mac == null || mac.length() == 0) return 0;
		DBHelper dbHelper = DBProvider.obtenerConex(ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues v = new ContentValues();
		v.put("Nombre", nombre == null ? ctx.getString(R.string.misc_sinNombre) : nombre);
		v.put("MAC", mac);
		long resul = db.insert(Placa.class.getSimpleName(), null, v);
		id = (int) resul;
		
		DBProvider.cerrarConex();
		return resul;
	}
	
	public static ArrayList<Placa> select(Context ctx){
		DBHelper dbHelper = DBProvider.obtenerConex(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		if (db == null) return null;
		
		Cursor cursor = db.query(Placa.class.getSimpleName(), null, null, null, null, null, null);
		
		if (cursor.getCount() <= 0)
			return new ArrayList<Placa>();
		
		ArrayList<Placa> placas = new ArrayList<Placa>();
		
		try {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				placas.add(new Placa(cursor));
				cursor.moveToNext();
			}
		} finally {
			cursor.close(); cursor = null;
			DBProvider.cerrarConex();
		}
		
		return placas;
		
	}
	
	/**
	 * Select en la base de datos local para buscar
	 * una placa por MAC por defecto. Carga en el objeto Actual
	 * @param ctx
	 * Contexto para poder conectar a la base de datos.
	 */
	public void load(Context ctx){
		if (mac == null || mac.length() == 0) return;
		DBHelper dbHelper = DBProvider.obtenerConex(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		if (db == null) return;
		
		Cursor cursor = db.query(Placa.class.getSimpleName(), null, "mac = ?",new String[] {mac}, null, null, null);
		
		if (cursor.getCount() <= 0){
			cursor.close();
			DBProvider.cerrarConex();
			return;
		}
		
		try {
			cursor.moveToFirst();
			id = cursor.getInt(0);
			nombre = cursor.getString(1);
			mac = cursor.getString(2);
		} finally {
			cursor.close(); cursor = null;
			DBProvider.cerrarConex();
		}
	}
	
	
}
