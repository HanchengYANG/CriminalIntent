package com.yang.hancheng.criminalintent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Project : CriminalIntent
 * Created by Hancheng Yang ( hancheng.yang@eptender.com )
 * Date : 21/02/2017, 15:09
 * Description :
 **/

public abstract class SingleFragmentActivity extends AppCompatActivity {
    private FragmentManager fm;
    private Fragment mFragment;
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        fm = getSupportFragmentManager();
        mFragment = fm.findFragmentById(R.id.fragment_container);
        startFragment(mFragment);
    }

    private void startFragment(Fragment fragment) {
        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
