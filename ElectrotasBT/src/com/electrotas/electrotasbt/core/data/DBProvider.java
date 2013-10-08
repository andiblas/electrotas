/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.electrotas.electrotasbt.core.data;

import android.content.Context;

/**
 *
 * @author
 * AndresP
 */
public class DBProvider {

    private static DBHelper conex;

    public static synchronized DBHelper obtenerConex(Context ctx) {

        if (conex == null) {
            conex = new DBHelper(ctx);
        }

        return conex;

    }

    public static synchronized void cerrarConex() {

        if (conex != null) {
            conex.close();
        }

    }
}
