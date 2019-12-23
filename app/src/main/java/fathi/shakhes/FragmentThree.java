package fathi.shakhes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import shakhes.R;

/**
 * Created by docotel on 3/2/16.
 */
public class FragmentThree extends Fragment {

    public static FragmentThree newInstance(){
        return new FragmentThree();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_three, container, false);

        return rootView;
    }
}
