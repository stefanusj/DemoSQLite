package com.stefanus.sqlite.biodata;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Stefanus on 01/09/2017.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "biodatadiri.db";
    private static final int DATABASE_VERSION = 1;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + SqliteContract.SqliteEntry.TABLE_NAME + "(" +
                SqliteContract.SqliteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SqliteContract.SqliteEntry.COLUMN_NAME + " TEXT, " +
                SqliteContract.SqliteEntry.COLUMN_BIRTHDAY + " TEXT, " +
                SqliteContract.SqliteEntry.COLUMN_GENDER + " TEXT, " +
                SqliteContract.SqliteEntry.COLUMN_ADDRESS + " TEXT)";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);

        ContentValues cv = new ContentValues();
        cv.put(SqliteContract.SqliteEntry.COLUMN_NAME, "Stefanus Julianto");
        cv.put(SqliteContract.SqliteEntry.COLUMN_BIRTHDAY, "1997/May/13");
        cv.put(SqliteContract.SqliteEntry.COLUMN_GENDER, "Laki-laki");
        cv.put(SqliteContract.SqliteEntry.COLUMN_ADDRESS, "Jalan Borobudur Raya A 796, Kota Bekasi");

        db.insert(SqliteContract.SqliteEntry.TABLE_NAME, null, cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SqliteContract.SqliteEntry.TABLE_NAME);
        onCreate(db);
    }
}
