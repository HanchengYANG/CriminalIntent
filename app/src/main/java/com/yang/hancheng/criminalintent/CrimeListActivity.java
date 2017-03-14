package com.yang.hancheng.criminalintent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


/**
 * Project : CriminalIntent
 * Created by Hancheng Yang ( hancheng.yang@eptender.com )
 * Date : 21/02/2017, 15:24
 * Description :
 **/


public class CrimeListActivity extends SingleFragmentActivity {
    private static final String KEY_PERMISSION = "permission";
    private static final int REQ_PERMISSION = 1;
    private static boolean reqGranted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            reqGranted = savedInstanceState.getBoolean(KEY_PERMISSION);
        }
        if(!reqGranted) {
            requestPermission();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_PERMISSION, reqGranted);
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    private boolean requestPermission() {
        int permissionCheckReadContacts = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);
        int permissionCheckReadExStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheckWriteExStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> permission = new ArrayList<>();
        if(permissionCheckReadContacts != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.READ_CONTACTS);
        }
        if(permissionCheckReadExStorage != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if(permissionCheckWriteExStorage != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(permission.isEmpty()){
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, permission.toArray(new String[permission.size()]), REQ_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(this, permission.toArray(new String[permission.size()]), REQ_PERMISSION);
            }
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSION:
                if( grantResults.length > 0) {
                    if(requestPermission()) {
                        reqGranted = true;
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}
