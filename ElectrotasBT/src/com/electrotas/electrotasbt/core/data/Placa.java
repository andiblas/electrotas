package com.electrotas.electrotasbt.core.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Placa {
	
	private int id;
	private String nombre;
	private String mac;
	
	public Placa(){
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
	
	public long insert(Context ctx) {
		if (mac == null || mac.length() == 0) return 0;
		DBHelper dbHelper = DBProvider.obtenerConex(ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues v = new ContentValues();
		v.put("Nombre", nombre);
		v.put("MAC", mac);
		long resul = db.insert(Placa.class.getSimpleName(), null, v);
		
		DBProvider.cerrarConex();
		return resul;
	}
	
	
}
