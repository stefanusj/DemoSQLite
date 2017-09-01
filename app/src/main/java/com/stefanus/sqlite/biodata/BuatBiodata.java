package com.stefanus.sqlite.biodata;

import android.app.DatePickerDialog;
import android.content.ContentValues;
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

public class BuatBiodata extends AppCompatActivity {

    private Calendar calendar;

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
                insertData();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertData() {
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
            db.beginTransaction();
            db.insert(SqliteEntry.TABLE_NAME, null, cv);
            db.setTransactionSuccessful();
            Toast.makeText(getApplicationContext(), R.string.prompt_1, Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.e("error", e.toString());
        } finally {
            db.endTransaction();
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
