package com.electrotas.electrotasbt.ui;

import java.util.ArrayList;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.electrotas.electrotasbt.R;
import com.electrotas.electrotasbt.core.ETDevice;
import com.electrotas.electrotasbt.helpers.Tostada;
import com.electrotas.electrotasbt.ui.adapters.MenuAdapter;

public class HomeActivity extends ActionBarActivity {

	private DrawerLayout drawer;
	private ListView drawerListL;
	private ListView drawerListR;

	// Local Bluetooth adapter
	private BluetoothAdapter btAdapter = null;
	private ArrayList<BluetoothDevice> jaja = null;

	private ETDevice dispositivo;

	public ETDevice getDispositivo() {
		return dispositivo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homeactivity);
		
		iniciarBluetooth();
		
		dispositivo = new ETDevice(getApplicationContext());
		
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerListL = (ListView) findViewById(R.id.left_drawer);
		drawerListR = (ListView) findViewById(R.id.right_drawer);
		
		Set<BluetoothDevice> caca = btAdapter.getBondedDevices();
		jaja = new ArrayList<BluetoothDevice>();
		
		for (BluetoothDevice a : caca) {
			jaja.add(a);
		}
		
		drawerListL.setAdapter(new MenuAdapter(this));

		drawerListR.setAdapter(new ArrayAdapter<BluetoothDevice>(
				getSupportActionBar().getThemedContext(),
				android.R.layout.simple_list_item_1, jaja));

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, new HomeFragment()).commit();

		drawerListL.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Fragment fragment = null;
				switch (position) {
				case 0:
					fragment = new HomeFragment();
					break;
				case 1:
					fragment = new ColoresFragment();
					break;
				case 2:
					fragment = new RelesFragment();
					break;
				}
				
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.content_frame, fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
				drawerListL.setItemChecked(position, true);
				drawer.closeDrawer(drawerListL);
			}
		});
		drawerListR.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				dispositivo.connect(jaja.get(arg2));
			}
		});
	}

//	@Override
//	protected void onStop() {
//		btAdapter.disable();
//		super.onStop();
//	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_acc1:
			Tostada.mostrar(getApplicationContext(), "Test", "Test test test test", Tostada.MENSAJE_MALO);
			break;
		case R.id.menu_conf:

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

	// funciones
	public void iniciarBluetooth() {

		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
			Tostada.mostrar(getApplicationContext(), R.string.msjNoBluetooth, R.string.msjNoBluetoothDesc, Tostada.MENSAJE_MALO);
			finish();
			return;
		}

		if (!btAdapter.isEnabled()) {
			btAdapter.enable();
		}

	}
	
}
