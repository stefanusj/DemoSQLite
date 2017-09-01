package com.stefanus.sqlite.biodata;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.stefanus.sqlite.biodata.SqliteContract.SqliteEntry;

public class LihatBiodata extends AppCompatActivity {

    private String selected;

    private Cursor cursor;

    private SqliteHelper sqliteHelper;

    private TextView tvNama, tvTtl, tvJk, tvAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_biodata);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selected = getIntent().getStringExtra(SqliteEntry.COLUMN_NAME);

        setTitle("Data " + selected);

        sqliteHelper = new SqliteHelper(this);

        tvNama = (TextView) findViewById(R.id.tvNama);
        tvTtl = (TextView) findViewById(R.id.tvTtl);
        tvJk = (TextView) findViewById(R.id.tvJk);
        tvAlamat = (TextView) findViewById(R.id.tvAlamat);

        setData(selected);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setData(String selected) {

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + SqliteEntry.TABLE_NAME + " where " + SqliteEntry.COLUMN_NAME + " = '" + selected + "'", null);

        cursor.moveToFirst();

        tvNama.setText(cursor.getString(1));
        tvTtl.setText(cursor.getString(2));
        tvJk.setText(cursor.getString(3));
        tvAlamat.setText(cursor.getString(4));

    }
}
