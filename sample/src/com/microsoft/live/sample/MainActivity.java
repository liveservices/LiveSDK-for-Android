// ------------------------------------------------------------------------------
// Copyright (c) 2014 Microsoft Corporation
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.
// ------------------------------------------------------------------------------

package com.microsoft.live.sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TabHost;
import android.widget.Toast;

import com.microsoft.live.LiveAuthClient;
import com.microsoft.live.LiveAuthException;
import com.microsoft.live.LiveAuthListener;
import com.microsoft.live.LiveConnectSession;
import com.microsoft.live.LiveStatus;
import com.microsoft.live.sample.hotmail.ContactsActivity;
import com.microsoft.live.sample.identity.ViewProfileActivity;
import com.microsoft.live.sample.skydrive.SkyDriveActivity;

public class MainActivity extends TabActivity {
    private static final int DIALOG_LOGOUT_ID = 0;

    private LiveAuthClient mAuthClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LiveSdkSampleApplication app = (LiveSdkSampleApplication) getApplication();
        mAuthClient = app.getAuthClient();

        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent(this, ViewProfileActivity.class);
        spec = tabHost.newTabSpec("profile").setIndicator("Profile").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent(this, ContactsActivity.class);
        spec = tabHost.newTabSpec("contacts").setIndicator("Contacts").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent(this, SkyDriveActivity.class);
        spec = tabHost.newTabSpec("skydrive").setIndicator("SkyDrive").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent(this, ExplorerActivity.class);
        spec = tabHost.newTabSpec("explorer").setIndicator("Explorer").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDialog(DIALOG_LOGOUT_ID);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        Dialog dialog = null;
        switch (id) {
            case DIALOG_LOGOUT_ID: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Logout")
                       .setMessage("The Live Connect Session will be cleared.")
                       .setPositiveButton("OK", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuthClient.logout(new LiveAuthListener() {
                            @Override
                            public void onAuthError(LiveAuthException exception, Object userState) {
                                showToast(exception.getMessage());
                            }

                            @Override
                            public void onAuthComplete(LiveStatus status,
                                                       LiveConnectSession session,
                                                       Object userState) {
                                LiveSdkSampleApplication app =
                                        (LiveSdkSampleApplication)getApplication();
                                app.setSession(null);
                                app.setConnectClient(null);
                                finish();
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog = builder.create();
                break;
            }
        }

        if (dialog != null) {
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    removeDialog(id);
                }
            });
        }

        return dialog;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
