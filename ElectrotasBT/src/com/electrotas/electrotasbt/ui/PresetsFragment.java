package com.electrotas.electrotasbt.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electrotas.electrotasbt.R;

public class PresetsFragment extends Fragment {
	
	private HomeActivity act;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View ui = inflater
				.inflate(R.layout.presetsfrag, container, false);
		
		act = (HomeActivity) getActivity();
		act.getSupportActionBar().setTitle(R.string.fragname_Preset);
		
		return ui;
	}
	
}
