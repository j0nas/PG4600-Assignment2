package no.wact.jenjon13.assignment2.game;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordsFragment extends android.app.Fragment {
    private static final int WORDS_PER_TURN = 5;
    private static final int ANSWERS_PER_TURN = 3;

    private String correctWord;

    private GameHandler gameHandler;
    private Context context;
    private RelativeLayout fragmentLayout;
    private LinearLayout controlsContain;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        fragmentLayout = (RelativeLayout) rootView.findViewById(R.id.fragmentMainLayout);
        context = container.getContext();
        gameHandler = new GameHandler(context);

        controlsContain = (LinearLayout) fragmentLayout.findViewById(R.id.controlsContain);
        controlsContain.addView(new NextRoundButton());

        adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1);
        ((ListView) fragmentLayout.findViewById(R.id.wordsList)).setAdapter(adapter);

        adapter.addAll(gameHandler.getNRandomWords(WORDS_PER_TURN));

        return rootView;
    }

    private void startRound() {
        List<String> currentWords = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            currentWords.add(adapter.getItem(i));
        }

        correctWord = currentWords.remove(new Random().nextInt(currentWords.size()));
        Collections.shuffle(currentWords);
        adapter.clear();
        adapter.addAll(currentWords);

        controlsContain.removeAllViews();
        // TODO: will fail if getNRandomWords gets enough words so that there aren't enough left for the answers!
        final List<String> unusedWords = gameHandler.getUnusedWords(currentWords);

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

    private void provideResponseToClient(String answer) {
        Toast.makeText(context, "Your answer was " + (answer.equals(correctWord) ? "" : "not ") + "correct.",
                Toast.LENGTH_SHORT).show();

        adapter.clear();
        adapter.addAll(gameHandler.getNRandomWords(WORDS_PER_TURN));

        controlsContain.removeAllViews();
        controlsContain.addView(new NextRoundButton());
    }

    private class NextRoundButton extends Button {
        public NextRoundButton() {
            super(context);
            this.setText("Go!");
            this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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
            this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    provideResponseToClient(label);
                }
            });
        }
    }
}
