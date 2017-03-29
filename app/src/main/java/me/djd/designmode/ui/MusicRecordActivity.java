package me.djd.designmode.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import me.djd.designmode.R;
import me.djd.designmode.base.BaseActivity;

/**
 * Created by dujiande on 2017/3/27.
 */

public class MusicRecordActivity extends BaseActivity {

    private String TAG = getClass().getSimpleName();

    @BindView(R.id.button3)
    Button btnStart;

    private static final String endStr = "结束录制";
    private static final String startStr = "开始录制";


    AudioRecord mRecord = null;
    boolean mReqStop = false;
    private final int kSampleRate = 44100;
    private final int kChannelMode = AudioFormat.CHANNEL_IN_STEREO;
    private final int kEncodeFormat = AudioFormat.ENCODING_PCM_16BIT;

    private final int kFrameSize = 2048;
    private String filePath = "/sdcard/voice.pcm";

    String[] permissionArr = {
            Manifest.permission.CAPTURE_AUDIO_OUTPUT,
            Manifest.permission.CAPTURE_VIDEO_OUTPUT,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MOUNT_FORMAT_FILESYSTEMS
    };

    public static void appJump(Context context) {
        Intent intent = new Intent(context, MusicRecordActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int setRootView() {
        return R.layout.activity_music_record;
    }

    @Override
    public void initData() {
        btnStart.setText(startStr);

    }

    @Override
    public void initListener() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(btnStart.getText().toString().equals(endStr)){
                   endRecord();
               }else{
                   if(checkIfHasPermisions(permissionArr)){
                       startRecord();
                   }else{
                       requestPermision(permissionArr);
                   }
               }

            }
        });

    }

    public void grantedPermissionCallBack(){
        startRecord();
    }

    private void endRecord() {
        mReqStop = true;
        btnStart.setText(startStr);
    }

    private void startRecord() {
        btnStart.setText(endStr);
        (new Thread() {
            @Override
            public void run() {
                record();
            }
        }).start();
    }


    private void record() {
        mReqStop = false;
        FileOutputStream os = null;
        int minBufferSize = AudioRecord.getMinBufferSize(kSampleRate, kChannelMode,
                kEncodeFormat);
        mRecord = new AudioRecord(MediaRecorder.AudioSource.REMOTE_SUBMIX,
                kSampleRate, kChannelMode, kEncodeFormat, minBufferSize * 2);
        mRecord.startRecording();
        try {
            os = new FileOutputStream(filePath);
            byte[] buffer = new byte[kFrameSize];
            int num = 0;
            while (!mReqStop) {
                num = mRecord.read(buffer, 0, kFrameSize);
                Log.d(TAG, "buffer = " + buffer.toString() + ", num = " + num);
                os.write(buffer, 0, num);
            }

            Log.d(TAG, "exit loop");
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Dump PCM to file failed");
        }
        mRecord.stop();
        mRecord.release();
        mRecord = null;
        Log.d(TAG, "clean up");
    }


}
