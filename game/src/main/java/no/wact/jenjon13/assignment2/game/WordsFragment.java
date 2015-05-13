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
    private static final int NUMBER_OF_WORDS_PER_TURN = 5;

    private List<String> activeWords;
    private TextView[] activeWordViews;
    private String correctWord;

    private GameHandler gameHandler;
    private Context context;
    private LinearLayout fragmentLayout;
    private List<AnswerButton> answerButtons = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        fragmentLayout = (LinearLayout) rootView.findViewById(R.id.fragmentMainLayout);
        context = container.getContext();
        gameHandler = new GameHandler(context);

        fetchAndShowWordSelection();

        rootView.findViewById(R.id.btnReady).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRound();
            }
        });
        return rootView;
    }

    /**
     * Hides the "correct" word and displays option buttons.
     */
    private void startRound() {
        fragmentLayout.findViewById(R.id.btnReady).setVisibility(View.GONE);

        final int correctWordIndex = new Random().nextInt(activeWords.size());
        correctWord = activeWords.get(correctWordIndex);
        activeWordViews[correctWordIndex].setVisibility(View.GONE);

        final List<String> unusedWords = gameHandler.getUnusedWords(activeWords);

        answerButtons.clear();
        answerButtons.add(new AnswerButton(context, unusedWords.get(0)));
        answerButtons.add(new AnswerButton(context, unusedWords.get(1)));
        answerButtons.add(new AnswerButton(context, correctWord));
        Collections.shuffle(answerButtons);
        for (AnswerButton button : answerButtons) {
            fragmentLayout.addView(button);
        }
    }

    private void fetchAndShowWordSelection() {
        activeWords = gameHandler.getRandomWordSelection(NUMBER_OF_WORDS_PER_TURN);
        activeWordViews = new TextView[activeWords.size()];

        for (int i = 0; i < activeWords.size(); i++) {
            final TextView textView = new TextView(context);
            textView.setText(activeWords.get(i));
            fragmentLayout.addView(textView);
            activeWordViews[i] = textView;
        }
    }

    private void provideResponseToClient(String answer) {
        Toast.makeText(context, "Your answer was " +
                (answer.equals(correctWord) ? "" : "not ") + "correct.", Toast.LENGTH_SHORT).show();
        fragmentLayout.findViewById(R.id.btnReady).setVisibility(View.VISIBLE);

        for (AnswerButton button : answerButtons) {
            fragmentLayout.removeView(button);
        }

        for (TextView view : activeWordViews) {
            fragmentLayout.removeView(view);
        }

        fetchAndShowWordSelection();
    }

    private class AnswerButton extends Button {
        public AnswerButton(Context context, final String label) {
            super(context);
            this.setText(label);
            this.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            provideResponseToClient(label);
                        }
                    });
        }
    }
}
