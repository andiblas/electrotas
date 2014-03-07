package com.electrotas.electrotasbt.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 
 * @author AndresP
 */
public class ConfHelper {

	private Context mCtx;
	
	//Poner aca como constantes todas las Key del xml de preferencias de la app
	//y tambien las otras key que usemos dentro del codigo.
	public static final String KEY_PRIMERAVEZ = "primeravez";
	public static final String KEY_ANIMACIONES = "animaciones";

	public ConfHelper(Context c) {
		mCtx = c;
	}

    //GRABAR en PREFERENCIAS
    public void savePref(String clave, String valor) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(mCtx).edit();
        e.putString(clave, valor);
        e.commit();
    }

    public void savePref(String clave, boolean valor) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(mCtx).edit();
        e.putBoolean(clave, valor);
        e.commit();
    }

    public void savePref(String clave, int valor) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(mCtx).edit();
        e.putInt(clave, valor);
        e.commit();
    }
	
	/**
	 * Buscar preferencias de la Aplicacion (preferencias.xml)
	 * 
	 * @param nombre
	 *            Key de la preferencia a devolver
	 * @return Valor de la preferencia
	 */
	public boolean getBoolConf(final String nombre) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
		return prefs.getBoolean(nombre, false);
	}

	/**
	 * Buscar preferencias de la Aplicacion (preferencias.xml)
	 * 
	 * @param nombre
	 *            Key de la preferencia a devolver
	 * @return Valor de la preferencia
	 */
	public String getStringConf(final String nombre) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
		return prefs.getString(nombre, "");
	}

	/**
	 * Buscar preferencias de la Aplicacion (preferencias.xml)
	 * 
	 * @param nombre
	 *            Key de la preferencia a devolver
	 * @return Valor de la preferencia
	 */
	public int getIntConf(final String nombre) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
		return prefs.getInt(nombre, 0);
	}

	
	
}
