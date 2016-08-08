package com.michaelyzm.daddytale.activity;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.michaelyzm.daddytale.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhoyu on 8/8/2016.
 */
public class RecordActivity extends Activity {

    final String DEFAULT_BOOK_NAME = "DADDYTALE_";
    EditText mFileNameEdit;
    TextView mTimer;
    TextView mStartButton;
    TextView mStopButton;
    MediaRecorder mMediaRecorder;
    Timer timer;
    long startTime = 0;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        mTimer = (TextView)findViewById(R.id.activity_record_timer);
        mStartButton = (TextView)findViewById(R.id.activity_record_start_button);
        mStopButton = (TextView)findViewById(R.id.activity_record_stop_button);
        mFileNameEdit = (EditText)findViewById(R.id.activity_record_title);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mFileNameEdit.getText().toString().trim()))
                {
                    Toast.makeText(RecordActivity.this, "Please input valid book name before recording", Toast.LENGTH_LONG).show();
                    return;
                }
                mStartButton.setVisibility(View.GONE);
                mStopButton.setVisibility(View.VISIBLE);
                String filename = getFileName();
                mMediaRecorder = new MediaRecorder();
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mMediaRecorder.setOutputFile(filename);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                try{
                    mMediaRecorder.prepare();
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                mMediaRecorder.start();
                startTime = System.currentTimeMillis();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mTimer.setText(transferTimeConsumingToString(System.currentTimeMillis() - startTime));
                            }
                        });
                    }
                }, 200, 200);

            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStopButton.setVisibility(View.GONE);
                mStartButton.setVisibility(View.VISIBLE);
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mMediaRecorder = null;
                timer.cancel();
                timer = null;
                startTime = 0;
                mTimer.setText("00:00:00");
            }
        });
    }

    private String getFileName()
    {
        String filename = mFileNameEdit.getText().toString();
        if(TextUtils.isEmpty(filename))
        {
            filename = DEFAULT_BOOK_NAME;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
            filename = filename + dateFormat.format(new Date(System.currentTimeMillis()));
        }
        if(getExternalCacheDir() != null) {
            filename = getExternalCacheDir().getAbsolutePath() + File.separator + filename + ".3gp";
        }
        else
        {
            filename = getCacheDir().getAbsolutePath() + File.separator + filename + ".3gp";
        }
        return filename;
    }

    private String transferTimeConsumingToString(long timeConsuming)
    {
        timeConsuming /= 1000;
        long seconds = timeConsuming % 60;
        timeConsuming /= 60;
        long minutes = timeConsuming % 60;
        timeConsuming /= 60;
        long hours = timeConsuming;
        String result = ((hours > 9)?(hours+""):("0"+hours)) + ":" + ((minutes > 9)?(minutes+""):("0"+minutes)) +":" + ((seconds > 9)?(seconds+""):("0"+seconds));
        return result;
    }
}
