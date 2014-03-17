package com.electrotas.electrotasbt.ui.adapters;

import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.electrotas.electrotasbt.R;

public class BTDeviceAdapter extends ArrayAdapter<BluetoothDevice> {

	private ArrayList<BluetoothDevice> list = null;
	private final LayoutInflater infl;

	public BTDeviceAdapter(Context c, int resource, ArrayList<BluetoothDevice> objects) {
		super(c, resource, objects);
		list = objects;
		infl = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public BluetoothDevice getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MenuHolder a = null;
		
		if (convertView == null) {
			convertView = infl.inflate(R.layout.menulist, parent, false);
			TextView labelView = (TextView) convertView
					.findViewById(R.id.navmenu_texto);
			
			a = new MenuHolder();
			a.labelView = labelView;
			
			convertView.setTag(a);
		}
		
		if (a == null){
			a = (MenuHolder) convertView.getTag();	
		}
		
		a.labelView.setText(list.get(position).getName());
		
		return convertView;
	}


	private static class MenuHolder {
		private TextView labelView;
	}

}
