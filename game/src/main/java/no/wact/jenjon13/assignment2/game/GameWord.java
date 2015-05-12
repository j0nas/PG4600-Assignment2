package no.wact.jenjon13.assignment2.game;

public class GameWord {
    private String word;
    private boolean correct = false;

    public GameWord(String word) {
        this.word = word;
    }

    public GameWord(String word, boolean correct) {
        this.word = word;
        this.correct = correct;
    }

    public String getWord() {
        return word;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean isCorrect) {
        this.correct = isCorrect;
    }
}
