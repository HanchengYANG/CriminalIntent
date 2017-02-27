package com.yang.hancheng.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Project : CriminalIntent
 * Created by Hancheng Yang ( hancheng.yang@eptender.com )
 * Date : 21/02/2017, 15:24
 * Description :
 **/
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
