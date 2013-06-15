package com.mobilis.FacebookConnect.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mobilis.FacebookConnect.R;

/**
 * Created with IntelliJ IDEA.
 * User: ricardonakayama
 * Date: 15/06/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SplashFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash,
                container, false);
        return view;
    }
}
