package com.stefanus.sqlite.biodata;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.stefanus.sqlite.biodata.SqliteContract.SqliteEntry;

public class UpdateBiodata extends AppCompatActivity {

    private String selected;

    private Calendar calendar;

    private Cursor cursor;
    private ContentValues cv;

    private SqliteHelper sqliteHelper;

    private RadioButton rbPria, rbWanita;
    private TextInputEditText etNama, etTtl, etAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sqliteHelper = new SqliteHelper(this);

        calendar = Calendar.getInstance();

        etNama = (TextInputEditText) findViewById(R.id.etNama);
        etTtl = (TextInputEditText) findViewById(R.id.etTtl);
        etAlamat = (TextInputEditText) findViewById(R.id.etAlamat);
        rbPria = (RadioButton) findViewById(R.id.rbPria);
        rbWanita = (RadioButton) findViewById(R.id.rbWanita);

        etTtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        selected = getIntent().getStringExtra(SqliteEntry.COLUMN_NAME);
        setData(selected);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_biodata, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.actionSave:
                saveData(selected);
                break;
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setData(String selected) {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + SqliteEntry.TABLE_NAME + " where " + SqliteEntry.COLUMN_NAME + " = '" + selected + "'", null);

        cursor.moveToFirst();

        etNama.setText(cursor.getString(1));
        etTtl.setText(cursor.getString(2));
        if (cursor.getString(3).equals(getString(R.string.male))) rbPria.setChecked(true);
        else if (cursor.getString(3).equals(getString(R.string.female))) rbWanita.setChecked(true);
        etAlamat.setText(cursor.getString(4));

    }

    private void saveData(String selected) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        cv = new ContentValues();
        cv.put(SqliteEntry.COLUMN_NAME, etNama.getText().toString());
        cv.put(SqliteEntry.COLUMN_BIRTHDAY, etTtl.getText().toString());
        String gender = "";
        if (rbPria.isChecked()) gender = getString(R.string.male);
        if (rbWanita.isChecked()) gender = getString(R.string.female);
        cv.put(SqliteEntry.COLUMN_GENDER, gender);
        cv.put(SqliteEntry.COLUMN_ADDRESS, etAlamat.getText().toString());

        try {
            db.update(
                    SqliteEntry.TABLE_NAME,
                    cv,
                    SqliteEntry.COLUMN_NAME + " ='" + selected + "'",
                    null);
            Toast.makeText(getApplicationContext(), R.string.prompt_1, Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.e("error", e.toString());
        } finally {
        }
    }

    private void updateLabel() {
        String myFormat = "yyyy/MMM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etTtl.setText(sdf.format(calendar.getTime()));
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
}
