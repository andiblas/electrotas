package com.electrotas.electrotasbt.ui;

import java.io.IOException;
import java.io.OutputStream;

import afzkl.development.colorpickerview.view.ColorPickerView;
import afzkl.development.colorpickerview.view.ColorPickerView.OnColorChangedListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electrotas.electrotasbt.R;

public class ColoresFragment extends Fragment {

	private HomeActivity act;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View ui = inflater.inflate(R.layout.coloresfrag, container, false);
		act = (HomeActivity) getActivity();
		ColorPickerView cpv = (ColorPickerView) ui.findViewById(R.id.ColorPicker);

		cpv.setOnColorChangedListener(new OnColorChangedListener() {
			@Override
			public void onColorChanged(int newColor) {
				
				if (act.getOutStream() == null) return;
				
				ui.setBackgroundColor(newColor);
				byte[] buf0 = new byte[1];
				byte[] buf1 = new byte[1];
				OutputStream loc = act.getOutStream();
				
				try {
					buf0[0] = (byte) 001;
					buf1[0] = (byte) ((newColor >> 16) & 0xff);
					loc.write(buf0);
					loc.write(buf1);
					loc.flush();
					
					buf0[0] = (byte) 002;
					buf1[0] = (byte) ((newColor >> 8) & 0xff);
					loc.write(buf0);
					loc.write(buf1);
					loc.flush();
					
					buf0[0] = (byte) 003;
					buf1[0] = (byte) (newColor & 0xff);
					loc.write(buf0);
					loc.write(buf1);
					loc.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		return ui;
	}

}
