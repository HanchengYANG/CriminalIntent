package com.yang.hancheng.criminalintent;

import android.support.v4.app.Fragment;
import android.os.Bundle;

/**
 * Project : CriminalIntent
 * Created by Hancheng Yang ( hancheng.yang@eptender.com )
 * Date : 21/02/2017, 15:09
 * Description :
 **/

public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
