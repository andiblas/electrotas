package com.electrotas.electrotasbt.ui;

import java.util.ArrayList;

import afzkl.development.colorpickerview.view.ColorPickerView;
import afzkl.development.colorpickerview.view.ColorPickerView.OnColorChangedListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

import com.electrotas.electrotasbt.R;
import com.electrotas.electrotasbt.core.data.Color;
import com.electrotas.electrotasbt.helpers.Tostada;
import com.electrotas.electrotasbt.ui.adapters.GridColoresAdapter;

public class ColoresFragment extends Fragment {

	private HomeActivity act;
	private int colorActual = 0;
	private ArrayList<Color> mLista;
	private GridColoresAdapter adap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View ui = inflater
				.inflate(R.layout.coloresfrag, container, false);
		act = (HomeActivity) getActivity();
		final ColorPickerView cpv = (ColorPickerView) ui
				.findViewById(R.id.ColorPicker);
		setHasOptionsMenu(true);

		cpv.setOnColorChangedListener(new OnColorChangedListener() {
			@Override
			public void onColorChanged(int newColor) {
				cambiarColor(newColor);
			}
		});

		mLista = Color.select(getActivity().getApplicationContext());

		if (mLista != null)
			adap = new GridColoresAdapter(mLista, getActivity().getApplicationContext());
		
		GridView grdColores = (GridView) ui.findViewById(R.id.gridColores);
		grdColores.setAdapter(adap);
		grdColores.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				cpv.setColor(android.graphics.Color.parseColor(mLista.get(arg2)
						.getColor()));
				cambiarColor(android.graphics.Color.parseColor(mLista.get(arg2)
						.getColor()));
			}
		});
		grdColores.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mLista.get(arg2).delete(getActivity().getApplicationContext());
				mLista.remove(arg2);
				adap.notifyDataSetChanged();
				return false;
			}
		});

		return ui;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inf) {
		inf.inflate(R.menu.coloresfrag, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_guardarColor:
			try {
				Color nuevo = new Color(colorActual);
				nuevo.insert(getActivity().getApplicationContext());
				mLista.add(nuevo);
				adap.notifyDataSetChanged();
				Tostada.mostrar(getActivity().getApplicationContext(),
						"Color guardado!", null, Tostada.MENSAJE_BUENO);
			} catch (Exception e) {
				Tostada.mostrar(getActivity().getApplicationContext(),
						"Ocurrio un problema al guardar el color.", null,
						Tostada.MENSAJE_MALO);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	//funciones
	private void cambiarColor(int c){
		if (colorActual == c)
			return;
		colorActual = c;
		act.getDispositivo().cambiarColor(c);
	}

}
