package no.wact.jenjon13.assignment2.game;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameHandler {
    private List<String> loadedWords = new ArrayList<>();

    public GameHandler(Context context) {
        try (final SQLiteDatabase db = new WordsOpenHelper(context).getReadableDatabase()) {
            try (final Cursor cursor = db.rawQuery("SELECT * FROM " + WordsOpenHelper.TABLE_NAME, null)) {
                if (cursor.moveToFirst()) {
                    do {
                        loadedWords.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }
            }
        }
    }

    public List<String> getRandomWordSelection(int numberOfWords) {
        Collections.shuffle(loadedWords);
        return loadedWords.subList(0, Math.min(numberOfWords, loadedWords.size()));
    }

    public List<String> getUnusedWords(List<String> usedWords) {
        List<String> unusedWords = new ArrayList<>();

        for (String word : loadedWords) {
            if (!usedWords.contains(word)) {
                unusedWords.add(word);
            }
        }

        return unusedWords;
    }
}
