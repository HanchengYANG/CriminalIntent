package com.yang.hancheng.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Project : CriminalIntent
 * Created by Hancheng Yang ( hancheng.yang@eptender.com )
 * Date : 14/02/2017, 15:10
 * Description :
 **/

public class CrimeFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final String DIALOG_PHOTO = "DialogPhoto";

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mSuspectButton;
    private Button mReportButton;
    private Button mCallSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private CheckBox mSolvedCheckBox;

    private File mPhotoFile;

    private Intent pickContact;
    private Intent captureImage;

    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        setHasOptionsMenu(true);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mDateButton = (Button)v.findViewById(R.id.crime_date);
        mTimeButton = (Button)v.findViewById(R.id.crime_time);
        mReportButton = (Button)v.findViewById(R.id.crime_report);
        mSuspectButton = (Button)v.findViewById(R.id.crime_suspect);
        mCallSuspectButton = (Button)v.findViewById(R.id.crime_call_suspect);
        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_camera);
        mDateButton.setOnClickListener(this);
        mTimeButton.setOnClickListener(this);
        mReportButton.setOnClickListener(this);
        mSuspectButton.setOnClickListener(this);
        mCallSuspectButton.setOnClickListener(this);
        if(mCrime.getSuspect() != null){
            mSuspectButton.setText(mCrime.getSuspect());
            mCallSuspectButton.setEnabled(true);
        } else {
            mCallSuspectButton.setEnabled(false);
        }
        PackageManager packageManager = getActivity().getPackageManager();
        pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        if(packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }
        mPhotoView = (ImageView)v.findViewById(R.id.crime_photo);
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });
        captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = (mPhotoFile != null && captureImage.resolveActivity(packageManager) != null );
        mPhotoButton.setEnabled(canTakePhoto);
        if(canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mPhotoButton.setOnClickListener(this);
        updateDate();
        updatePhotoView();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        Date newDate;
        switch (requestCode){
            case REQUEST_DATE:
                newDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mCrime.setDate(newDate);
                updateDate();
                break;
            case REQUEST_TIME:
                newDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_TIME);
                mCrime.setDate(newDate);
                updateDate();
                break;
            case REQUEST_CONTACT:
                Uri contactUri = data.getData();
                String[] queryFields = new String[] {
                        ContactsContract.Contacts.DISPLAY_NAME
                };
                Cursor cursor = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
                try {
                    if(cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        String suspect = cursor.getString(0);
                        mCrime.setSuspect(suspect);
                        mSuspectButton.setText(suspect);
                        mCallSuspectButton.setEnabled(true);
                    }
                } finally {
                    cursor.close();
                }
                break;
            case REQUEST_PHOTO:
                updatePhotoView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
        switch (v.getId()){
            case R.id.crime_date:
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
                break;
            case R.id.crime_time:
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
                break;
            case R.id.crime_report:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);
                break;
            case R.id.crime_suspect:
                startActivityForResult(pickContact, REQUEST_CONTACT);
                break;
            case R.id.crime_call_suspect:
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String[] projection = new String[]{
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
                String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "= ?";
                String[] selectionArgs = new String[] {
                        mSuspectButton.getText().toString()
                };
                Cursor c = getActivity().getContentResolver().query(uri, projection, selection, selectionArgs, null);
                try {
                    if(c.getCount() != 0) {
                        c.moveToFirst();
                        String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Uri numberUri = Uri.parse("tel:" +  number);
                        Intent dial = new Intent(Intent.ACTION_DIAL, numberUri);
                        startActivity(dial);
                    }
                } finally {
                    c.close();
                }
                break;
            case R.id.crime_camera:
                startActivityForResult(captureImage, REQUEST_PHOTO);
                break;
            case R.id.crime_photo:
                CrimePhotoViewFragment photoViewDialog = CrimePhotoViewFragment.newInstance(mCrime.getId());
                photoViewDialog.show(manager, DIALOG_PHOTO);
                break;
            default:
                break;
        }
    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy", mCrime.getDate()));
        mTimeButton.setText(DateFormat.format("h:mm a", mCrime.getDate()));
    }

    private void updatePhotoView() {
        if(mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
            mPhotoView.setOnClickListener(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
            mPhotoView.setOnClickListener(this);
        }
    }

    private String getCrimeReport() {
        String solvedString = null;
        if(mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if(suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }
}
