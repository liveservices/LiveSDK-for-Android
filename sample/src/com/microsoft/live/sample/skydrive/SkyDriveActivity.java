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

package com.microsoft.live.sample.skydrive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveDownloadOperation;
import com.microsoft.live.LiveDownloadOperationListener;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveOperationListener;
import com.microsoft.live.LiveUploadOperationListener;
import com.microsoft.live.sample.LiveSdkSampleApplication;
import com.microsoft.live.sample.R;
import com.microsoft.live.sample.skydrive.SkyDriveObject.Visitor;
import com.microsoft.live.sample.skydrive.SkyDrivePhoto.Image;
import com.microsoft.live.sample.util.FilePicker;
import com.microsoft.live.sample.util.JsonKeys;

public class SkyDriveActivity extends ListActivity {
    private class NewFolderDialog extends Dialog {
        public NewFolderDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.create_folder);
            setTitle("New Folder");

            final EditText name = (EditText) findViewById(R.id.nameEditText);
            final EditText description = (EditText) findViewById(R.id.descriptionEditText);

            findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> folder = new HashMap<String, String>();
                    folder.put(JsonKeys.NAME, name.getText().toString());
                    folder.put(JsonKeys.DESCRIPTION, description.getText().toString());

                    final ProgressDialog progressDialog =
                            showProgressDialog("", "Saving. Please wait...", true);
                    progressDialog.show();

