package no.wact.jenjon13.assignment2.game;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordsFragment extends android.app.Fragment {
    private static final int WORDS_PER_TURN = 5;
    private static final int ANSWERS_PER_TURN = 3;

    private List<String> activeWords;
    private String correctWord;

    private GameHandler gameHandler;
    private Context context;
    private LinearLayout fragmentLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        fragmentLayout = (LinearLayout) rootView.findViewById(R.id.fragmentMainLayout);
        context = container.getContext();
        gameHandler = new GameHandler(context);

        resetUI();
        return rootView;
    }

    private void startRound() {
        correctWord = activeWords.get(new Random().nextInt(activeWords.size()));
        fragmentLayout.removeAllViews();

        Collections.shuffle(activeWords);
        generateWordsViews(activeWords, true);

        // TODO: will fail if getNRandomWords gets enough words so that there aren't enough left for the answers!
        final List<String> unusedWords = gameHandler.getUnusedWords(activeWords);

        List<AnswerButton> answerButtons = new ArrayList<>();
        final int loopTo = Math.min(unusedWords.size(), ANSWERS_PER_TURN);
        for (int i = 0; i < loopTo; i++) {
            answerButtons.add(new AnswerButton(i == loopTo - 1 ? correctWord : unusedWords.get(i)));
        }

        Collections.shuffle(answerButtons);
        for (AnswerButton button : answerButtons) {
            fragmentLayout.addView(button);
        }
    }

    private void generateWordsViews(List<String> words, boolean hideCorrectWord) {
        for (int i = 0; i < words.size(); i++) {
            if (!(hideCorrectWord && words.get(i).equals(correctWord))) {
                final TextView textView = new TextView(context);
                textView.setText(words.get(i));
                fragmentLayout.addView(textView);
            }
        }
    }

    private void provideResponseToClient(String answer) {
        Toast.makeText(context, "Your answer was " + (answer.equals(correctWord) ? "" : "not ") + "correct.",
                Toast.LENGTH_SHORT).show();
        resetUI();
    }

    private void resetUI() {
        fragmentLayout.removeAllViews();
        fragmentLayout.addView(new NextRoundButton());
        generateWordsViews(activeWords = gameHandler.getNRandomWords(WORDS_PER_TURN), false);
    }

    private class NextRoundButton extends Button {
        public NextRoundButton() {
            super(context);
            this.setText("Go!");
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRound();
                    fragmentLayout.removeView(v);
                }
            });
        }
    }

    private class AnswerButton extends Button {
        public AnswerButton(final String label) {
            super(context);
            this.setText(label);
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    provideResponseToClient(label);
                }
            });
        }
    }
}
