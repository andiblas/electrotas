package com.electrotas.electrotasbt.helpers;

import android.content.Context;

public class SessionManager {
	
	private Context mCtx;
	
	public SessionManager(Context c){
		mCtx = c;
	}
	
	/**
	 * Determina si es la primera vez que se abre la aplicación
	 * 
	 * @param ctx
	 *            Contexto para determinar la primera ejecución
	 * @return <strong>True: </strong>si es la primera vez que se ejecuta. <br>
	 *         <strong>False: </strong>y si ya te dije que si viene true, es la primera vez, si viene False, que va a ser?
	 */
	public static boolean isFirstTime(final Context ctx) {
		ConfHelper oConf = new ConfHelper(ctx);
        if (oConf.getBoolConf(ConfHelper.KEY_PRIMERAVEZ) == false) {
        	oConf.savePref(ConfHelper.KEY_PRIMERAVEZ, true);
            return true;
        } else {
            return false;
        }
	}
	
}
