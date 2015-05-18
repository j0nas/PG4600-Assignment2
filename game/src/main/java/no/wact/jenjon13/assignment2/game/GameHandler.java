package no.wact.jenjon13.assignment2.game;

import android.content.Context;
import no.wact.jenjon13.assignment2.game.db.HighscoresOpenHelper;
import no.wact.jenjon13.assignment2.game.db.WordsOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameHandler {
    private final Context context;
    private List<String> loadedWords = new ArrayList<>();

    public GameHandler(Context context) {
        this.context = context;
        try (WordsOpenHelper wordsOpenHelper = new WordsOpenHelper(context)) {
            loadedWords = wordsOpenHelper.fetchWords();
        }
    }

    public List<String> getNRandomWords(int numberOfWords) {
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

    public void saveScore(int score) {
        try (HighscoresOpenHelper db = new HighscoresOpenHelper(context)) {
            db.saveScore(score, "TODO");
        }
    }
}
