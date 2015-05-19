package no.wact.jenjon13.assignment2.game;

import android.content.Context;
import no.wact.jenjon13.assignment2.game.db.WordsOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameHandler {
    private List<String> loadedWords = new ArrayList<>();

    public GameHandler(Context context) {
        try (WordsOpenHelper wordsOpenHelper = new WordsOpenHelper(context)) {
            loadedWords = wordsOpenHelper.fetchWords();
        }
    }

    /**
     * Returns a list of N random words from the list of loaded words.
     *
     * @param numberOfWords The number of words that the resulting list should contain.
     *                      Will not be greater than the number of words loaded.
     * @return A list containing a random selection of 'numberOfWords' words.
     */
    public List<String> getNRandomWords(int numberOfWords) {
        Collections.shuffle(loadedWords);
        return loadedWords.subList(0, Math.min(numberOfWords, loadedWords.size()));
    }

    /**
     * Given a list of words, will return all loaded words not found in the provided list.
     *
     * @param usedWords A list of words
     * @return A list containing all words in 'loadedWords' not found in the provided 'usedWords' list.
     */
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
