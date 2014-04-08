package com.electrotas.electrotasbt.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import com.electrotas.electrotasbt.R;

public class SavePresetDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		builder.setView(inflater.inflate(R.layout.savepresetdialog, null));
		builder.setTitle(R.string.dia_tit_GuardarPreset)
				.setPositiveButton(R.string.dia_guardar,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
							}
						}).setNegativeButton(R.string.dia_cancelar, null);
		// Create the AlertDialog object and return it
		return builder.create();
	}

}
