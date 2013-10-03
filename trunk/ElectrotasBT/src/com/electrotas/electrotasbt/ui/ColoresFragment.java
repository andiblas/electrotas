package com.electrotas.electrotasbt.ui;

import afzkl.development.colorpickerview.view.ColorPickerView;
import afzkl.development.colorpickerview.view.ColorPickerView.OnColorChangedListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electrotas.electrotasbt.R;

public class ColoresFragment extends Fragment {

	private HomeActivity act;
	private int colorAnterior = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View ui = inflater.inflate(R.layout.coloresfrag, container, false);
		act = (HomeActivity) getActivity();
		ColorPickerView cpv = (ColorPickerView) ui.findViewById(R.id.ColorPicker);

		cpv.setOnColorChangedListener(new OnColorChangedListener() {
			@Override
			public void onColorChanged(int newColor) {
				if (colorAnterior == newColor) {
					Log.d("evitado " + String.valueOf(newColor), "*************Evitado**************");
					return;
				}
				colorAnterior = newColor;
				Log.d("eventooooooo " + String.valueOf(newColor), "*************EVENTOOOOOOOOOOOO**************");
				act.getDispositivo().cambiarColor(newColor);
			}
		});

		return ui;
	}

}
