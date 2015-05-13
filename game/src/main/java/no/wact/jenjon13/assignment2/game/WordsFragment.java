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
    private Map<GameWord, TextView> activeWords = new HashMap<>();
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

        answerButtons.clear();
        for (GameWord word : activeWords.keySet()) {
            answerButtons.add(new AnswerButton(context, word.getWord(), word.isCorrect()));
            fragmentLayout.addView(answerButtons.get(answerButtons.size() - 1));

            /* TODO: instead of holding on to activeWords,
             just present and forget all but one,
             when hiding, fetch new ones, shuffle in
             the correct word -- this loosens up a lot of dependencies
            */
            if (word.isCorrect()) {
                activeWords.get(word).setVisibility(View.GONE);
            }
        }
    }

    private void fetchAndShowWordSelection() {
        activeWords.clear();
        for (GameWord word : gameHandler.getSelectionOfRandomWords(NUMBER_OF_WORDS_PER_TURN)) {
            final TextView textView = new TextView(context);
            textView.setText(word.getWord());
            fragmentLayout.addView(textView);
            activeWords.put(word, textView);
        }
    }

    private void provideResponseToClient(boolean correct) {
        Toast.makeText(context, "Your answer was " + (correct ? "" : "not ") + "correct.", Toast.LENGTH_SHORT).show();
        fragmentLayout.findViewById(R.id.btnReady).setVisibility(View.VISIBLE);

        for (AnswerButton button : answerButtons) {
            fragmentLayout.removeView(button);
        }

        for (TextView view : activeWords.values()) {
            fragmentLayout.removeView(view);
        }

        fetchAndShowWordSelection();
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
