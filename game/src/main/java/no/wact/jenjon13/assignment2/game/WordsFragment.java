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

        fragmentLayout.addView(new NextRoundButton());
        fetchAndShowWordSelection();
        return rootView;
    }

    private void startRound() {
        // TODO: will fail if getNRandomWords gets enough words so that there aren't enough left for the answers!
        final List<String> unusedWords = gameHandler.getUnusedWords(activeWords);

        List<AnswerButton> answerButtons = new ArrayList<>();
        final int loopTo = Math.min(unusedWords.size(), ANSWERS_PER_TURN);
        for (int i = 0; i < loopTo; i++) {
            answerButtons.add(new AnswerButton(i == loopTo - 1 ? randomlyPickCorrectWord() : unusedWords.get(i)));
        }

        Collections.shuffle(answerButtons);
        for (AnswerButton button : answerButtons) {
            fragmentLayout.addView(button);
        }
    }

    private String randomlyPickCorrectWord() {
        correctWord = activeWords.get(new Random().nextInt(activeWords.size()));

        for (int i = 0; i < fragmentLayout.getChildCount(); i++) {
            final View view = fragmentLayout.getChildAt(i);
            if (view instanceof TextView && ((TextView) view).getText().equals(correctWord)) {
                view.setVisibility(View.GONE);
            }
        }

        return correctWord;
    }

    private void fetchAndShowWordSelection() {
        activeWords = gameHandler.getNRandomWords(WORDS_PER_TURN);

        for (int i = 0; i < activeWords.size(); i++) {
            final TextView textView = new TextView(context);
            textView.setText(activeWords.get(i));
            fragmentLayout.addView(textView);
        }
    }

    private void provideResponseToClient(String answer) {
        Toast.makeText(context, "Your answer was " +
                (answer.equals(correctWord) ? "" : "not ") + "correct.", Toast.LENGTH_SHORT).show();

        fragmentLayout.removeAllViews();
        fragmentLayout.addView(new NextRoundButton());
        fetchAndShowWordSelection();
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
