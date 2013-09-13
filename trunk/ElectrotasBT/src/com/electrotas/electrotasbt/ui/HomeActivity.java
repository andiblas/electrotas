package com.electrotas.electrotasbt.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.electrotas.electrotasbt.R;

public class HomeActivity extends ActionBarActivity {

	private String[] opcionesMenu;
	private DrawerLayout drawerL;
	private ListView drawerListL;
	private ListView drawerListR;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homeactivity);
		
		opcionesMenu = new String[] { "Home", "Colores", "Reles" };
		String[] optionesDisp = new String[] {"Dispositivo 1", "Dispositivo 2"};
		drawerL = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerListL = (ListView) findViewById(R.id.left_drawer);
		drawerListR = (ListView) findViewById(R.id.right_drawer);
		
		drawerListL.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				opcionesMenu));
		
		drawerListR.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				optionesDisp));
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, new HomeFragment()).commit();
		
		drawerListL.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View view,
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
				drawerL.closeDrawer(drawerListL);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_acc1:
			Toast.makeText(getApplicationContext(), "Accion", Toast.LENGTH_LONG)
					.show();
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
