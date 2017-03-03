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
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Project : CriminalIntent
 * Created by Hancheng Yang ( hancheng.yang@eptender.com )
 * Date : 03/03/2017, 16:13
 * Description :
 **/
public class TimePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    public static final String EXTRA_TIME = "com.yang.hancheng.criminalintent.time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date)getArguments().getSerializable(ARG_DATE);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        mTimePicker = (TimePicker)v.findViewById(R.id.dialog_time_time_picker);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int min, hour;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            min = mTimePicker.getMinute();
                            hour = mTimePicker.getHour();
                        } else {
                            min = mTimePicker.getCurrentMinute();
                            hour = mTimePicker.getCurrentHour();
                        }
                        Date date = new GregorianCalendar(year, month, day, hour, min).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date date){
        if(getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        if(date != null) {
            intent.putExtra(EXTRA_TIME, date);
        }
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
