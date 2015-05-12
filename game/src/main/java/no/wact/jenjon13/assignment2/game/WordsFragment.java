package no.wact.jenjon13.assignment2.game;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class WordsFragment extends android.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final String[] allValuesFromTable = getAllValuesFromTable(container);
        final LinearLayout fragmentLayout = (LinearLayout) rootView.findViewById(R.id.fragmentMainLayout);

        for (String value : allValuesFromTable) {
            final Button button = new Button(getActivity());
            button.setText(value);
            fragmentLayout.addView(button);
        }

        return rootView;
    }

    private String[] getAllValuesFromTable(ViewGroup container) {
        try (final Cursor cursor = new WordsOpenHelper(container.getContext())
                .getReadableDatabase()
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
