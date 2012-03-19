//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//------------------------------------------------------------------------------

package com.microsoft.live.sample.hotmail;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveOperationListener;
import com.microsoft.live.sample.LiveSdkSampleApplication;
import com.microsoft.live.sample.R;
import com.microsoft.live.sample.util.JsonKeys;

public class ContactsActivity extends ListActivity {
    private class CreateContactDialog extends Dialog {
        public CreateContactDialog(Context context) {
            super(context);
            setContentView(R.layout.create_contact);

            final EditText firstName = (EditText) findViewById(R.id.firstNameEditText);
            final EditText lastName = (EditText) findViewById(R.id.lastNameEditText);
            final RadioGroup gender = (RadioGroup) findViewById(R.id.genderRadioGroup);

            findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final ProgressDialog progDialog = ProgressDialog.show(ContactsActivity.this,
                                                                          "",
                                                                          "Saving. Please wait...");
                    JSONObject body = new JSONObject();

                    try {
                        body.put(JsonKeys.FIRST_NAME, firstName.getText().toString());
                        body.put(JsonKeys.LAST_NAME, lastName.getText().toString());
                        switch (gender.getCheckedRadioButtonId()) {
                            case R.id.maleRadio: {
                                body.put(JsonKeys.GENDER, "male");
                                break;
                            }
                            case R.id.femaleRadio: {
                                body.put(JsonKeys.GENDER, "female");
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        showToast(e.getMessage());
                        return;
                    }

                    mClient.postAsync("me/contacts", body, new LiveOperationListener() {
                        @Override
                        public void onError(LiveOperationException exception,
                                            LiveOperation operation) {
                            progDialog.dismiss();
                            showToast(exception.getMessage());
                        }

                        @Override
                        public void onComplete(LiveOperation operation) {
                            progDialog.dismiss();

                            JSONObject result = operation.getResult();
                            if (result.has(JsonKeys.ERROR)) {
                                JSONObject error = result.optJSONObject(JsonKeys.ERROR);
                                String message = error.optString(JsonKeys.MESSAGE);
                                String code = error.optString(JsonKeys.CODE);
                                showToast(code + ": " + message);
                            } else {
                                loadContacts();
                                dismiss();
                            }
                        }
                    });
                }
            });
        }
    }

    private class ContactsListAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private final ArrayList<Contact> mContacts;

        public ContactsListAdapter(Context context) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mContacts = new ArrayList<Contact>();
        }

        public ArrayList<Contact> getContacts() {
            return mContacts;
        }

        @Override
        public int getCount() {
            return mContacts.size();
        }

        @Override
        public Contact getItem(int position) {
            return mContacts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView != null ? convertView :
                                           mInflater.inflate(R.layout.view_contacts_list_item,
                                                             parent,
                                                             false);
            TextView name = (TextView) v.findViewById(R.id.nameTextView);
            Contact contact = getItem(position);
            name.setText(contact.getName());

            return v;
        }
    }

    private class ViewContactDialog extends Dialog {
        private final Contact mContact;

        public ViewContactDialog(Context context, Contact contact) {
            super(context);
            assert contact != null;
            mContact = contact;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.view_contact);

            ((TextView) findViewById(R.id.idTextView)).setText("Id: " + mContact.getId());
            ((TextView) findViewById(R.id.nameTextView)).setText("Name: " + mContact.getName());
            ((TextView) findViewById(R.id.genderTextView)).setText("Gender: " + mContact.getGender());
            ((TextView) findViewById(R.id.isFriendTextView)).setText("Is Friend?: " + mContact.getIsFriend());
            ((TextView) findViewById(R.id.isFavoriteTextView)).setText("Is Favorite?: " + mContact.getIsFavorite());
            ((TextView) findViewById(R.id.userIdTextView)).setText("User Id: " + mContact.getUserId());
            ((TextView) findViewById(R.id.updatedTimeTextView)).setText("Updated Time: " + mContact.getUpdatedTime());
        }
    }

    private LiveConnectClient mClient;
    private ContactsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contacts);


        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = (Contact) parent.getItemAtPosition(position);
                ViewContactDialog dialog = new ViewContactDialog(ContactsActivity.this, contact);
                dialog.setOwnerActivity(ContactsActivity.this);
                dialog.show();
            }
        });

        LinearLayout layout = new LinearLayout(this);
        Button newCalendarButton = new Button(this);
        newCalendarButton.setText("New Contact");
        newCalendarButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateContactDialog dialog = new CreateContactDialog(ContactsActivity.this);
                dialog.setOwnerActivity(ContactsActivity.this);
                dialog.show();
            }
        });

        layout.addView(newCalendarButton);
        lv.addHeaderView(layout);

        mAdapter = new ContactsListAdapter(this);
        setListAdapter(mAdapter);

        LiveSdkSampleApplication app = (LiveSdkSampleApplication) getApplication();
        mClient = app.getConnectClient();
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

    @Override
    protected void onStart() {
        super.onStart();
        loadContacts();
    }

    private void loadContacts() {
        final ProgressDialog progDialog =
                ProgressDialog.show(this, "", "Loading. Please wait...", true);

        mClient.getAsync("me/contacts", new LiveOperationListener() {
            @Override
            public void onError(LiveOperationException exception, LiveOperation operation) {
                progDialog.dismiss();
                showToast(exception.getMessage());
            }

            @Override
            public void onComplete(LiveOperation operation) {
                progDialog.dismiss();

                JSONObject result = operation.getResult();
                if (result.has(JsonKeys.ERROR)) {
                    JSONObject error = result.optJSONObject(JsonKeys.ERROR);
                    String message = error.optString(JsonKeys.MESSAGE);
                    String code = error.optString(JsonKeys.CODE);
                    showToast(code + ": " + message);
                    return;
                }

                ArrayList<Contact> contacts = mAdapter.getContacts();
                contacts.clear();

                JSONArray data = result.optJSONArray(JsonKeys.DATA);
                for (int i = 0; i < data.length(); i++) {
                    Contact contact = new Contact(data.optJSONObject(i));
                    contacts.add(contact);
                }

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
