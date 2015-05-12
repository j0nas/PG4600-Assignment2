package no.wact.jenjon13.assignment2.game;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class WordsFragment extends android.app.Fragment {
    private List<String> words = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final String[] wordsFromDB = getAllValuesFromTable(container);
        for (String word : wordsFromDB) {
            words.add(word);
        }

        final LinearLayout fragmentLayout = (LinearLayout) rootView.findViewById(R.id.fragmentMainLayout);
        for (String value : words) {
            final Button button = new Button(getActivity());
            button.setText(value);
            fragmentLayout.addView(button);
        }

        final Button button = new Button(getActivity());
        button.setText("I'm ready!");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRound();
            }
        });
        fragmentLayout.addView(button);

        return rootView;
    }

    private void startRound() {
        Log.w("startRound", "Starting round!");
    }

    private String[] getAllValuesFromTable(ViewGroup container) {
        try (final SQLiteDatabase db = new WordsOpenHelper(container.getContext()).getReadableDatabase()) {
            try (final Cursor cursor = db
                    .rawQuery("SELECT * FROM " + WordsOpenHelper.WORDS_TABLE_NAME, null)) {

                List<String> values = new ArrayList<>();
                if (cursor.moveToFirst()) {
                    do {
                        values.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }

                return values.toArray(new String[values.size()]);
            }
        }
    }
}
