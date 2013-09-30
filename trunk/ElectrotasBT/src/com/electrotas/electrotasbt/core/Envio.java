package com.electrotas.electrotasbt.core;

import java.io.IOException;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class Envio implements Acciones {
	private final BluetoothSocket mmSocket;
	private final OutputStream mmOutStream;
	
	private int newColor;
	private int nroRele;
	
	public Envio(BluetoothSocket btS){
		mmSocket = btS;
		OutputStream tmpOut = null;
		// Get the BluetoothSocket input and output streams
		try {
			tmpOut = btS.getOutputStream();
		} catch (IOException e) {
			Log.e("Envio", "temp sockets not created", e);
		}

		mmOutStream = tmpOut;
	}
	
	public int getNewColor() {
		return newColor;
	}

	public void setNewColor(int newColor) {
		this.newColor = newColor;
	}

	public int getNroRele() {
		return nroRele;
	}

	public void setNroRele(int nroRele) {
		this.nroRele = nroRele;
	}

	@Override
	public void cambiarColor() {


	}

	@Override
	public void toggleRele() {


	}

	@Override
	public void cancelar() {
		try {
			mmSocket.close();
		} catch (IOException e) {
			Log.e("Envio", "close() fallo", e);
		}
	}

}
