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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	
	private static final int CANTIDAD_RELES = 8;
	
	private ToggleButton[] reles = new ToggleButton[CANTIDAD_RELES];
	
	
	//Array de flags para cada Rele que avisa que se cambio
	//el estado del rele en la app para despues ignorar el
	//cambio que viene por el thread Checkeador.
	private boolean[] cambio = new boolean[] {false,false,false,false,false,false,false,false};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View ui = inflater.inflate(R.layout.relesfrag, container, false);
		act = (HomeActivity) getActivity();
		
		reles[0] = (ToggleButton) ui.findViewById(R.id.rele1);
		reles[1] = (ToggleButton) ui.findViewById(R.id.rele2);
		reles[2] = (ToggleButton) ui.findViewById(R.id.rele3);
		reles[3] = (ToggleButton) ui.findViewById(R.id.rele4);
		reles[4] = (ToggleButton) ui.findViewById(R.id.rele5);
		reles[5] = (ToggleButton) ui.findViewById(R.id.rele6);
		reles[6] = (ToggleButton) ui.findViewById(R.id.rele7);
		reles[7] = (ToggleButton) ui.findViewById(R.id.rele8);
		
		for (ToggleButton tb : reles) {
			tb.setOnCheckedChangeListener(event);
		}
		
		setHasOptionsMenu(true);
		
		return ui;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inf) {
		inf.inflate(R.menu.relesfrag, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_selTodos:
			for (int i = 0; i < CANTIDAD_RELES; i++) {
				if (reles[i].isChecked() == false){
					act.getDispositivo().toggleAllReles(true);
					break;
				}
			}
			return true;
		case R.id.menu_deselTodos:
			for (int i = 0; i < CANTIDAD_RELES; i++) {
				if (reles[i].isChecked() == true){
					act.getDispositivo().toggleAllReles(false);
					break;
				}
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

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
			cambio[Integer.valueOf((String) bv.getTag()) - 1] = true;
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
			
			for (int i = 0; i < CANTIDAD_RELES; i++) {
				if (!cambio[i]) reles[i].setChecked(est[i]);				
			}
			
			for (int i = 0; i < CANTIDAD_RELES; i++) {
				cambio[i] = false;
			}
		}
	};
	
	private void activarCheckeador() {
		if (act.getDispositivo().getState() == ETDevice.STATE_NADA)
			return;

		LocalBroadcastManager
				.getInstance(getActivity().getApplicationContext())
				.registerReceiver(mr,
						new IntentFilter(Checkeador.ACCION_CHECK_READY));

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
					Thread.sleep(500);
				} catch (InterruptedException e) {
					Log.e(Checkeador.class.getSimpleName(), e.toString());
					break;
				}
			}
		}

	}

}
