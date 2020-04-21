package com.example.butymovamoneytracker.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.butymovamoneytracker.R;
import com.example.butymovamoneytracker.data.extra.RemoveData;

import java.util.Objects;

public class RemoveDialog extends DialogFragment {

    public static final String REMOVE_DIALOG_TAG = "remove_dialog_tag";
    public static final int RC_REMOVE_DIALOG = 102;
    private static final String KEY_REMOVE_DATA = "remove_data";
    private RemoveData data;

    public void setRemoveData(RemoveData data) {
        this.data = data;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState!=null)
            data = savedInstanceState.getParcelable(KEY_REMOVE_DATA);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getString(R.string.confirmation_message_remove, getResources().getQuantityString(data.getResId(), data.getCount(), data.getCount())))
                .setPositiveButton(R.string.confirmation_positive_result, (dialog, which) ->
                        Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent()))
                .setNegativeButton(R.string.confirmation_negative_result, null);

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_REMOVE_DATA, data);
    }
}
