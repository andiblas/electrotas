package com.electrotas.electrotasbt.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.electrotas.electrotasbt.R;
import com.electrotas.electrotasbt.helpers.Tostada;

public class HomeActivity extends ActionBarActivity {

	private DrawerLayout drawer;
	private ListView drawerListL;
	private ListView drawerListR;

	// Local Bluetooth adapter
	private BluetoothAdapter btAdapter = null;
	private ArrayList<BluetoothDevice> jaja = null;
	private final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
	
	
	public OutputStream getOutStream() {
		return outStream;
	}

	public void setOutStream(OutputStream outStream) {
		this.outStream = outStream;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homeactivity);
		
		iniciarBluetooth();

		String[] opcionesMenu = new String[] { "Home", "Colores", "Reles" };
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerListL = (ListView) findViewById(R.id.left_drawer);
		drawerListR = (ListView) findViewById(R.id.right_drawer);

		Set<BluetoothDevice> caca = btAdapter.getBondedDevices();
		jaja = new ArrayList<BluetoothDevice>();

		for (BluetoothDevice a : caca) {
			jaja.add(a);
		}

		drawerListL.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				opcionesMenu));

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

				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
				drawerListL.setItemChecked(position, true);
				drawer.closeDrawer(drawerListL);
			}
		});
		drawerListR.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				BluetoothDevice sel = jaja.get(arg2);

				try {
					btSocket = createBluetoothSocket(sel);
				} catch (IOException e) {
					Toast.makeText(
							getApplicationContext(),
							"Error al querer crear la conexión jaja."
									+ e.getMessage(), Toast.LENGTH_LONG).show();
				}

				if (btSocket == null) {
					return;
				}

				try {
					btSocket.connect();

					try {
						outStream = btSocket.getOutputStream();
					} catch (IOException e) {
					}

				} catch (Exception e) {
					try {
						btSocket.close();
					} catch (IOException e1) {
						Toast.makeText(
								getApplicationContext(),
								"Error al querer cerrar la conexión."
										+ e.getMessage(), Toast.LENGTH_LONG)
								.show();
					}
				}

				if (outStream != null)
					Toast.makeText(getApplicationContext(),
							"Salio todo bien guacho!", Toast.LENGTH_LONG)
							.show();

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

	private BluetoothSocket createBluetoothSocket(BluetoothDevice device)
			throws IOException {
		if (Build.VERSION.SDK_INT >= 10) {
			try {
				return device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (Exception e) {
				Log.e("Error", "Could not create secure RFComm Connection", e);
			}
		}
		return null;
	}

}
