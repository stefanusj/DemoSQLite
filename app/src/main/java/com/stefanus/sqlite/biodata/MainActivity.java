package com.stefanus.sqlite.biodata;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String[] data;

    private Cursor cursor;

    private SqliteHelper sqliteHelper;

    private FloatingActionButton fabTambah;
    private ListView lvNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqliteHelper = new SqliteHelper(this);

        fabTambah = (FloatingActionButton) findViewById(R.id.fabTambah);
        lvNama = (ListView) findViewById(R.id.lvNama);

        fabTambah.setOnClickListener(onFabClicked);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllBiodata();
    }

    private void getAllBiodata() {

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + SqliteContract.SqliteEntry.TABLE_NAME, null);

        data = new String[cursor.getCount()];

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            data[i] = cursor.getString(1);
        }

        lvNama.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data));

        lvNama.setOnItemClickListener(onListClicked);

    }

    private View.OnClickListener onFabClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, BuatBiodata.class));
        }
    };

    private AdapterView.OnItemClickListener onListClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final String selected = data[position];
            final CharSequence[] dialogItem = {"Lihat Biodata", "Update Biodata", "Hapus Biodata"};
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Pilihan");
            builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0: //Lihat Biodata
                            Intent intent = new Intent(MainActivity.this, LihatBiodata.class);
                            intent.putExtra(SqliteContract.SqliteEntry.COLUMN_NAME, selected);
                            startActivity(intent);
                            break;
                        case 1: //Update Biodata
                            Intent intent1 = new Intent(MainActivity.this, UpdateBiodata.class);
                            intent1.putExtra(SqliteContract.SqliteEntry.COLUMN_NAME, selected);
                            startActivity(intent1);
                            break;
                        case 2: //Hapus Biodata
                            try {
                                SQLiteDatabase db = sqliteHelper.getWritableDatabase();
                                db.delete(SqliteContract.SqliteEntry.TABLE_NAME, SqliteContract.SqliteEntry.COLUMN_NAME + " = '" + selected + "'", null);
                                Toast.makeText(getApplicationContext(), R.string.prompt_2, Toast.LENGTH_SHORT).show();
                                db.close();
                            } catch (Exception e) {
                                Log.e("error", e.toString());
                            } finally {
                                getAllBiodata();
                            }
                            break;
                    }
                }
            });
            builder.create().show();
        }
    };
}
