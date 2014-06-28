//------------------------------------------------------------------------------
// Copyright 2014 Microsoft Corporation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//------------------------------------------------------------------------------

package com.microsoft.live.sample.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.microsoft.live.sample.R;

public class FilePicker extends ListActivity {
    private class FilePickerListAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private final ArrayList<File> mFiles;

        public FilePickerListAdapter(Context context) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mFiles = new ArrayList<File>();
        }

        public ArrayList<File> getFiles() {
            return mFiles;
        }

        @Override
        public int getCount() {
            return mFiles.size();
        }

        @Override
        public File getItem(int position) {
            return mFiles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView != null ? convertView :
                                           mInflater.inflate(R.layout.file_picker_list_item,
                                                             parent,
                                                             false);
            TextView name = (TextView) v.findViewById(R.id.nameTextView);
            TextView type = (TextView) v.findViewById(R.id.typeTextView);

            File file = getItem(position);
            name.setText(file.getName());
            type.setText(file.isDirectory() ? "Folder" : "File");

            return v;
        }
    }

    public static final int PICK_FILE_REQUEST = 0;
    public static final String EXTRA_FILE_PATH = "filePath";

    private File mCurrentFolder;
    private Stack<File> mPrevFolders;
    private FilePickerListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_picker);

        mPrevFolders = new Stack<File>();

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = (File) parent.getItemAtPosition(position);

                if (file.isDirectory()) {
                    mPrevFolders.push(mCurrentFolder);
                    loadFolder(file);
                } else {
                    Intent data = new Intent();
                    data.putExtra(EXTRA_FILE_PATH, file.getPath());
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            }
        });

        mAdapter = new FilePickerListAdapter(FilePicker.this);
        setListAdapter(mAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !mPrevFolders.isEmpty()) {
            File folder = mPrevFolders.pop();
            loadFolder(folder);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadFolder(Environment.getExternalStorageDirectory());
    }

    private void loadFolder(File folder) {
        assert folder.isDirectory();
        setTitle(folder.getName());

        mCurrentFolder = folder;

        ProgressDialog progressDialog =
                ProgressDialog.show(this, "", "Loading. Please wait...", true);
        ArrayList<File> adapterFiles = mAdapter.getFiles();
        adapterFiles.clear();
        adapterFiles.addAll(Arrays.asList(folder.listFiles()));
        mAdapter.notifyDataSetChanged();

        progressDialog.dismiss();
    }
}
