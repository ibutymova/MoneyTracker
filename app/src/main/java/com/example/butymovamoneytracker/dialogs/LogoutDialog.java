package com.example.butymovamoneytracker.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.butymovamoneytracker.R;
import com.example.butymovamoneytracker.screens.main.MainActivityViewModel;

public class LogoutDialog extends DialogFragment {

    public static final String LOGOUT_DIALOG_TAG = "logout_dialog_tag";

    private MainActivityViewModel viewModel;

    public void setViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(R.string.confirmation_message_logout)
                .setPositiveButton(R.string.confirmation_positive_result, (dialog, which) -> viewModel.logOutConfirmationYes())
                .setNegativeButton(R.string.confirmation_negative_result, null);

        return builder.create();
    }

    @Override
    public void onDestroy() {
        viewModel=null;
        super.onDestroy();
    }
}
