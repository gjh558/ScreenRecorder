/*
 * Copyright (c) 2014 Yrom Wang <http://www.yrom.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.yrom.screenrecorder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    private MediaProjectionManager mMediaProjectionManager;
    private ScreenRecorder mRecorder;
    private Button mButton;
    private Button mButtonStop;
    private EditText editTextId;
    private EditText editTextRoomId;
    private EditText editTextIp;

    private String teacherId;
    private String roomId;

    private Scheduler mScheduler;

    private String ip;

    //SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button);
        mButtonStop = (Button)findViewById(R.id.button2);
        mButton.setOnClickListener(this);
        mButtonStop.setOnClickListener(this);

        editTextId = (EditText)findViewById(R.id.editText_id);
        editTextRoomId = (EditText)findViewById(R.id.editText_room);
        editTextIp = (EditText)findViewById(R.id.editTextIp);

//        settings = getSharedPreferences("SCREENSERVER", 0);
//        if (checkPreviousSettings()) {
//            editTextIp.setText(this.ip);
//            editTextId.setText(this.teacherId);
//            editTextRoomId.setText(this.roomId);
//
//        }
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
    }

    public void onStop() {
        super.onStop();
    }
//    private boolean checkPreviousSettings() {
//        this.ip = settings.getString("IP", "");
//        this.teacherId = settings.getString("ID", "");
//        this.roomId = settings.getString("RID", "");
//
//        if (this.ip.length() <= 0 || this.teacherId.length() <= 0 || this.roomId.length() <= 0) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    private void setSettings() {
//        SharedPreferences.Editor editor = settings.edit();
//
//        editor.putString("IP", this.ip);
//        editor.putString("ID", teacherId);
//        editor.putString("RID", roomId);
//        editor.commit();
//    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
        if (mediaProjection == null) {
            Log.e("@@", "media projection is null");
            return;
        }

        // video size
        final int width = 720  ;
        final int height = 1080;
        File file = new File(Environment.getExternalStorageDirectory(),
                "record-" + width + "x" + height + "-" + System.currentTimeMillis() + ".mp4");
        final int bitrate = 1500000;
        mRecorder = new ScreenRecorder(width, height, bitrate, 1, mediaProjection, file.getAbsolutePath());
        mScheduler = new Scheduler(MainActivity.this, mRecorder, ip, teacherId, roomId);
        mScheduler.start();

        //mButton.setText("Stop Recorder");
        //Toast.makeText(this, "Screen recorder is running...", Toast.LENGTH_SHORT).show();
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {
        int Id = v.getId();

        switch (Id) {
            case R.id.button:
                if (mScheduler == null) {
                    teacherId = editTextId.getText().toString().trim();
                    roomId = editTextRoomId.getText().toString().trim();
                    ip = editTextIp.getText().toString().trim();
                    if (teacherId.length() > 0 && roomId.length() > 0 && ip.length() > 0) {

                        Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
                        startActivityForResult(captureIntent, REQUEST_CODE);
                    }else {
                        Toast.makeText(this, "Please input valid ID and IP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Screen recorder is running...", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button2:
                if (mScheduler != null) {
                    mScheduler.Stop();
                    mScheduler = null;
                }
                break;
            default:
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(mScheduler != null){
//            mScheduler.Stop();
//            mScheduler = null;
//        }
    }
}
