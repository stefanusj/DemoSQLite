package com.stefanus.sqlite.biodata;

import android.provider.BaseColumns;

/**
 * Created by Stefanus on 02/09/2017.
 */

public class SqliteContract {

    public static final class SqliteEntry implements BaseColumns {
        public static final String TABLE_NAME = "biodata";
        public static final String COLUMN_NAME = "nama";
        public static final String COLUMN_BIRTHDAY = "ttl";
        public static final String COLUMN_GENDER = "jenis_kelamin";
        public static final String COLUMN_ADDRESS = "alamat";
    }
}
