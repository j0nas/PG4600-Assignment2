package no.wact.jenjon13.assignment2.game.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import no.wact.jenjon13.assignment2.game.R;

import java.util.ArrayList;
import java.util.List;

public class WordsOpenHelper extends SQLiteOpenHelper implements AutoCloseable {
    public static final String DATABASE_NAME = "WordsDb";
    public static final String TABLE_NAME = "words";
    public static final String COLUMN_NAME = "word";
    private static final int DATABASE_VERSION = 1;
    private static final String WORDS_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME + " VARCHAR" +
                    ");";
    private final Context context;

    public WordsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public List<String> fetchWords() {
        final ArrayList<String> words = new ArrayList<>();

        try (final SQLiteDatabase db = this.getReadableDatabase();
             final Cursor cursor = db.rawQuery("SELECT * FROM " + WordsOpenHelper.TABLE_NAME, null)) {
            if (cursor.moveToFirst()) {
                do {
                    words.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        }

        return words;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WORDS_TABLE_CREATE);

        final StringBuilder builder = new StringBuilder("INSERT INTO " + TABLE_NAME + " VALUES ");
        final String[] companies = context.getResources().getStringArray(R.array.companies);
        for (int i = 0; i < companies.length; i++) {
            builder.append("('").append(companies[i]).append("')").append(i != companies.length - 1 ? ", " : "");
        }

        db.execSQL(builder.toString());
        Log.w("onCreate", "Created table!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
