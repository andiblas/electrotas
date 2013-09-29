package com.electrotas.electrotasbt.core;

import java.io.IOException;
import java.io.OutputStream;
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

	// Constantes
	public static final int STATE_NADA = 0; // Haciendo nada
	public static final int STATE_CONECTANDO = 1; // Intentando Conectar
	public static final int STATE_CONECTADO = 2; // Actualmente conectado

	public ETDevice(Context ct) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();

	}

	private synchronized void setState(int state) {
		estActual = state;
	}

	public synchronized int getState() {
		return estActual;
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
	
    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    public synchronized void connect(BluetoothDevice device) {

        // Cancel any thread attempting to make a connection
        if (estActual == STATE_CONECTANDO) {
            if (thConnect != null) {thConnect.cancel(); thConnect = null;}
        }

        // Cancel any thread currently running a connection
        if (thConectado != null) {thConectado.cancel(); thConectado = null;}

        // Start the thread to connect with the given device
        thConnect = new ConnectThread(device);
        thConnect.start();
        setState(STATE_CONECTANDO);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {

        // Cancel the thread that completed the connection
        if (thConnect != null) {thConnect.cancel(); thConnect = null;}

        // Cancel any thread currently running a connection
        if (thConectado != null) {thConectado.cancel(); thConectado = null;}

        // Start the thread to manage the connection and perform transmissions
        thConectado = new ConnectedThread(socket);
        
        setState(STATE_CONECTADO);
    }
	
    /**
     * Stop all threads
     */
    public synchronized void stop() {
        // Cancel the thread that completed the connection
        if (thConnect != null) {thConnect.cancel(); thConnect = null;}

        // Cancel any thread currently running a connection
        if (thConectado != null) {thConectado.cancel(); thConectado = null;}
        setState(STATE_NADA);
    }
    
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (estActual != STATE_CONECTADO) return;
            r = thConectado;
            r.setBuffer(out);
        }
        // Perform the write unsynchronized
        r.start();
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

	/**
	 * Thread para enviar datos.
	 */
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private byte[] buffer;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			OutputStream tmpOut = null;

			// Get the BluetoothSocket input and output streams
			try {
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}

			mmOutStream = tmpOut;
		}
		
		public void setBuffer(byte[] buffer) {
			this.buffer = buffer;
		}
		
		public void run() {
			if (buffer == null) return;
			try {
				mmOutStream.write(buffer);
			} catch (IOException e) {
				Log.e(TAG, "disconnected", e);
				connectionFailed();
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() fallo", e);
			}
		}
	}

}
