package com.sam_chordas.android.stockhawk.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by rodrigo.alencar on 5/11/16.
 */
public class AlertDialogFragment extends DialogFragment {

    private DialogInterface.OnClickListener mListener;

    public static AlertDialogFragment newInstance(String title, DialogInterface.OnClickListener onClickListener) {
        AlertDialogFragment frag = new AlertDialogFragment();
        frag.mListener = onClickListener;
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok, mListener)
                .setView(R.layout.input_dialog);
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()){
            getDialog().setDismissMessage(null);
        }

        super.onDestroyView();
    }
}