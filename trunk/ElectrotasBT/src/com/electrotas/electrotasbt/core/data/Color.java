package com.electrotas.electrotasbt.core.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Color {

	private int id;
	private String color;

	public Color() {
	}

	public Color(int a) {
		color = String.format("#%06X", (0xFFFFFF & a));
	}

	public Color(String c) {
		color = c;
	}
	
	private Color(Cursor c){
		id = c.getInt(0);
		color = c.getString(1);
	}

	public int getId() {
		return id;
	}

	public void setId(int i) {
		id = i;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String c) {
		color = c;
	}

	public long insert(Context ctx) {
		DBHelper dbHelper = DBProvider.obtenerConex(ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues v = new ContentValues();
		v.put("color", color);

		return db.insert(Color.class.getSimpleName(), null, v);
	}
	
	public static Cursor selectCS(Context ctx){
		DBHelper dbHelper = DBProvider.obtenerConex(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		if (db == null) return null;
		
		Cursor resul = null;
		try {
			resul = db.query(Color.class.getSimpleName(), null, null, null, null, null, null);
		} finally{
			DBProvider.cerrarConex();
		}
		return resul;
		
	}
	
	public static ArrayList<Color> select(Context ctx){
		DBHelper dbHelper = DBProvider.obtenerConex(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		if (db == null) return null;
		
		Cursor cursor = db.query(Color.class.getSimpleName(), null, null, null, null, null, null);

		if (cursor.getCount() <= 0)
			return new ArrayList<Color>();

		ArrayList<Color> colores = new ArrayList<Color>();

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			colores.add(new Color(cursor));
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return colores;
		
	}
	
	public void delete(Context ctx){
		DBHelper dbHelper = DBProvider.obtenerConex(ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db == null) return;
		
		try {
			db.delete(Color.class.getSimpleName(), "id = ?", new String[] {String.valueOf(this.id)});
		} finally{
			DBProvider.cerrarConex();
		}
	}
	
}













