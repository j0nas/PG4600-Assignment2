package no.wact.jenjon13.assignment2.game.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import no.wact.jenjon13.assignment2.game.MainActivity;
import no.wact.jenjon13.assignment2.game.R;

public class TitleScreenFragment extends Fragment {
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String buttonText = ((Button) v).getText().toString();
            int targetFragmentId = -1;

            if (buttonText.equals(getResources().getString(R.string.titlescreen_btnAbout))) {
                targetFragmentId = R.layout.fragment_about;
            } else if (buttonText.equals(getResources().getString(R.string.titlescreen_btnHighscores))) {
                targetFragmentId = R.layout.fragment_higscores;
            } else {
                targetFragmentId = R.layout.fragment_main;
            }

            ((MainActivity) getActivity()).replaceFragments(targetFragmentId, -1);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_title_screen, container, false);
        for (View view : rootView.getTouchables()) {
            if (view instanceof Button) {
                view.setOnClickListener(clickListener);
            }
        }

        return rootView;
    }

}
