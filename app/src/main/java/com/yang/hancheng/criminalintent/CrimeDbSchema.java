package com.yang.hancheng.criminalintent;

/**
 * Project : CriminalIntent
 * Created by Hancheng Yang ( hancheng.yang@eptender.com )
 * Date : 06/03/2017, 10:44
 * Description :
 **/
public class CrimeDbSchema {
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
