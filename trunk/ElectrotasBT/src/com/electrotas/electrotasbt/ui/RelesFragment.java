package com.electrotas.electrotasbt.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.electrotas.electrotasbt.R;

public class RelesFragment extends Fragment {

	private HomeActivity act;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View ui = inflater.inflate(R.layout.relesfrag, container, false);
		act = (HomeActivity) getActivity();

		ToggleButton rele1 = (ToggleButton) ui.findViewById(R.id.rele1);
		rele1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				act.getDispositivo().toggleRele(1, isChecked);
			}

		});
		return ui;
	}
}
