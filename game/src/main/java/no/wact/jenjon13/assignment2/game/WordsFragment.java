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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordsFragment extends android.app.Fragment {
    private static final int NUMBER_OF_WORDS_PER_TURN = 5;
    private Map<GameWord, TextView> words = new HashMap<>();
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

        showWordSelection();

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
        initNextRound();

        for (GameWord word : words.keySet()) { //TODO looser coupling to dependency
            answerButtons.add(new AnswerButton(context, word.getWord(), word.isCorrect()));
            fragmentLayout.addView(answerButtons.get(answerButtons.size() - 1));

            if (word.isCorrect()) {
                words.get(word).setVisibility(View.GONE);
            }
        }
    }

    private void showWordSelection() {
        for (GameWord word : gameHandler.getSelectionOfRandomWords(NUMBER_OF_WORDS_PER_TURN)) {
            final TextView textView = new TextView(context);
            textView.setText(word.getWord());
            fragmentLayout.addView(textView);
            words.put(word, textView);
        }
    }

    private void initNextRound() {
        fragmentLayout.findViewById(R.id.btnReady).setVisibility(View.GONE);
        answerButtons.clear();
        //words.clear();
        //showWordSelection();
    }

    private void provideResponseToClient(boolean correct) {
        Toast.makeText(context, "Your answer was " + (correct ? "" : "not ") + "correct.", Toast.LENGTH_SHORT).show();
        fragmentLayout.findViewById(R.id.btnReady).setVisibility(View.VISIBLE);

        for (AnswerButton button : answerButtons) {
            fragmentLayout.removeView(button);
        }

        for (TextView view : words.values()) {
            fragmentLayout.removeView(view);
        }
    }

    private class AnswerButton extends Button {
        public AnswerButton(Context context, String label, final boolean correct) {
            super(context);
            this.setText(label);
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    provideResponseToClient(correct);
                }
            });
        }
    }
}
