/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.electrotas.electrotasbt.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.electrotas.electrotasbt.R;

/**
 * 
 * @author AndresP
 */
public class Tostada {

	public static final int MENSAJE_INFO = 0;
	public static final int MENSAJE_BUENO = 1;
	public static final int MENSAJE_MALO = 2;

	
	public static void mostrar(Context ctx, int resTitulo, int resDesc, int tipo) {
		View layout = LayoutInflater.from(ctx).inflate(R.layout.tostada, null);

		TextView txtTit = (TextView) layout.findViewById(R.id.tos_titulo);
		TextView txtDesc = (TextView) layout.findViewById(R.id.tos_desc);
		ImageView img = (ImageView) layout.findViewById(R.id.tos_img);
		
		switch (tipo) {
			case MENSAJE_BUENO:
				img.setImageResource(R.drawable.ok);
				break;
			case MENSAJE_MALO:
				img.setImageResource(R.drawable.alerta);
				break;
			default:
				// mostramos el logo ciat que esta por defecto
				break;
		}

		txtTit.setText(resTitulo);
		txtDesc.setText(resDesc);

		Toast toast = new Toast(ctx);
		// toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	public static void mostrar(Context ctx, int resTitulo, String desc, int tipo) {
		View layout = LayoutInflater.from(ctx).inflate(R.layout.tostada, null);

		TextView txtTit = (TextView) layout.findViewById(R.id.tos_titulo);
		TextView txtDesc = (TextView) layout.findViewById(R.id.tos_desc);
		ImageView img = (ImageView) layout.findViewById(R.id.tos_img);
		
		switch (tipo) {
			case MENSAJE_BUENO:
				img.setImageResource(R.drawable.ok);
				break;
			case MENSAJE_MALO:
				img.setImageResource(R.drawable.alerta);
				break;
			default:
				// mostramos el logo ciat que esta por defecto
				break;
		}

		txtTit.setText(resTitulo);

		if (desc != null && !(desc.trim().length() == 0)) {
			txtDesc.setText(desc);
		} else {
			txtDesc.setVisibility(View.GONE);
		}

		Toast toast = new Toast(ctx);
		// toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	public static void mostrar(Context ctx, String tit, String desc, int tipo) {
		View layout = LayoutInflater.from(ctx).inflate(R.layout.tostada, null);

		TextView txtTit = (TextView) layout.findViewById(R.id.tos_titulo);
		TextView txtDesc = (TextView) layout.findViewById(R.id.tos_desc);
		ImageView img = (ImageView) layout.findViewById(R.id.tos_img);
		
		switch (tipo) {
			case MENSAJE_BUENO:
				img.setImageResource(R.drawable.ok);
				break;
			case MENSAJE_MALO:
				img.setImageResource(R.drawable.alerta);
				break;
			default:
				// mostramos el logo ciat que esta por defecto
				break;
		}

		if (tit != null && !(tit.trim().length() == 0)) {
			txtTit.setText(tit);
		} else {
			txtTit.setVisibility(View.GONE);
		}

		if (desc != null && !(desc.trim().length() == 0)) {
			txtDesc.setText(desc);
		} else {
			txtDesc.setVisibility(View.GONE);
		}

		Toast toast = new Toast(ctx);
		// toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

}
