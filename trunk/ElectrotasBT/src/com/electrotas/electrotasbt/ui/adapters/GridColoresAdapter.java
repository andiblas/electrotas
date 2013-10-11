package com.electrotas.electrotasbt.ui.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.electrotas.electrotasbt.core.data.Color;

public class GridColoresAdapter extends BaseAdapter {

	private final Context mCtx;
	private final ArrayList<Color> mLista;

	public GridColoresAdapter(ArrayList<Color> l, Context ctx) {
		mLista = l;
		mCtx = ctx;
	}

	@Override
	public int getCount() {
		return mLista.size();
	}

	@Override
	public Object getItem(int position) {
		return mLista.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mLista.get(position).getId();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView resul;
		if (convertView == null) {
			resul = new TextView(mCtx);
			resul.setHeight(40);
		} else {
			resul = (TextView) convertView;
		}

		resul.setBackgroundColor(android.graphics.Color.parseColor(mLista.get(
				position).getColor()));
		//resul.setText(mLista.get(position).getColor());
		return resul;
	}

}
