package no.wact.jenjon13.assignment2.game.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HighscoresOpenHelper extends SQLiteOpenHelper implements AutoCloseable {
    public static final String DATABASE_NAME = "ScoresDb";
    public static final String TABLE_NAME = "highscores";
    public static final String SCORE_COLUMN_NAME = "score";
    public static final String NAME_COLUMN_NAME = "name";
    private static final String ID_COLUMN_NAME = "id";

    private static final int DATABASE_VERSION = 1;
    private static final String SCORES_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    SCORE_COLUMN_NAME + " INT, " +
                    NAME_COLUMN_NAME + " VARCHAR" +
                    ");";
    private static final int MAX_ENTRIES = 7;

    public HighscoresOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCORES_TABLE_CREATE);
    }

    public String[] getHighscores() {
        final List<String> list = new ArrayList<>();
        try (SQLiteDatabase readableDatabase = this.getReadableDatabase();
             Cursor cursor = readableDatabase.query(false, TABLE_NAME, null, null, null, null, null, "score DESC", null)) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(2) + ": " + cursor.getInt(1));
                } while (cursor.moveToNext());
            }
        }

        return list.toArray(new String[list.size()]);
    }

    public void saveScore(int score, String name) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            final ContentValues values = new ContentValues();
            values.put(SCORE_COLUMN_NAME, score);
            values.put(NAME_COLUMN_NAME, name);

            try (Cursor query = db.query(false, TABLE_NAME, null, null, null, null, null, null, null)) {
                if (query.getCount() < MAX_ENTRIES) {
                    db.insert(TABLE_NAME, null, values);
                    return;
                }
            }

            String id = null;
            try (Cursor cursor = db.rawQuery("SELECT id, score FROM " + TABLE_NAME + " ORDER BY score ASC LIMIT 1", null)) {
                if (cursor.moveToFirst()) {
                    if (score <= cursor.getInt(1)) {
                        Log.w("saveScore", "The lowest found value isn higher than the provided score.");
                        return;
                    }

                    id = cursor.getString(0);
                }
            }

            db.update(TABLE_NAME, values, "id = " + id, null);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
