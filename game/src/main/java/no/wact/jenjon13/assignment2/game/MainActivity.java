package no.wact.jenjon13.assignment2.game;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import no.wact.jenjon13.assignment2.game.db.HighscoresOpenHelper;
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

    /**
     * Replaces the currently visible fragment.
     *
     * @param fragmentId The id of the fragment which the current one is to be replaced with.
     * @param score      Convenience parameter for when switching to 'register_score' fragment.
     *                   Can be -1 for all other fragments.
     */
    public void replaceFragments(int fragmentId, int score) {
        Fragment newFragment;
        switch (fragmentId) {
            case R.layout.fragment_title_screen:
                newFragment = new TitleScreenFragment();
                break;
            case R.layout.fragment_main:
                newFragment = new WordsFragment();
                break;
            case R.layout.fragment_register_score:
                newFragment = new RegisterScoreFragment();
                final Bundle bundle = new Bundle();
                bundle.putInt(HighscoresOpenHelper.SCORE_COLUMN_NAME, score);
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
