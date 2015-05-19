package no.wact.jenjon13.assignment2.game.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import no.wact.jenjon13.assignment2.game.MainActivity;
import no.wact.jenjon13.assignment2.game.R;
import no.wact.jenjon13.assignment2.game.db.HighscoresOpenHelper;

public class RegisterScoreFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_register_score, container, false);

        rootView.findViewById(R.id.btnBackRegisterScore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragments(R.layout.fragment_title_screen, -1);
            }
        });


        final int score = getArguments().getInt(HighscoresOpenHelper.SCORE_COLUMN_NAME);
        ((TextView) rootView.findViewById(R.id.txtScoreRegisterScore)).setText(String.valueOf(score));

        rootView.findViewById(R.id.btnSaveRegisterScore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = ((TextView) rootView.findViewById(R.id.txtNameRegisterScore)).getText().toString().trim();
                if (!name.isEmpty()) {
                    try (HighscoresOpenHelper db = new HighscoresOpenHelper(rootView.getContext())) {
                        db.saveScore(score, name);
                        ((MainActivity) getActivity()).replaceFragments(R.layout.fragment_title_screen, -1);
                    }
                } else {
                    Toast.makeText(rootView.getContext(), "Enter a name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

}
