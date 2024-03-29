package no.wact.jenjon13.assignment2.game.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import no.wact.jenjon13.assignment2.game.GameHandler;
import no.wact.jenjon13.assignment2.game.MainActivity;
import no.wact.jenjon13.assignment2.game.R;
import no.wact.jenjon13.assignment2.game.db.HighscoresOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordsFragment extends Fragment {
    public static final String ROUND_TXT_PREFIX = "Round #";
    private static final int WORDS_PER_TURN = 5;
    private static final int ANSWERS_PER_TURN = 3;
    private String correctWord;
    private GameHandler gameHandler;
    private Context context;
    private RelativeLayout gameContain;
    private LinearLayout controlsContain;
    private ArrayAdapter<String> adapter;
    private int roundNumber = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gameContain = (RelativeLayout) rootView.findViewById(R.id.fragmentGameLayout);
        context = container.getContext();
        gameHandler = new GameHandler(context);

        controlsContain = (LinearLayout) gameContain.findViewById(R.id.controlsContain);
        controlsContain.addView(new NextRoundButton());

        adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1);
        ((ListView) gameContain.findViewById(R.id.wordsList)).setAdapter(adapter);

        adapter.addAll(gameHandler.getNRandomWords(WORDS_PER_TURN));

        return rootView;
    }

    /**
     * Randomly removes one of the previously displayed words.
     * Initializes and displays correct and wrong answers for the client.
     */
    private void startRound() {
        ((TextView) gameContain.findViewById(R.id.txtRoundNum)).setText(ROUND_TXT_PREFIX + ++roundNumber);

        List<String> currentWords = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            currentWords.add(adapter.getItem(i));
        }

        // TODO: will fail if getNRandomWords gets enough words so that there aren't enough left for the answers!
        final List<String> unusedWords = gameHandler.getUnusedWords(currentWords);

        correctWord = currentWords.remove(new Random().nextInt(currentWords.size()));
        Collections.shuffle(currentWords);
        adapter.clear();
        adapter.addAll(currentWords);

        controlsContain.removeAllViews();
        List<AnswerButton> answerButtons = new ArrayList<>();
        final int loopTo = Math.min(unusedWords.size(), ANSWERS_PER_TURN);
        for (int i = 0; i < loopTo; i++) {
            answerButtons.add(new AnswerButton(i == loopTo - 1 ? correctWord : unusedWords.get(i)));
        }

        Collections.shuffle(answerButtons);
        for (AnswerButton button : answerButtons) {
            controlsContain.addView(button);
        }
    }

    /**
     * Provides feedback to the client and initializes "Start next round" controls.
     *
     * @param answer The answer which the client picked, which is to be evaluated for correctness.
     */
    private void provideResponseToClient(String answer) {
        final boolean answerIsCorrect = answer.equals(correctWord);
        Toast.makeText(context, "Your answer was " + (answerIsCorrect ? "" : "not ") + "correct.",
                Toast.LENGTH_SHORT).show();

        if (answerIsCorrect) {
            adapter.clear();
            adapter.addAll(gameHandler.getNRandomWords(WORDS_PER_TURN));

            controlsContain.removeAllViews();
            controlsContain.addView(new NextRoundButton());
            return;
        }

        try (HighscoresOpenHelper db = new HighscoresOpenHelper(context)) {
            ((MainActivity) getActivity()).replaceFragments(
                    (roundNumber - 1 > 0) &&
                            (db.countHighScoreEntries() < HighscoresOpenHelper.MAX_ENTRIES ||
                                    db.getIdOfLowestScoreEntryIfLowerThan(roundNumber - 1) > -1) ?
                            R.layout.fragment_register_score : R.layout.fragment_title_screen, roundNumber - 1);
        }
    }

    private class NextRoundButton extends Button {
        public NextRoundButton() {
            super(context);
            this.setText("Go!");
            this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRound();
                    gameContain.removeView(v);
                }
            });
        }
    }

    private class AnswerButton extends Button {
        public AnswerButton(final String label) {
            super(context);
            this.setText(label);
            this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    provideResponseToClient(label);
                }
            });
        }
    }
}
