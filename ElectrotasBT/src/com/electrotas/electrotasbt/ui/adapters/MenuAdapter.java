package com.electrotas.electrotasbt.ui.adapters;

import java.util.Hashtable;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.electrotas.electrotasbt.R;

public class MenuAdapter extends BaseAdapter {
	
	private static final String MENU_HOME = "Home";
	private static final String MENU_COLORES = "Colores";
	private static final String MENU_RELES = "Reles";
	
	private final Activity mCtx;
	private final Hashtable<String, Integer> vals = new Hashtable<String, Integer>();

	public MenuAdapter(Activity ctx) {
		mCtx = ctx;
		vals.put(MENU_HOME, R.drawable.alerta);
		vals.put(MENU_COLORES, R.drawable.ok);
		vals.put(MENU_RELES, R.drawable.icon);
	}

	@Override
	public int getCount() {
		return vals.size();
	}

	@Override
	public Object getItem(int position) {
		return vals.get(getKey(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vItem;
		
		if (convertView == null) {
			
			convertView = mCtx.getLayoutInflater().inflate(R.layout.menulist, null);
			
			vItem = new ViewHolder();
			vItem.icono = (ImageView) convertView.findViewById(R.id.menu_icon);
			
			vItem.txt = (TextView) convertView.findViewById(R.id.menu_texto);
			
			convertView.setTag(vItem);

		} else {
			vItem = (ViewHolder) convertView.getTag();
		}
		
		vItem.txt.setText(getKey(position));
		vItem.icono.setImageResource(vals.get(getKey(position)));
		
		// Se devuelve ya la vista nueva o reutilizada que ha sido dibujada
		return convertView;
		
	}

	private String getKey(int pos){
		switch (pos) {
		case 0:
			return MENU_HOME;
		case 1:
			return MENU_COLORES;
		case 2:
			return MENU_RELES;
		default:
			return null;
		}
	}
	
	private static final class ViewHolder
	{
		ImageView icono;
		TextView txt;
	}
	
	
}
