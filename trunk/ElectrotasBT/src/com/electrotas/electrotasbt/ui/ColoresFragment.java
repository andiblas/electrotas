package com.electrotas.electrotasbt.ui;

import afzkl.development.colorpickerview.view.ColorPickerView;
import afzkl.development.colorpickerview.view.ColorPickerView.OnColorChangedListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electrotas.electrotasbt.R;

public class ColoresFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View ui = inflater.inflate(R.layout.coloresfrag, container, false);
		ColorPickerView cpv = (ColorPickerView) ui.findViewById(R.id.ColorPicker);
		
		cpv.setOnColorChangedListener(new OnColorChangedListener() {
			@Override
			public void onColorChanged(int newColor) {
				ui.setBackgroundColor(newColor);
			}
		});
		
		return ui;
	}
	
}
