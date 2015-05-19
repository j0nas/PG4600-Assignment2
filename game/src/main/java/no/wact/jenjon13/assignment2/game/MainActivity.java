package no.wact.jenjon13.assignment2.game;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import no.wact.jenjon13.assignment2.game.fragments.*;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new TitleScreenFragment())
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

    public void replaceFragments(int fragmentId, int score) {
        Fragment newFragment;
        switch (fragmentId) {
            case R.layout.fragment_menuscreen:
                newFragment = new TitleScreenFragment();
                break;
            case R.layout.fragment_main:
                newFragment = new WordsFragment();
                break;
            case R.layout.fragment_register_score:
                newFragment = new RegisterScoreFragment();
                final Bundle bundle = new Bundle();
                bundle.putInt("SCORE", score);
                newFragment.setArguments(bundle);
                break;
            case R.layout.fragment_higscores:
                newFragment = new HighscoresFragment();
                break;
            case R.layout.fragment_about:
                newFragment = new AboutFragment();
                break;
            default:
                Log.wtf("replaceFragments", "No such case: " + fragmentId);
                return;
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, newFragment)
                .addToBackStack(null)
                .commit();
    }
}
