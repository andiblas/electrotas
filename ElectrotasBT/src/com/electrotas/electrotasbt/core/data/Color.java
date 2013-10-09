package com.electrotas.electrotasbt.core.data;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Color {
	
	private int id;
	private String nombre;
	private String color;
	
	public int getId() {
		return id;
	}
	public void setId(int i) {
		id = i;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String n) {
		nombre = n;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String c) {
		color = c;
	}
	
	public long insert(Context ctx){
        DBHelper dbHelper = DBProvider.obtenerConex(ctx);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues v = new ContentValues();
        for (Field f : Color.class.getDeclaredFields()){
        	try {
				v.put(f.getName(), f.getGenericType() f.get(this));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
	}
}









