package com.electrotas.electrotasbt.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class Envio implements Acciones {
	private final BluetoothSocket mmSocket;
	private final OutputStream mmOutStream;
	private final InputStream mmInStream;

	private int newColor = -1;
	private int nroRele = -1;
	private boolean checkeado = true;

	public Envio(BluetoothSocket btS) {
		mmSocket = btS;
		OutputStream tmpOut = null;
		InputStream tmpIn = null;
		// Get the BluetoothSocket input and output streams
		try {
			tmpOut = btS.getOutputStream();
			tmpIn = btS.getInputStream();
		} catch (IOException e) {
			Log.e("Envio", "temp sockets not created", e);
		}

		mmInStream = tmpIn;
		mmOutStream = tmpOut;
	}

	public void setNewColor(int newColor) {
		this.newColor = newColor;
	}

	public void setNroRele(int nroRele) {
		this.nroRele = nroRele;
	}

	public void setCheckeado(boolean checkeado) {
		this.checkeado = checkeado;
	}

	@Override
	public void cambiarColor() {
		if (newColor == -1)
			return;
		byte[] buf0 = new byte[1];
		byte[] buf1 = new byte[1];

		try {
			synchronized (Envio.class) {
				buf0[0] = (byte) 001;
				buf1[0] = (byte) ((newColor >> 16) & 0xff);
				mmOutStream.write(buf0);
				mmOutStream.write(buf1);
				mmOutStream.flush();

				buf0[0] = (byte) 002;
				buf1[0] = (byte) ((newColor >> 8) & 0xff);
				mmOutStream.write(buf0);
				mmOutStream.write(buf1);
				mmOutStream.flush();

				buf0[0] = (byte) 003;
				buf1[0] = (byte) (newColor & 0xff);
				mmOutStream.write(buf0);
				mmOutStream.write(buf1);
				mmOutStream.flush();
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void toggleRele() {
		if (nroRele == -1)
			return;
		byte[] buf0 = new byte[1];
		byte[] buf1 = new byte[1];
		if (checkeado) {
			buf0[0] = (byte) 255;
			buf1[0] = (byte) ((nroRele * 2) - 1);
		} else {
			buf0[0] = (byte) 255;
			buf1[0] = (byte) (nroRele * 2);
		}

		try {
			synchronized (Envio.class) {
				mmOutStream.write(buf0);
				mmOutStream.write(buf1);
				mmOutStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void toggleAllReles() {
		byte[] buf0 = new byte[1];
		byte[] buf1 = new byte[1];
		for (int rele = 1; rele < 9; rele++) {
			if (checkeado) {
				buf0[0] = (byte) 255;
				buf1[0] = (byte) ((rele * 2) - 1);
			} else {
				buf0[0] = (byte) 255;
				buf1[0] = (byte) (rele * 2);
			}

			try {
				synchronized (Envio.class) {
					mmOutStream.write(buf0);
					mmOutStream.write(buf1);
					mmOutStream.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * Checkea y devuelve un array de boolean con el estado
	 * de cada uno de los reles de como se encuentra en la
	 * placa a la hora de ejecutar el metodo
	 * @author Andres
	 */
	@Override
	public boolean[] checkState() {

		boolean[] resul = new boolean[8];
		// se manda un 5 para avisar que
		// se va a consultar el estado
		byte[] buf0 = new byte[1];
		buf0[0] = (byte) 005;

		try {
			synchronized (Envio.class) {
				mmOutStream.write(buf0);
				mmOutStream.flush();

				buf0 = new byte[1];
				for (int i = 0; i < 8; i++) {
					mmInStream.read(buf0);
					resul[i] = buf0[0] == 1 ? true : false;
				}

			}
			return resul;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
