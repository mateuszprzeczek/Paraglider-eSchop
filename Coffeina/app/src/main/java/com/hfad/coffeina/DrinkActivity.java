package com.hfad.coffeina;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends Activity {
    public static final String EXTRA_DRINK_ID = "drinkId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        // Pobieramy identyfikator napoju z intencji
        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINK_ID);

        SQLiteOpenHelper coffeinaDatabaseHelper = new CoffeinaDatabaseHelper(this);
        try {
            SQLiteDatabase db = coffeinaDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("DRINK", new String[]{
                            "NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?", new String[]{
                            Integer.toString(drinkId)}, null, null, null);
            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);

                TextView name = findViewById(R.id.name);
                name.setText(nameText);
                TextView description = findViewById(R.id.description);
                description.setText(descriptionText);
                ImageView photo = findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);
                CheckBox favorite = findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    //aktualizujemy bazę danych po kliknięciu pola wyboru
    public void onFavoriteClicked(View view){
        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINK_ID);

        //pobieramy wartość pola wyboru
        CheckBox favorite = findViewById(R.id.favorite);
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("FAVORITE", favorite.isChecked());

        //Pobieramy referencję do bazy danych i aktualizujemy kolumnę FAVORITE
        SQLiteOpenHelper coffeinaDatabaseHelper =
                new CoffeinaDatabaseHelper(DrinkActivity.this);
        try {
            SQLiteDatabase db = coffeinaDatabaseHelper.getWritableDatabase();
            db.update("DRINK", drinkValues, "_id = ?",
                    new String[]{Integer.toString(drinkId)});
            db.close();
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
