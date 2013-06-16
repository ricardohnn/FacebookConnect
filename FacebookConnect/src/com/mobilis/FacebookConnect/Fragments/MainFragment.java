package com.mobilis.FacebookConnect.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.facebook.*;
import com.facebook.widget.LoginButton;
import com.mobilis.FacebookConnect.R;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: ricardonakayama
 * Date: 15/06/13
 * Time: 9:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private UiLifecycleHelper uiHelper;
    private Button queryButton;
    private Button multiQueryButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_android, container, false);

        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));

        queryButton = (Button) view.findViewById(R.id.queryButton);
        multiQueryButton = (Button) view.findViewById(R.id.multiQueryButton);

        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fqlQuery = "SELECT uid, name, pic_square FROM user WHERE uid IN " +
                        "(SELECT uid2 FROM friend WHERE uid1 = me() LIMIT 25)";
                Bundle params = new Bundle();
                params.putString("q", fqlQuery);
                Session session = Session.getActiveSession();
                Request request = new Request(session,
                        "/fql",
                        params,
                        HttpMethod.GET,
                        new Request.Callback(){
                            public void onCompleted(Response response) {
                                Log.i(TAG, "Result: " + response.toString());
                            }
                        });
                Request.executeBatchAsync(request);
            }
        });

        multiQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fqlQuery = "{" +
                        "'friends':'SELECT uid2 FROM friend WHERE uid1 = me() LIMIT 25'," +
                        "'friendinfo':'SELECT uid, name, pic_square FROM user WHERE uid IN " +
                        "(SELECT uid2 FROM #friends)'," +
                        "}";
                Bundle params = new Bundle();
                params.putString("q", fqlQuery);
                Session session = Session.getActiveSession();
                Request request = new Request(session,
                        "/fql",
                        params,
                        HttpMethod.GET,
                        new Request.Callback(){
                            public void onCompleted(Response response) {
                                Log.i(TAG, "Result: " + response.toString());
                            }
                        });
                Request.executeBatchAsync(request);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            queryButton.setVisibility(View.VISIBLE);
            multiQueryButton.setVisibility(View.VISIBLE);
        } else if (state.isClosed()) {
            queryButton.setVisibility(View.INVISIBLE);
            multiQueryButton.setVisibility(View.INVISIBLE);
        }
    }
}
