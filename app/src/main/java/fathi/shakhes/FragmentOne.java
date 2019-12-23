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
public class FragmentOne extends Fragment {

    public static FragmentOne newInstance(){
        return new FragmentOne();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);

        return rootView;
    }
}