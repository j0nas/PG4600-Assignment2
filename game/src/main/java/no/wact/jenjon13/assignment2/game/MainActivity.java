package no.wact.jenjon13.assignment2.game;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
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
}