                    mClient.postAsync(mCurrentFolderId,
                                      new JSONObject(folder),
                                      new LiveOperationListener() {
                        @Override
                        public void onError(LiveOperationException exception, LiveOperation operation) {
                            progressDialog.dismiss();
                            showToast(exception.getMessage());
                        }

                        @Override
                        public void onComplete(LiveOperation operation) {
                            progressDialog.dismiss();

                            JSONObject result = operation.getResult();
                            if (result.has(JsonKeys.ERROR)) {
                                JSONObject error = result.optJSONObject(JsonKeys.ERROR);
                                String message = error.optString(JsonKeys.MESSAGE);
                                String code = error.optString(JsonKeys.CODE);
                                showToast(code + ":" + message);
                            } else {
                                dismiss();
                                loadFolder(mCurrentFolderId);
                            }
                        }
                    });
                }
            });

            findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    private class PlayAudioDialog extends Dialog {
        private final SkyDriveAudio mAudio;
        private MediaPlayer mPlayer;
        private TextView mPlayerStatus;

        public PlayAudioDialog(Context context, SkyDriveAudio audio) {
            super(context);
            assert audio != null;
            mAudio = audio;
            mPlayer = new MediaPlayer();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setTitle(mAudio.getName());

            mPlayerStatus = new TextView(getContext());
            mPlayerStatus.setText("Buffering...");
            addContentView(mPlayerStatus,
                           new LayoutParams(LayoutParams.WRAP_CONTENT,
                                            LayoutParams.WRAP_CONTENT));

            mPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mPlayerStatus.setText("Playing...");
                    mPlayer.start();
                }
            });

            mPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mPlayerStatus.setText("Finished playing.");
                }
            });

            try {
                mPlayer.setDataSource(mAudio.getSource());
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.prepareAsync();
            } catch (IllegalArgumentException e) {
                showToast(e.getMessage());
                return;
            } catch (IllegalStateException e) {
                showToast(e.getMessage());
                return;
            } catch (IOException e) {
                showToast(e.getMessage());
                return;
            }
        }

        @Override
        protected void onStop() {
            super.onStop();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * Supported media formats can be found
     * <a href="http://developer.android.com/guide/appendix/media-formats.html">here</a>
     */
    private class PlayVideoDialog extends Dialog {
        private final SkyDriveVideo mVideo;
        private VideoView mVideoHolder;

        public PlayVideoDialog(Context context, SkyDriveVideo video) {
            super(context);
            assert video != null;
            mVideo = video;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setTitle(mVideo.getName());

            mVideoHolder = new VideoView(getContext());
            mVideoHolder.setMediaController(new MediaController(getContext()));
            mVideoHolder.setVideoURI(Uri.parse(mVideo.getSource()));
            addContentView(mVideoHolder,
                           new LayoutParams(LayoutParams.WRAP_CONTENT,
                                            LayoutParams.WRAP_CONTENT));
        }

        @Override
        protected void onStart() {
            super.onStart();
            mVideoHolder.start();
        }
    }

    private class SkyDriveListAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private final ArrayList<SkyDriveObject> mSkyDriveObjs;
        private View mView;

        public SkyDriveListAdapter(Context context) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mSkyDriveObjs = new ArrayList<SkyDriveObject>();
        }

        /**
         * @return The underlying array of the class. If changes are made to this object and you
         * want them to be seen, call {@link #notifyDataSetChanged()}.
         */
        public ArrayList<SkyDriveObject> getSkyDriveObjs() {
            return mSkyDriveObjs;
        }

        @Override
        public int getCount() {
            return mSkyDriveObjs.size();
        }

        @Override
        public SkyDriveObject getItem(int position) {
            return mSkyDriveObjs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // Note: This implementation of the ListAdapter.getView(...) forces a download of thumb-nails when retrieving
        // views, this is not a good solution in regards to CPU time and network band-width.
        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {
            SkyDriveObject skyDriveObj = getItem(position);
            mView = convertView != null ? convertView : null;

            skyDriveObj.accept(new Visitor() {
                @Override
                public void visit(SkyDriveVideo video) {
                    if (mView == null) {
                        mView = inflateNewSkyDriveListItem();
                    }

                    setIcon(R.drawable.video_x_generic);
                    setName(video);
                    setDescription(video);
                }

                @Override
                public void visit(SkyDriveFile file) {
                    if (mView == null) {
                        mView = inflateNewSkyDriveListItem();
                    }

                    setIcon(R.drawable.text_x_preview);
                    setName(file);
                    setDescription(file);
                }

                @Override
                public void visit(SkyDriveFolder folder) {
                    if (mView == null) {
                        mView = inflateNewSkyDriveListItem();
                    }

                    setIcon(R.drawable.folder);
                    setName(folder);
                    setDescription(folder);
                }

                @Override
                public void visit(SkyDrivePhoto photo) {
                    if (mView == null) {
                        mView = inflateNewSkyDriveListItem();
                    }

                    setIcon(R.drawable.image_x_generic);
                    setName(photo);
                    setDescription(photo);

                    // Try to find a smaller/thumbnail and use that source
                    String thumbnailSource = null;
                    String smallSource = null;
                    for (Image image : photo.getImages()) {
                        if (image.getType().equals("small")) {
                            smallSource = image.getSource();
                        } else if (image.getType().equals("thumbnail")) {
                            thumbnailSource = image.getSource();
                        }
                    }

                    String source = thumbnailSource != null ? thumbnailSource :
                                    smallSource != null ? smallSource : null;

                    // if we do not have a thumbnail or small image, just leave.
                    if (source == null) {
                        return;
                    }

                    // Since we are doing async calls and mView is constantly changing,
                    // we need to hold on to this reference.
                    final View v = mView;
                    new AsyncTask<String, Long, Bitmap>() {
                        @Override
                        protected Bitmap doInBackground(String... params) {
                            try {
                                // Download the thumb nail image
                                LiveDownloadOperation operation = mClient.download(params[0]);

                                // Make sure we don't burn up memory for all of
                                // these thumb nails that are transient
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPurgeable = true;
                                return BitmapFactory.decodeStream(operation.getStream(), (Rect)null, options);
                            } catch (Exception e) {
                                showToast(e.getMessage());
                                return null;
                            }
                        }

                        @Override
                        protected void onPostExecute(Bitmap result) {
                            ImageView imgView = (ImageView)v.findViewById(R.id.skyDriveItemIcon);
                            imgView.setImageBitmap(result);
                        }
                    }.execute(source);
                }

                @Override
                public void visit(SkyDriveAlbum album) {
                    if (mView == null) {
                        mView = inflateNewSkyDriveListItem();
                    }

                    setIcon(R.drawable.folder_image);
                    setName(album);
                    setDescription(album);
                }

                @Override
                public void visit(SkyDriveAudio audio) {
                    if (mView == null) {
                        mView = inflateNewSkyDriveListItem();
                    }

                    setIcon(R.drawable.audio_x_generic);
                    setName(audio);
                    setDescription(audio);
                }

                private void setName(SkyDriveObject skyDriveObj) {
                    TextView tv = (TextView) mView.findViewById(R.id.nameTextView);
                    tv.setText(skyDriveObj.getName());
                }

                private void setDescription(SkyDriveObject skyDriveObj) {
                    String description = skyDriveObj.getDescription();
                    if (description == null) {
                        description = "No description.";
                    }

                    TextView tv = (TextView) mView.findViewById(R.id.descriptionTextView);
                    tv.setText(description);
                }

                private View inflateNewSkyDriveListItem() {
                    return mInflater.inflate(R.layout.skydrive_list_item, parent, false);
                }

                private void setIcon(int iconResId) {
                    ImageView img = (ImageView) mView.findViewById(R.id.skyDriveItemIcon);
                    img.setImageResource(iconResId);
                }
            });


            return mView;
        }
    }

    private class ViewPhotoDialog extends Dialog {
        private final SkyDrivePhoto mPhoto;

        public ViewPhotoDialog(Context context, SkyDrivePhoto photo) {
            super(context);
            assert photo != null;
            mPhoto = photo;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setTitle(mPhoto.getName());
            final ImageView imgView = new ImageView(getContext());
            addContentView(imgView,
                           new LayoutParams(LayoutParams.WRAP_CONTENT,
                                            LayoutParams.WRAP_CONTENT));

            mClient.downloadAsync(mPhoto.getSource(), new LiveDownloadOperationListener() {
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
                    new AsyncTask<LiveDownloadOperation, Long, Bitmap>() {
                        @Override
                        protected Bitmap doInBackground(LiveDownloadOperation... params) {
                            return extractScaledBitmap(mPhoto, params[0].getStream());
                        }

                        @Override
                        protected void onPostExecute(Bitmap result) {
                            imgView.setImageBitmap(result);
                        }
                    }.execute(operation);
                }
            });
        }
    }

    public static final String EXTRA_PATH = "path";

    private static final int DIALOG_DOWNLOAD_ID = 0;
    private static final String HOME_FOLDER = "me/skydrive";

    private LiveConnectClient mClient;
    private SkyDriveListAdapter mPhotoAdapter;
    private String mCurrentFolderId;
    private Stack<String> mPrevFolderIds;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FilePicker.PICK_FILE_REQUEST) {
            if (resultCode == RESULT_OK) {
                String filePath = data.getStringExtra(FilePicker.EXTRA_FILE_PATH);

                if (TextUtils.isEmpty(filePath)) {
                    showToast("No file was choosen.");
                    return;
                }

                File file = new File(filePath);

                final ProgressDialog uploadProgressDialog =
                        new ProgressDialog(SkyDriveActivity.this);
                uploadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                uploadProgressDialog.setMessage("Uploading...");
                uploadProgressDialog.setCancelable(true);
                uploadProgressDialog.show();

                final LiveOperation operation =
                        mClient.uploadAsync(mCurrentFolderId,
                                            file.getName(),
                                            file,
                                            new LiveUploadOperationListener() {
                    @Override
                    public void onUploadProgress(int totalBytes,
                                                 int bytesRemaining,
                                                 LiveOperation operation) {
                        int percentCompleted = computePrecentCompleted(totalBytes, bytesRemaining);

                        uploadProgressDialog.setProgress(percentCompleted);
                    }

                    @Override
                    public void onUploadFailed(LiveOperationException exception,
                                               LiveOperation operation) {
                        uploadProgressDialog.dismiss();
                        showToast(exception.getMessage());
                    }

                    @Override
                    public void onUploadCompleted(LiveOperation operation) {
                        uploadProgressDialog.dismiss();

                        JSONObject result = operation.getResult();
                        if (result.has(JsonKeys.ERROR)) {
                            JSONObject error = result.optJSONObject(JsonKeys.ERROR);
                            String message = error.optString(JsonKeys.MESSAGE);
                            String code = error.optString(JsonKeys.CODE);
                            showToast(code + ": " + message);
                            return;
                        }

                        loadFolder(mCurrentFolderId);
                    }
                });

                uploadProgressDialog.setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        operation.cancel();
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skydrive);

        mPrevFolderIds = new Stack<String>();

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SkyDriveObject skyDriveObj = (SkyDriveObject) parent.getItemAtPosition(position);

                skyDriveObj.accept(new Visitor() {
                    @Override
                    public void visit(SkyDriveAlbum album) {
                        mPrevFolderIds.push(mCurrentFolderId);
                        loadFolder(album.getId());
                    }

                    @Override
                    public void visit(SkyDrivePhoto photo) {
                        ViewPhotoDialog dialog =
                                new ViewPhotoDialog(SkyDriveActivity.this, photo);
                        dialog.setOwnerActivity(SkyDriveActivity.this);
                        dialog.show();
                    }

                    @Override
                    public void visit(SkyDriveFolder folder) {
                        mPrevFolderIds.push(mCurrentFolderId);
                        loadFolder(folder.getId());
                    }

                    @Override
                    public void visit(SkyDriveFile file) {
                        Bundle b = new Bundle();
                        b.putString(JsonKeys.ID, file.getId());
                        b.putString(JsonKeys.NAME, file.getName());
                        showDialog(DIALOG_DOWNLOAD_ID, b);
                    }

                    @Override
                    public void visit(SkyDriveVideo video) {
                        PlayVideoDialog dialog = new PlayVideoDialog(SkyDriveActivity.this, video);
                        dialog.setOwnerActivity(SkyDriveActivity.this);
                        dialog.show();
                    }

                    @Override
                    public void visit(SkyDriveAudio audio) {
                        PlayAudioDialog audioDialog =
                                new PlayAudioDialog(SkyDriveActivity.this, audio);
                        audioDialog.show();
                    }
                });
            }
        });

        LinearLayout layout = new LinearLayout(this);
        Button newFolderButton = new Button(this);
        newFolderButton.setText("New Folder");
        newFolderButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NewFolderDialog dialog = new NewFolderDialog(SkyDriveActivity.this);
                dialog.setOwnerActivity(SkyDriveActivity.this);
                dialog.show();
            }
        });

        layout.addView(newFolderButton);

        Button uploadFileButton = new Button(this);
        uploadFileButton.setText("Upload File");
        uploadFileButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FilePicker.class);
                startActivityForResult(intent, FilePicker.PICK_FILE_REQUEST);
            }
        });

        layout.addView(uploadFileButton);
        lv.addHeaderView(layout);

        mPhotoAdapter = new SkyDriveListAdapter(this);
        setListAdapter(mPhotoAdapter);

        LiveSdkSampleApplication app = (LiveSdkSampleApplication) getApplication();
        mClient = app.getConnectClient();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // if prev folders is empty, send the back button to the TabView activity.
            if (mPrevFolderIds.isEmpty()) {
                return false;
            }

            loadFolder(mPrevFolderIds.pop());
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected Dialog onCreateDialog(final int id, final Bundle bundle) {
        Dialog dialog = null;
        switch (id) {
            case DIALOG_DOWNLOAD_ID: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Download")
                       .setMessage("This file will be downloaded to the sdcard.")
                       .setPositiveButton("OK", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog progressDialog =
                                new ProgressDialog(SkyDriveActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setMessage("Downloading...");
                        progressDialog.setCancelable(true);
                        progressDialog.show();

                        String fileId = bundle.getString(JsonKeys.ID);
                        String name = bundle.getString(JsonKeys.NAME);

                        File file = new File(Environment.getExternalStorageDirectory(), name);

                        final LiveDownloadOperation operation =
                                mClient.downloadAsync(fileId + "/content",
                                                      file,
                                                      new LiveDownloadOperationListener() {
                            @Override
                            public void onDownloadProgress(int totalBytes,
                                                           int bytesRemaining,
                                                           LiveDownloadOperation operation) {
                                int percentCompleted =
                                        computePrecentCompleted(totalBytes, bytesRemaining);

                                progressDialog.setProgress(percentCompleted);
                            }

                            @Override
                            public void onDownloadFailed(LiveOperationException exception,
                                                         LiveDownloadOperation operation) {
                                progressDialog.dismiss();
                                showToast(exception.getMessage());
                            }

                            @Override
                            public void onDownloadCompleted(LiveDownloadOperation operation) {
                                progressDialog.dismiss();
                                showToast("File downloaded.");
                            }
                        });

                        progressDialog.setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                operation.cancel();
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new Dialog.OnClickListener() {
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

    @Override
    protected void onStart() {
        super.onStart();
        loadFolder(HOME_FOLDER);
    }

    private void loadFolder(String folderId) {
        assert folderId != null;
        mCurrentFolderId = folderId;

        final ProgressDialog progressDialog =
                ProgressDialog.show(this, "", "Loading. Please wait...", true);

        mClient.getAsync(folderId + "/files", new LiveOperationListener() {
            @Override
            public void onComplete(LiveOperation operation) {
                progressDialog.dismiss();

                JSONObject result = operation.getResult();
                if (result.has(JsonKeys.ERROR)) {
                    JSONObject error = result.optJSONObject(JsonKeys.ERROR);
                    String message = error.optString(JsonKeys.MESSAGE);
                    String code = error.optString(JsonKeys.CODE);
                    showToast(code + ": " + message);
                    return;
                }

                ArrayList<SkyDriveObject> skyDriveObjs = mPhotoAdapter.getSkyDriveObjs();
                skyDriveObjs.clear();

                JSONArray data = result.optJSONArray(JsonKeys.DATA);
                for (int i = 0; i < data.length(); i++) {
                    SkyDriveObject skyDriveObj = SkyDriveObject.create(data.optJSONObject(i));
                    if (skyDriveObj != null) {
                        skyDriveObjs.add(skyDriveObj);
                    }
                }

                mPhotoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(LiveOperationException exception, LiveOperation operation) {
                progressDialog.dismiss();

                showToast(exception.getMessage());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private int computePrecentCompleted(int totalBytes, int bytesRemaining) {
        return (int) (((float)(totalBytes - bytesRemaining)) / totalBytes * 100);
    }

    private ProgressDialog showProgressDialog(String title, String message, boolean indeterminate) {
        return ProgressDialog.show(this, title, message, indeterminate);
    }

    /**
     * Extract a photo from SkyDrive and creates a scaled bitmap according to the device resolution, this is needed to
     * prevent memory over-allocation that can cause some devices to crash when opening high-resolution pictures
     *
     * Note: this method should not be used for downloading photos, only for displaying photos on-screen
     *
     * @param photo The source photo to download
     * @param imageStream The stream that contains the photo
     * @return Scaled bitmap representation of the photo
     * @see http://stackoverflow.com/questions/477572/strange-out-of-memory-issue-while-loading-an-image-to-a-bitmap-object/823966#823966
     */
    private Bitmap extractScaledBitmap(SkyDrivePhoto photo, InputStream imageStream) {
        Display display = getWindowManager().getDefaultDisplay();
        int IMAGE_MAX_SIZE = Math.max(display.getWidth(), display.getHeight());

        int scale = 1;
        if (photo.getHeight() > IMAGE_MAX_SIZE  || photo.getWidth() > IMAGE_MAX_SIZE) {
            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
               (double) Math.max(photo.getHeight(), photo.getWidth())) / Math.log(0.5)));
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = scale;
        return BitmapFactory.decodeStream(imageStream, (Rect)null, options);
    };
}
