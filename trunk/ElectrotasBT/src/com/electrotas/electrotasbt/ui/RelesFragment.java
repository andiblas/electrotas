package com.electrotas.electrotasbt.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
	private Checkeador checker;
	
	private ToggleButton rele1;
	private ToggleButton rele2;
	private ToggleButton rele3;
	private ToggleButton rele4;
	private ToggleButton rele5;
	private ToggleButton rele6;
	private ToggleButton rele7;
	private ToggleButton rele8;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View ui = inflater.inflate(R.layout.relesfrag, container, false);
		act = (HomeActivity) getActivity();

		rele1 = (ToggleButton) ui.findViewById(R.id.rele1);
		rele2 = (ToggleButton) ui.findViewById(R.id.rele2);
		rele3 = (ToggleButton) ui.findViewById(R.id.rele3);
		rele4 = (ToggleButton) ui.findViewById(R.id.rele4);
		rele5 = (ToggleButton) ui.findViewById(R.id.rele5);
		rele6 = (ToggleButton) ui.findViewById(R.id.rele6);
		rele7 = (ToggleButton) ui.findViewById(R.id.rele7);
		rele8 = (ToggleButton) ui.findViewById(R.id.rele8);
		rele1.setOnCheckedChangeListener(event);
		rele2.setOnCheckedChangeListener(event);
		rele3.setOnCheckedChangeListener(event);
		rele4.setOnCheckedChangeListener(event);
		rele5.setOnCheckedChangeListener(event);
		rele6.setOnCheckedChangeListener(event);
		rele7.setOnCheckedChangeListener(event);
		rele8.setOnCheckedChangeListener(event);
		
		return ui;
	}

	@Override
	public void onResume() {
		super.onResume();
		activarCheckeador();
	}

	@Override
	public void onPause() {
		super.onPause();
		desactivarCheckeador();
	}
	
	private OnCheckedChangeListener event = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton bv, boolean isChecked) {
			act.getDispositivo().toggleRele(Integer.valueOf((String) bv.getTag()), isChecked);
		}
	};
	
	
	/**
	 * Este es el receptor de la difusion, hecha por el thread Checkeador
	 * cuando termina de buscar los valores de la placa
	 */
	private BroadcastReceiver mr = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean[] est = act.getDispositivo().getEstadoReles();
			rele1.setChecked(est[0]);
			rele2.setChecked(est[1]);
			rele3.setChecked(est[2]);
			rele4.setChecked(est[3]);
			rele5.setChecked(est[4]);
			rele6.setChecked(est[5]);
			rele7.setChecked(est[6]);
			rele8.setChecked(est[7]);
		}
	};
	
	
	private void activarCheckeador() {
		if (act.getDispositivo().getState() == ETDevice.STATE_NADA)
			return;

		LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(mr,new IntentFilter(Checkeador.ACCION_CHECK_READY));
		
		checker = new Checkeador(getActivity().getApplicationContext(),
				act.getDispositivo());
		checker.start();
	}

	private void desactivarCheckeador() {
		if (checker != null) {
			checker.interrupt();
			checker = null;
			LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(mr);
		}
	}

	private final class Checkeador extends Thread {

		private static final String ACCION_CHECK_READY = "com.electrotas.electrotasbt.CHECK_READY";

		private final ETDevice dispo;
		private final Context mCtx;

		protected Checkeador(Context ctx, ETDevice c) {
			dispo = c;
			mCtx = ctx;
		}

		@Override
		public void run() {
			while (!isInterrupted()) {
				try {
					dispo.actualizarEstado();
					LocalBroadcastManager.getInstance(mCtx).sendBroadcast(
							new Intent(ACCION_CHECK_READY));
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Log.e(Checkeador.class.getSimpleName(), e.getMessage());
					break;
				}
			}
		}

	}

}
