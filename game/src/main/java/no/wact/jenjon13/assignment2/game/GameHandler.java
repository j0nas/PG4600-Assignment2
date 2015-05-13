package no.wact.jenjon13.assignment2.game;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameHandler {
    private List<GameWord> words = new ArrayList<>();
    private List<GameWord> activeWords = new ArrayList<>();

    public GameHandler(Context context) {
        try (final SQLiteDatabase db = new WordsOpenHelper(context).getReadableDatabase()) {
            try (final Cursor cursor = db.rawQuery("SELECT * FROM " + WordsOpenHelper.TABLE_NAME, null)) {
                if (cursor.moveToFirst()) {
                    do {
                        words.add(new GameWord(cursor.getString(0)));
                    } while (cursor.moveToNext());
                }
            }
        }
    }

    public List<GameWord> getSelectionOfRandomWords(int numberOfWords) {
        Collections.shuffle(words);
        activeWords = words.subList(0, Math.min(numberOfWords, words.size()));
        activeWords.get(0).setCorrect(true);
        return activeWords;
    }
}
