package com.yang.hancheng.criminalintent;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.yang.hancheng.criminalintent.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Project : CriminalIntent
 * Created by Hancheng Yang ( hancheng.yang@eptender.com )
 * Date : 06/03/2017, 14:02
 * Description :
 **/
public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        Date date = new Date(getLong(getColumnIndex(CrimeTable.Cols.DATE)));
        boolean isSolved = (getInt(getColumnIndex(CrimeTable.Cols.SOLVED)) == 1);
        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(date);
        crime.setSolved(isSolved);
        return crime;
    }

}
