package no.wact.jenjon13.assignment2.game.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HighscoresOpenHelper extends SQLiteOpenHelper implements AutoCloseable {
    public static final String DATABASE_NAME = "ScoresDb";
    public static final String TABLE_NAME = "highscores";
    public static final String SCORE_COLUMN_NAME = "score";
    public static final String NAME_COLUMN_NAME = "name";

    private static final int DATABASE_VERSION = 1;
    private static final String SCORES_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
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

    public void saveScore(int score, String name) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            final ContentValues values = new ContentValues();
            values.put("score", score);
            values.put("name", name);


            int entriesCount;
            try (Cursor query = db.query(false, TABLE_NAME, null, null, null, null, null, null, null)) {
                entriesCount = query.getCount();
            }

            if (entriesCount < MAX_ENTRIES) {
                final long insert = db.insert(TABLE_NAME, null, values);
                Log.w("saveScore:insert", "Inserted as row #" + String.valueOf(insert));
                return;
            }

            /*
            final int delete = db.delete(TABLE_NAME, "score = (SELECT min(score) FROM " + TABLE_NAME + " LIMIT 1)", null);
            Log.w("delete", "affected rows: " + delete);
            */

            final long insert = db.insert(TABLE_NAME, null, values);
            Log.w("insert", "affected rows: " + insert);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
