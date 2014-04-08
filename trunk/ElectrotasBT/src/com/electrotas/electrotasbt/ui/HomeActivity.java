package com.electrotas.electrotasbt.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.electrotas.electrotasbt.R;
import com.electrotas.electrotasbt.core.ETDevice;
import com.electrotas.electrotasbt.core.data.Placa;
import com.electrotas.electrotasbt.helpers.Tostada;
import com.electrotas.electrotasbt.ui.adapters.BTDeviceAdapter;
import com.electrotas.electrotasbt.ui.adapters.MenuAdapter;
import com.electrotas.electrotasbt.ui.adapters.PlacasAdapter;

public class HomeActivity extends ActionBarActivity {

	// ui
	private DrawerLayout drawer;
	private ActionBarDrawerToggle toggle;
	private ListView drawerListL;
	private RelativeLayout drawerListR;
	private ListView listaFav;
	private ListView listaNuevos;

	// Local Bluetooth adapter
	private BluetoothAdapter btAdapter = null;

	// Adaptadores de listas
	private PlacasAdapter placasAdap;
	private ArrayAdapter<BluetoothDevice> btAdap;
	private final HashSet<BluetoothDevice> disposEncontrados = new HashSet<BluetoothDevice>();

	// Dispositivo
	private ETDevice dispositivo;
	
	// Otros
	private int fragActual = 0;
	
	public ETDevice getDispositivo() {
		return dispositivo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homeactivity);

		iniciarBluetooth();
		initActionBar();

		dispositivo = new ETDevice(getApplicationContext());
		drawerListL = (ListView) findViewById(R.id.lvMenu);
		drawerListR = (RelativeLayout) findViewById(R.id.right_drawer);
		listaFav = (ListView) findViewById(R.id.lv_favoritos);
		listaNuevos = (ListView) findViewById(R.id.lv_nuevos);

		drawerListL.setAdapter(new MenuAdapter(this));

		placasAdap = new PlacasAdapter(getApplicationContext(),
				R.layout.menulist, Placa.select(getApplicationContext()));
		listaFav.setAdapter(placasAdap);

		Set<BluetoothDevice> bonded = btAdapter.getBondedDevices();
		ArrayList<BluetoothDevice> lista = new ArrayList<BluetoothDevice>();
		for (BluetoothDevice a : bonded) {
			lista.add(a);
		}
		btAdap = new BTDeviceAdapter(getApplicationContext(),
				R.layout.menulist, lista);
		listaNuevos.setAdapter(btAdap);
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, new PresetsFragment()).commit();

		drawerListL.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Fragment fragment = null;
				switch (position) {
				case 0:
					fragment = new PresetsFragment();
					break;
				case 1:
					fragment = new ColoresFragment();
					break;
				case 2:
					fragment = new RelesFragment();
					break;
				}

				fragActual = position;
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.content_frame, fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
				drawerListL.setItemChecked(position, true);
				drawer.closeDrawer(drawerListL);
			}
		});
		listaFav.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					
					try {
						dispositivo.connect(btAdapter.getRemoteDevice(placasAdap.getItem(arg2).getMAC()));
					} catch (Exception e) {
						String[] msj = e.getMessage().split(";");
						Tostada.mostrar(getApplicationContext(), msj[0], msj[1],
								Tostada.MENSAJE_MALO);
					}
			}
		});
		listaNuevos.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					dispositivo.connect(btAdap.getItem(arg2));
				} catch (Exception e) {
					String[] msj = e.getMessage().split(";");
					Tostada.mostrar(getApplicationContext(), msj[0], msj[1],
							Tostada.MENSAJE_MALO);
				}
			}
		});
	}

	// @Override
	// protected void onStop() {
	// btAdapter.disable();
	// super.onStop();
	// }

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver,new IntentFilter(BluetoothDevice.ACTION_FOUND));
		
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mRDispoGuardado,
				new IntentFilter(ETDevice.ACTION_DISPO_GUARDADO));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mRDispoGuardado);
	}

	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			// When discovery finds a device
			if (!BluetoothDevice.ACTION_FOUND.equals(intent.getAction()))
				return;

			// Get the BluetoothDevice object from the Intent
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			
			if (disposEncontrados.contains(device)) return;
			
			disposEncontrados.add(device);
			btAdap.add(device);
			btAdap.notifyDataSetChanged();
		}
	};

	// Create a BroadcastReceiver for DISPO_GUARDADO
	private final BroadcastReceiver mRDispoGuardado = new BroadcastReceiver() {
		public void onReceive(Context context, Intent inte) {
			// Cuando se guardo una placa
			if (!ETDevice.ACTION_DISPO_GUARDADO.equals(inte.getAction()))
				return;
			
			Placa nueva = new Placa();
			nueva.setId(inte.getIntExtra(ETDevice.KEY_NUEVAPLACA_ID, -1));
			nueva.setNombre(inte.getStringExtra(ETDevice.KEY_NUEVAPLACA_NOMBRE));
			nueva.setMAC(inte.getStringExtra(ETDevice.KEY_NUEVAPLACA_MAC));
			
			placasAdap.add(nueva);
			placasAdap.notifyDataSetChanged();
		}
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (toggle.onOptionsItemSelected(item)) {
			if (drawer.isDrawerOpen(drawerListR)){
				drawer.closeDrawer(drawerListR);
			};
			return true;
		}

		switch (item.getItemId()) {

		case R.id.menu_acc1:
			TextView tv = (TextView) findViewById(R.id.navsec_nuevos);
			if (btAdapter.isDiscovering()){
				btAdapter.cancelDiscovery();
				tv.setText(R.string.tv_descubriendo_encontrados);
			}else{
				btAdapter.startDiscovery();
				tv.setText(R.string.tv_descubriendo);
			}
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		toggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		toggle.onConfigurationChanged(newConfig);
	}

	// funciones
	private void iniciarBluetooth() {

		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
			Tostada.mostrar(getApplicationContext(), R.string.msjNoBluetooth,
					R.string.msjNoBluetoothDesc, Tostada.MENSAJE_MALO);
			finish();
			return;
		}

		if (!btAdapter.isEnabled()) {
			btAdapter.enable();
		}

	}

	private void initActionBar() {
		
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		toggle = new ActionBarDrawerToggle(this, /* host Activity */
		drawer, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.openDrawer, /* "open drawer" description */
		R.string.closeDrawer /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				switch (fragActual) {
				case 0:
					getSupportActionBar().setTitle(R.string.fragname_Preset);
					break;
				case 1:
					getSupportActionBar().setTitle(R.string.fragname_Colores);
					break;
				case 2:
					getSupportActionBar().setTitle(R.string.fragname_Reles);
					break;
				}
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(R.string.app_name);
			}
			
		};
		
		// Set the drawer toggle as the DrawerListener
		drawer.setDrawerListener(toggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

}
