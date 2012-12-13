package com.microsoft.live.sample.identity;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.live.LiveAuthClient;
import com.microsoft.live.LiveAuthException;
import com.microsoft.live.LiveAuthListener;
import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveConnectSession;
import com.microsoft.live.LiveDownloadOperation;
import com.microsoft.live.LiveDownloadOperationListener;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveOperationListener;
import com.microsoft.live.LiveStatus;
import com.microsoft.live.sample.LiveSdkSampleApplication;
import com.microsoft.live.sample.R;
import com.microsoft.live.sample.util.JsonKeys;

public class ViewProfileActivity extends Activity {

    private class DownloadProfilePictureAsyncTask extends AsyncTask<LiveDownloadOperation, Void, BitmapDrawable> {
        @Override
        protected BitmapDrawable doInBackground(LiveDownloadOperation... params) {
            return new BitmapDrawable(getResources(), params[0].getStream());
        }

        @Override
        protected void onPostExecute(BitmapDrawable profilePicture) {
            mNameTextView.setCompoundDrawablesWithIntrinsicBounds(profilePicture,
                                                                  null,
                                                                  null,
                                                                  null);
        }
    }

    private TextView mNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        mNameTextView = (TextView)findViewById(R.id.nameTextView);

        final LiveSdkSampleApplication app = (LiveSdkSampleApplication)getApplication();

        findViewById(R.id.signOutButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveAuthClient authClient = app.getAuthClient();
                authClient.logout(new LiveAuthListener() {
                    @Override
                    public void onAuthError(LiveAuthException exception, Object userState) {
                        showToast(exception.getMessage());
                    }

                    @Override
                    public void onAuthComplete(LiveStatus status,
                                               LiveConnectSession session,
                                               Object userState) {
                        app.setSession(null);
                        app.setConnectClient(null);
                        getParent().finish();
                    }
                });
            }
        });


        final LiveConnectClient connectClient = app.getConnectClient();
        connectClient.getAsync("me", new LiveOperationListener() {
            @Override
            public void onError(LiveOperationException exception, LiveOperation operation) {
                showToast(exception.getMessage());
            }

            @Override
            public void onComplete(LiveOperation operation) {
                JSONObject result = operation.getResult();

                if (result.has(JsonKeys.ERROR)) {
                    JSONObject error = result.optJSONObject(JsonKeys.ERROR);
                    String code = error.optString(JsonKeys.CODE);
                    String message = error.optString(JsonKeys.MESSAGE);

                    showToast(code + ": " + message);
                } else {
                    User user = new User(result);
                    mNameTextView.setText("Hello, " + user.getName() + "!");
                }
            }
        });

        connectClient.getAsync("me/picture", new LiveOperationListener() {
            @Override
            public void onError(LiveOperationException exception, LiveOperation operation) {
                showToast(exception.getMessage());
            }

            @Override
            public void onComplete(LiveOperation operation) {
                JSONObject result = operation.getResult();

                if (result.has(JsonKeys.ERROR)) {
                    JSONObject error = result.optJSONObject(JsonKeys.ERROR);
                    String code = error.optString(JsonKeys.CODE);
                    String message = error.optString(JsonKeys.MESSAGE);
                    showToast(code + ": " + message);
                    return;
                }

                String location = result.optString(JsonKeys.LOCATION);
                connectClient.downloadAsync(location, new LiveDownloadOperationListener() {
                    @Override
                    public void onDownloadProgress(int totalBytes,
                                                   int bytesRemaining,
                                                   LiveDownloadOperation operation) {
                    }

                    @Override
                    public void onDownloadFailed(LiveOperationException exception,
                                                 LiveDownloadOperation operation) {
                        showToast(exception.getMessage());
                    }

                    @Override
                    public void onDownloadCompleted(LiveDownloadOperation operation) {
                        DownloadProfilePictureAsyncTask task =
                                new DownloadProfilePictureAsyncTask();
                        task.execute(operation);
                    }
                });
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Since this activity is part of a TabView we want to send
        // the back button to the TabView activity.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
