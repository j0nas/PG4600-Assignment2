package no.wact.jenjon13.assignment2.game.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class HighscoresOpenHelper extends SQLiteOpenHelper implements AutoCloseable {
    public static final String DATABASE_NAME = "ScoresDb";
    public static final String TABLE_NAME = "highscores";
    public static final String SCORE_COLUMN_NAME = "score";
    public static final String NAME_COLUMN_NAME = "name";
    public static final String ID_COLUMN_NAME = "id";
    public static final int MAX_ENTRIES = 7;

    private static final int DATABASE_VERSION = 1;
    private static final String SCORES_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    SCORE_COLUMN_NAME + " INT, " +
                    NAME_COLUMN_NAME + " VARCHAR" +
                    ");";
    private final SQLiteDatabase db;

    public HighscoresOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCORES_TABLE_CREATE);
    }

    /**
     * Gets all the held rows in the table as a String array.
     *
     * @return A String[] with values with the following formatting: "name: score".
     */
    public String[] getHighscores() {
        final List<String> list = new ArrayList<>();
        try (Cursor cursor = db.query(false, TABLE_NAME, null, null, null, null, null, "score DESC", null)) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(2) + ": " + cursor.getInt(1));
                } while (cursor.moveToNext());
            }
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * Creates a new entry in the table with the provided values if the table has fewer than
     * {@link MAX_ENTRIES} entries, or overwrite the lowest score in the table if any entries
     * with lower scores than the provided score are found, using {@link #getIdOfLowestScoreEntryIfLowerThan(int)
     * getIdOfLowestScoreEntryIfLowerThan}
     *
     * @param score The score to persist.
     * @param name  The name which is to be associated with the provided score.
     */
    public void saveScore(int score, String name) {
        final ContentValues values = new ContentValues();
        values.put(SCORE_COLUMN_NAME, score);
        values.put(NAME_COLUMN_NAME, name);

        try (Cursor query = db.query(false, TABLE_NAME, null, null, null, null, null, null, null)) {
            if (query.getCount() < MAX_ENTRIES) {
                db.insert(TABLE_NAME, null, values);
                return;
            }
        }

        int id = getIdOfLowestScoreEntryIfLowerThan(score);
        if (id > -1) {
            db.update(TABLE_NAME, values, "id = " + id, null);
        }
    }

    /**
     * Convenience method to count the amount of entries held by the highscore table.
     *
     * @return The amount of rows held by the highscore table, as an integer
     */
    public int countHighScoreEntries() {
        try (Cursor query = db.query(false, TABLE_NAME, null, null, null, null, null, null, null)) {
            return query.getCount();
        }
    }

    /**
     * Returns the id of the row with the lowest score if it is lower than the provided parameter.
     *
     * @param score The minimum score, which found entries have to be higher than, in order to match.
     * @return The id of the corresponding row as an integer, if any row with a score lower than
     * the provided parameter was found. Otherwise, returns -1
     */
    public int getIdOfLowestScoreEntryIfLowerThan(int score) {
        try (Cursor cursor = db.rawQuery("SELECT id, score FROM " + TABLE_NAME + " ORDER BY score ASC LIMIT 1", null)) {
            return (cursor.moveToFirst() && (score > cursor.getInt(1))) ? cursor.getInt(0) : -1;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
