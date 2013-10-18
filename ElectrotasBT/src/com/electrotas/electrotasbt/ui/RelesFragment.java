package com.electrotas.electrotasbt.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.electrotas.electrotasbt.R;
import com.electrotas.electrotasbt.core.ETDevice;

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
		Log.i(RelesFragment.class.getSimpleName(), "On Create View*******");
		return ui;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.i(RelesFragment.class.getSimpleName(), "On Start*******");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.i(RelesFragment.class.getSimpleName(), "On Resume*******");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.i(RelesFragment.class.getSimpleName(), "On Pause*******");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.i(RelesFragment.class.getSimpleName(), "On Stop*******");
	}
	
	private final class Checkeador extends Thread{
		
		private final ETDevice dispo;
		
		protected Checkeador(ETDevice c){
			dispo = c;
		}
		
		@Override
		public void run() {
			while (true){
				
			}
		}
		
	}
	
}




