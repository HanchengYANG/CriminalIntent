package com.yang.hancheng.criminalintent;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
        setContentView(getLayoutResId());
        fm = getSupportFragmentManager();
        mFragment = fm.findFragmentById(R.id.fragment_container);
        if(mFragment == null) {
            mFragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, mFragment).commit();
        }
    }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }
}
