package com.hfad.coffeina;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DrinkCategoryActivity extends Activity {
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_category);
        ListView listDrinks = (ListView) findViewById(R.id.list_drinks);
        SQLiteOpenHelper coffeinaDatabaseHelper = new CoffeinaDatabaseHelper(this);
        try {
            db = coffeinaDatabaseHelper.getReadableDatabase();
            cursor = db.query("DRINK", new String[]{"_id", "NAME"}, null,
                    null, null, null, null);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1, cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1}, 0);
            listDrinks.setAdapter(listAdapter);
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        // Tworzymy obiekt nasłuchujący
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> listDrinks,
                                            View itemView,
                                            int position,
                                            long id) {
                        // Przekazujemy kliknięty napój do aktywność DrinkActivity
                        Intent intent = new Intent(DrinkCategoryActivity.this,
                                DrinkActivity.class);
                        intent.putExtra(DrinkActivity.EXTRA_DRINK_ID, (int) id);
                        startActivity(intent);
                    }
                };

        // Przypisujemy obiekt nasłuchujący do widoku listy
        listDrinks.setOnItemClickListener(itemClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
