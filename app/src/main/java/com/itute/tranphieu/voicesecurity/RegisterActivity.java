package com.itute.tranphieu.voicesecurity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itute.tranphieu.voicesecurity.APIRquest.API;
import com.itute.tranphieu.voicesecurity.APIRquest.StatusClass;
import com.itute.tranphieu.voicesecurity.Record.Recorder;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    ImageButton btnRecord, btnStop;
    ProgressBar pbTime;
    CountDownTimer timer;
    TextView txtTime;
    EditText edtName;
    final long RECORD_TIME = 30000L;
    long need_time, ran_time = 0;
    boolean recording = false, nonProfile = true;
    final Recorder recorder = new Recorder();
    String filePahtName = "", identificationProfileId = "";
    Intent mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
        AnhXa();
        setOnButtonClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setButtonStatus();
        need_time = RECORD_TIME;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setButtonStatus();
        nonProfile = false;
        need_time = RECORD_TIME;
    }


    private void setOnButtonClick() {
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recording = true;
                setButtonStatus();
                pbTime.setMax((int) need_time);
                setTimer();
                //Create enroll
                recorder.startRecording();

                /*if (need_time == RECORD_TIME) //the first time
                {
                    pbTime.setMax((int) need_time);
                    setTimer();
                    //Create enroll
                    recorder.startRecording();
                    //
                } else {
                    pbTime.setMax((int) need_time);
                    setTimer();
                    //Update enroll
                    recorder.startRecording();
                    //
                }*/
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //need_time = need_time - ran_time;
                timer.cancel();
                recording = false;
                setButtonStatus();
//                if (need_time > 0) {
//                    Toast.makeText(RegisterActivity.this, "You need continue record " + need_time / 1000 + "s", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(RegisterActivity.this, "Recording finished", Toast.LENGTH_SHORT).show();
//                }
                //
                filePahtName = recorder.stopRecording();
                if (nonProfile) createProfile();
                createEnrollment();
                //
            }
        });
    }

    private void setTimer() {
        timer = new CountDownTimer(need_time, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                pbTime.setProgress((int) (millisUntilFinished));
                txtTime.setText(String.valueOf(millisUntilFinished / 1000));

                if (millisUntilFinished >= 0) {
                    ran_time = need_time - millisUntilFinished;
                }
            }

            @Override
            public void onFinish() {
//                ran_time=RECORD_TIME;
                //need_time = need_time - ran_time;
                //Toast.makeText(RegisterActivity.this, "Recording finished", Toast.LENGTH_SHORT).show();
                recording = false;
                setButtonStatus();
                //
                filePahtName = recorder.stopRecording();
                if (nonProfile) createProfile();
                createEnrollment();
                //
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            timer.cancel();
        } catch (Exception e) {

        }
    }

    private void setButtonStatus() {
        if (recording) {
            btnRecord.setVisibility(View.INVISIBLE);
            btnStop.setVisibility(View.VISIBLE);
            pbTime.setVisibility(View.VISIBLE);
            txtTime.setVisibility(View.VISIBLE);
            edtName.setEnabled(false);
        } else {
            btnStop.setVisibility(View.INVISIBLE);
            btnRecord.setVisibility(View.VISIBLE);
            pbTime.setVisibility(View.INVISIBLE);
            txtTime.setVisibility(View.INVISIBLE);
            edtName.setEnabled(true);
        }
    }

    private void AnhXa() {
        btnRecord = (ImageButton) findViewById(R.id.button_Record);
        btnStop = (ImageButton) findViewById(R.id.button_Stop);
        pbTime = (ProgressBar) findViewById(R.id.ProgressBarTime);
        txtTime = (TextView) findViewById(R.id.TextViewTimer);
        edtName = (EditText) findViewById(R.id.EditTextName);
    }

    private void createProfile() {
        identificationProfileId = API.createProfile();
        Log.d("\nCREATE PROFILE ", identificationProfileId);
        nonProfile = false;
    }

    private void createEnrollment() {
        String linkStatus = API.createEnrollment(filePahtName, identificationProfileId);
        Log.d("\nLINK STATUS", linkStatus);
        if (linkStatus.equals("")) {
            Toast.makeText(RegisterActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            return;
        }
        StatusClass status = API.getOperationStatus(linkStatus);
        Log.d("\t\t\tSTATUS", status.toString());
        if(status.getStatusSucceeded() == true && status.getEnrolling() == true){
            //neu trang thai la enrolling thanh cong
            Double remainingTime = status.getRemainingEnrollmentSpeechTime();
            Log.d("\nREMAINING TIME ", remainingTime.toString());
            need_time = Math.round(remainingTime + 0.49) * 1000;
            Log.d("\nNEED TIME ", need_time + "");

            if (need_time > 0) {
                if (need_time < 7000) need_time = 7000;
                ran_time = 0;
                Toast.makeText(RegisterActivity.this, "Please speak " + remainingTime + " seconds more", Toast.LENGTH_SHORT).show();
            } else if (need_time == 0.0) {
                //xoa di nhung profile con chua hoan thanh
                ArrayList<String> needDeleteProfile = API.getNeedDeleteProfile();
                for (int i = 0; i < needDeleteProfile.size(); i++) {
                    API.deleteProfile(needDeleteProfile.get(i));
                }
                //tro lai man hinh dang nhap
                Bundle bundle = new Bundle();
                bundle.putString("id", identificationProfileId);
                bundle.putString("name", edtName.getText().toString());
                mainActivity.putExtra("BUNDLE",bundle);
                startActivity(mainActivity);
            }
        } else {
            //Log.d("\nFAILED STATUS",status.toString());
            //Toast.makeText(RegisterActivity.this, "Failed status", Toast.LENGTH_SHORT).show();
        }
    }
}
