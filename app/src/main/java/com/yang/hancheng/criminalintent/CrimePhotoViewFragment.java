package com.yang.hancheng.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.UUID;

/**
 * Project : CriminalIntent
 * Created by Hancheng Yang ( hancheng.yang@eptender.com )
 * Date : 10/03/2017, 11:45
 * Description :
 **/
public class CrimePhotoViewFragment extends DialogFragment {

    private static final String ARG_CRIME_ID = "id";

    private ImageView mPhotoView;

    public static CrimePhotoViewFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimePhotoViewFragment fragment = new CrimePhotoViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        Crime crime = CrimeLab.get(getActivity()).getCrime(crimeId);
        File photo = CrimeLab.get(getActivity()).getPhotoFile(crime);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_photo_view, null);
        mPhotoView = (ImageView)view.findViewById(R.id.dialog_photo_view);
        Bitmap photoBitmap = PictureUtils.getScaledBitmap(photo.getPath(), getActivity());
        mPhotoView.setImageBitmap(photoBitmap);
        Dialog dialog = new AlertDialog.Builder(getActivity()).setView(view).create();
        return dialog;
    }
}
