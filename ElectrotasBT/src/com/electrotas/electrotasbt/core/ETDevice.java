package com.electrotas.electrotasbt.core;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

public class ETDevice {

	// Debug
	private static final String TAG = "ETDevice";

	// Unique UUID for this application
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// Threads
	private ConnectThread thConnect;
	private ConnectedThread thConectado;

	// Attr
	private final BluetoothAdapter mAdapter;
	private int estActual = 0;
	private BluetoothSocket btSocket;
	private boolean[] estadoReles;

	// Constantes
	public static final int STATE_NADA = 0; // Haciendo nada
	public static final int STATE_CONECTANDO = 1; // Intentando Conectar
	public static final int STATE_CONECTADO = 2; // Actualmente conectado

	public ETDevice() {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	private synchronized void setState(int state) {
		estActual = state;
	}

	public synchronized int getState() {
		return estActual;
	}

	public synchronized void setEstadoReles(boolean[] c) {
		estadoReles = c;
	}

	public synchronized boolean[] getEstadoReles() {
		return estadoReles;
	}

	/**
	 * Start the chat service. Specifically start AcceptThread to begin a
	 * session in listening (server) mode. Called by the Activity onResume()
	 */
	public synchronized void start() {

		// Cancel any thread attempting to make a connection
		if (thConnect != null) {
			thConnect.cancel();
			thConnect = null;
		}

		// Cancel any thread currently running a connection
		if (thConectado != null) {
			thConectado.cancel();
			thConectado = null;
		}

		setState(STATE_NADA);

	}

	public synchronized void connect(BluetoothDevice device) {

		// Cancel any thread attempting to make a connection
		if (estActual == STATE_CONECTANDO) {
			if (thConnect != null) {
				thConnect.cancel();
				thConnect = null;
			}
		}

		// Cancel any thread currently running a connection
		if (thConectado != null) {
			thConectado.cancel();
			thConectado = null;
		}

		// Start the thread to connect with the given device
		thConnect = new ConnectThread(device);
		thConnect.start();
		setState(STATE_CONECTANDO);
	}

	public synchronized void connected(BluetoothSocket socket,
			BluetoothDevice device) {

		// Cancel the thread that completed the connection
		if (thConnect != null) {
			thConnect.cancel();
			thConnect = null;
		}

		// Cancel any thread currently running a connection
		if (thConectado != null) {
			thConectado.cancel();
			thConectado = null;
		}

		btSocket = socket;

		setState(STATE_CONECTADO);
	}

	public synchronized void stop() {
		// Cancel the thread that completed the connection
		if (thConnect != null) {
			thConnect.cancel();
			thConnect = null;
		}

		// Cancel any thread currently running a connection
		if (thConectado != null) {
			thConectado.cancel();
			thConectado = null;
		}
		setState(STATE_NADA);
	}

	private void connectionFailed() {
		// Start the service over to restart listening mode
		ETDevice.this.start();
	}

	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		private String mSocketType;

		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;

			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
				Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
			}
			mmSocket = tmp;
		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
			setName("ConnectThread" + mSocketType);

			// Always cancel discovery because it will slow down a connection
			mAdapter.cancelDiscovery();

			// Make a connection to the BluetoothSocket
			try {
				mmSocket.connect();
			} catch (IOException e) {
				// Close the socket
				try {
					mmSocket.close();
				} catch (IOException e2) {
					Log.e(TAG, "unable to close() " + mSocketType
							+ " socket during connection failure", e2);
				}
				connectionFailed();
				return;
			}

			// Reset the ConnectThread because we're done
			synchronized (ETDevice.this) {
				thConnect = null;
			}

			// Start the connected thread
			connected(mmSocket, mmDevice);
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect " + mSocketType
						+ " socket failed", e);
			}
		}
	}

	public void cambiarColor(int newC) {
		if (estActual == STATE_CONECTADO) return;
		Envio acc = new Envio(btSocket);
		acc.setNewColor(newC);
		new ConnectedThread(acc, ConnectedThread.ACCION_CAMBIARCOLOR).start();
	}

	public void toggleRele(int rele, boolean chk) {
		if (estActual == STATE_CONECTADO) return;
		Envio acc = new Envio(btSocket);
		acc.setNroRele(rele);
		acc.setCheckeado(chk);
		new ConnectedThread(acc, ConnectedThread.ACCION_CAMBIARRELE).start();
	}

	public void actualizarEstado() {
		if (estActual != STATE_CONECTADO) return;
		Envio acc = new Envio(btSocket);
		boolean[] novo = acc.checkState();
		if (novo != null)
			setEstadoReles(novo);
//		ConnectedThread th = new ConnectedThread(acc, ConnectedThread.ACCION_ACTUALIZARESTADO);
//		th.start();
//		try {
//			th.join();
//		} catch (InterruptedException e) {
//		}
	}

	/**
	 * Thread para hacer lo que tenga que hacer.
	 */
	private class ConnectedThread extends Thread {

		public int accion = 0;
		public Acciones caca;

		public static final int ACCION_CAMBIARCOLOR = 0;
		public static final int ACCION_CAMBIARRELE = 1;
		public static final int ACCION_ACTUALIZARESTADO = 2;

		public ConnectedThread(Acciones a, int queAccion) {
			caca = a;
			accion = queAccion;
		}

		public void run() {
			switch (accion) {
			case ACCION_CAMBIARCOLOR:
				caca.cambiarColor();
				break;
			case ACCION_CAMBIARRELE:
				caca.toggleRele();
			case ACCION_ACTUALIZARESTADO:
				boolean[] novo = caca.checkState();
				if (novo != null)
					setEstadoReles(novo);
				break;
			}
		}

		public void cancel() {
			caca.cancelar();
		}
	}

}
