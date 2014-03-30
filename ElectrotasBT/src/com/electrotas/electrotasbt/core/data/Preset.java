package com.electrotas.electrotasbt.core.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.electrotas.electrotasbt.R;

public class Preset {
	
	private int id;
	private String nombre;
	private int color;
	private ArrayList<Integer> reles = new ArrayList<Integer>();
	
	public Preset() {
	}

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
	public int getColor() {
		return color;
	}
	public void setColor(int c) {
		color = c;
	}
	public ArrayList<Integer> getReles() {
		return reles;
	}
	public void setReles(ArrayList<Integer> r) {
		reles = r;
	}

	public void insert(Context ctx) {
		DBHelper dbHelper = DBProvider.obtenerConex(ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues v = new ContentValues();
		v.put("Nombre", nombre == null ? ctx.getString(R.string.misc_sinNombre) : nombre);
		v.put("Color", color);
		for (int r = 0; r < reles.size(); r++) {
			if (reles.get(r) != null)
				v.put("Rele" + (r + 1), reles.get(r));
		}
		id = (int) db.insert(Preset.class.getSimpleName(), null, v);

		DBProvider.cerrarConex();
		return;
	}
	
	
	
	
}
