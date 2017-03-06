package com.yang.hancheng.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;

/**
 * Project : CriminalIntent
 * Created by Hancheng Yang ( hancheng.yang@eptender.com )
 * Date : 2017/3/2, 10:34
 * Description :
 **/

public class DatePickerFragment extends DialogFragment {

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "com.yang.hancheng.criminalintent.date";
    public static final String EXTRA_TIME = "com.yang.hancheng.criminalintent.time";
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog outputDialog = null;
        View v;
        Date date = (Date)getArguments().getSerializable(ARG_DATE);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int min = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        switch (getTargetRequestCode()){
            case REQUEST_DATE:
                v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
                mDatePicker = (DatePicker)v.findViewById(R.id.dialog_date_date_picker);
                mDatePicker.init(year, month, day, null);
                outputDialog = new AlertDialog.Builder(getActivity())
                        .setView(v)
                        .setTitle(R.string.date_picker_title)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                Date date = calendar.getTime();
                                sendResult(Activity.RESULT_OK, EXTRA_DATE, date);
                            }
                        })
                        .create();
                break;
            case REQUEST_TIME:
                v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
                mTimePicker = (TimePicker)v.findViewById(R.id.dialog_time_time_picker);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mTimePicker.setHour(hour);
                    mTimePicker.setMinute(min);
                } else {
                    mTimePicker.setCurrentHour(hour);
                    mTimePicker.setCurrentMinute(min);
                }
                outputDialog = new AlertDialog.Builder(getActivity())
                        .setView(v)
                        .setTitle(R.string.time_picker_title)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int hour, min;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    hour = mTimePicker.getHour();
                                    min = mTimePicker.getMinute();
                                } else {
                                    hour = mTimePicker.getCurrentHour();
                                    min = mTimePicker.getCurrentMinute();
                                }
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, min);
                                Date date = calendar.getTime();
                                sendResult(Activity.RESULT_OK, EXTRA_TIME, date);
                            }
                        })
                        .create();
                break;
        }
        assert outputDialog != null;
        return outputDialog;
    }

    private void sendResult(int resultCode, String extra, Date date){
        if(getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(extra, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
