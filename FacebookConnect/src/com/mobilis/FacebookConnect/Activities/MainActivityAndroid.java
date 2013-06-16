package com.mobilis.FacebookConnect.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.mobilis.FacebookConnect.Fragments.MainFragment;

/**
 * Created with IntelliJ IDEA.
 * User: ricardonakayama
 * Date: 15/06/13
 * Time: 9:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainActivityAndroid extends FragmentActivity {
    private MainFragment mainFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            mainFragment = new MainFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, mainFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            mainFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }
    }
}
