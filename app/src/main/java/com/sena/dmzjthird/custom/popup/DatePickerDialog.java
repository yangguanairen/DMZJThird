package com.sena.dmzjthird.custom.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.sena.dmzjthird.R;

/**
 * FileName: DatePickerDialog
 * Author: JiaoCan
 * Date: 2022/1/30 9:42
 */

public class DatePickerDialog extends CenterPopupView {

    private TextView mTextView;

    public DatePickerDialog(@NonNull Context context, TextView textView) {
        super(context);
        mTextView = textView;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_date_picker_dialog;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate() {
        super.onCreate();


        DatePicker datePicker = findViewById(R.id.datePicker);
        TextView tvConfirm = findViewById(R.id.confirm);


        tvConfirm.setOnClickListener(v -> {

            int year = datePicker.getYear();
            int month = datePicker.getMonth() + 1;
            int day = datePicker.getDayOfMonth();

            mTextView.setText(year + "/" + month + "/" + day);

            dismiss();
        });
    }


}
