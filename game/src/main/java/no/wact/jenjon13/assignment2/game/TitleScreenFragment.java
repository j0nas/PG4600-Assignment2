package no.wact.jenjon13.assignment2.game;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TitleScreenFragment extends Fragment {
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((MainActivity) getActivity()).replaceFragments(((Button) v).getText().toString());
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
