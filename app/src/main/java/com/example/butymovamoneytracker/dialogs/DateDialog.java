package com.example.butymovamoneytracker.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DateDialog extends DialogFragment {
    public static final String DATE_MIN_DIALOG_TAG = "date_min_dialog_tag";
    public static final String DATE_MAX_DIALOG_TAG = "date_max_dialog_tag";

    private static final String YEAR_ARG = "year";
    private static final String MONTH_ARG = "month";
    private static final String DAY_ARG = "day";
    private DatePickerDialog.OnDateSetListener listener;

    public static DateDialog newInstance(int year, int month, int dayOfMonth) {
        Bundle bundle = new Bundle();
        bundle.putInt(YEAR_ARG, year);
        bundle.putInt(MONTH_ARG, month);
        bundle.putInt(DAY_ARG, dayOfMonth);

        DateDialog dialog = new DateDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DatePickerDialog dialog = new DatePickerDialog(requireContext(),
                listener,
                getArguments().getInt(YEAR_ARG),
                getArguments().getInt(MONTH_ARG),
                getArguments().getInt(DAY_ARG)
        );
        dialog.getDatePicker().setTag(getTag());
        return dialog;
    }

    @Override
    public void onDestroyView() {
        listener=null;
        super.onDestroyView();
    }
}