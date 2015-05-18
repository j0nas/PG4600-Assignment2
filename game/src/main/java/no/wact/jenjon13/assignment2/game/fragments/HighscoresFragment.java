package no.wact.jenjon13.assignment2.game.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import no.wact.jenjon13.assignment2.game.R;

public class HighscoresFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_higscores, container, false);
        rootView.findViewById(R.id.btnBackHighscores).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        return rootView;
    }
}
