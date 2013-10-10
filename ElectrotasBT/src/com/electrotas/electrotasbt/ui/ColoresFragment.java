package com.electrotas.electrotasbt.ui;

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

import com.electrotas.electrotasbt.R;
import com.electrotas.electrotasbt.core.data.Color;
import com.electrotas.electrotasbt.helpers.Tostada;

public class ColoresFragment extends Fragment {

	private HomeActivity act;
	private int colorActual = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View ui = inflater
				.inflate(R.layout.coloresfrag, container, false);
		act = (HomeActivity) getActivity();
		ColorPickerView cpv = (ColorPickerView) ui
				.findViewById(R.id.ColorPicker);
		setHasOptionsMenu(true);
		cpv.setOnColorChangedListener(new OnColorChangedListener() {
			@Override
			public void onColorChanged(int newColor) {
				if (colorActual == newColor)
					return;
				colorActual = newColor;
				act.getDispositivo().cambiarColor(newColor);
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
				Tostada.mostrar(getActivity().getApplicationContext(), "Color guardado!", null, Tostada.MENSAJE_BUENO);
			} catch (Exception e) {
				Tostada.mostrar(getActivity().getApplicationContext(), "Ocurrio un problema al guardar el color.", null, Tostada.MENSAJE_MALO);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
