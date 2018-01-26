package com.sa45team7.lussis.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sa45team7.lussis.R;

import static android.app.Activity.RESULT_OK;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by nhatton on 1/24/18.
 */

public class AdjustDialog extends DialogFragment {

    public static final int REQUEST_ADJUST = 8;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_adjustment, container);

        final EditText numberText = view.findViewById(R.id.number_text);

        ImageButton addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(numberText.getText().toString());
                numberText.setText(String.valueOf(number + 1));
            }
        });

        ImageButton removeButton = view.findViewById(R.id.remove_button);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(numberText.getText().toString());
                numberText.setText(String.valueOf(number - 1));
            }
        });

        final EditText reasonText = view.findViewById(R.id.reason_text);

        Button okButton = view.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(numberText.getText().toString());
                getActivity().getIntent().putExtra("quantity", number);
                getActivity().getIntent().putExtra("reason", reasonText.getText().toString());

                if (getTargetFragment() != null) {
                    getTargetFragment().onActivityResult(REQUEST_ADJUST, RESULT_OK, getActivity().getIntent());
                } else {
                    onActivityResult(REQUEST_ADJUST, RESULT_OK, getActivity().getIntent());
                }

                dismiss();
            }
        });

        Button cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        }
    }

}