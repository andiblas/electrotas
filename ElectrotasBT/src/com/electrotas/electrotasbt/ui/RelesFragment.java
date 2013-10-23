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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View ui = inflater.inflate(R.layout.relesfrag, container, false);
		act = (HomeActivity) getActivity();

		rele1 = (ToggleButton) ui.findViewById(R.id.rele1);
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
	public void onResume() {
		super.onResume();
		Log.i(RelesFragment.class.getSimpleName(), "On Resume*******");

		LocalBroadcastManager
				.getInstance(getActivity().getApplicationContext())
				.registerReceiver(mr,
						new IntentFilter(Checkeador.ACCION_CHECK_READY));

		checker = new Checkeador(getActivity().getApplicationContext(),
				act.getDispositivo());
		checker.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(RelesFragment.class.getSimpleName(), "On Pause*******");
		
		LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(mr);

		if (checker != null) {
			checker.interrupt();
			checker = null;
		}
	}

	private BroadcastReceiver mr = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			rele1.setChecked(act.getDispositivo().getEstadoReles()[0]);
		}
	};

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
