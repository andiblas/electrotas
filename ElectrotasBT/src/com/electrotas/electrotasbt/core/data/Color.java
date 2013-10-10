package com.electrotas.electrotasbt.core.data;

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
	
	public static Cursor select(Context ctx){
		DBHelper dbHelper = DBProvider.obtenerConex(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		if (db == null) return null;
		
		return db.query(Color.class.getSimpleName(), null, null, null, null, null, null);
		
	}
	
	
}
