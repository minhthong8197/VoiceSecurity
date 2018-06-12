package com.itute.tranphieu.voicesecurity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itute.tranphieu.voicesecurity.APIRquest.API;
import com.itute.tranphieu.voicesecurity.APIRquest.StatusClass;
import com.itute.tranphieu.voicesecurity.Record.Recorder;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageButton btnRecord, btnStop;
    ImageButton btnNew;
    ProgressBar pbTime;
    CountDownTimer timer;
    TextView txtTime;
    final long RECORD_TIME = 30000L;
    boolean recording = false;
    Intent intentRegister;
    Intent intentUser;
    String filePathName;
    final Recorder recorder = new Recorder();
    ArrayList<String> userName = new ArrayList<>();
    ArrayList<String> userId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Xin quyen he thong
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET}, 123);
        AnhXa();
        getData();
        setOnButtonClick();
        intentRegister = new Intent(MainActivity.this, RegisterActivity.class);
        intentUser = new Intent(MainActivity.this, UserActivity.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setButtonStatus();
    }

    private void setOnButtonClick() {
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recording = true;
                setButtonStatus();
                pbTime.setMax((int) RECORD_TIME);

                timer = new CountDownTimer(RECORD_TIME, 10) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        pbTime.setProgress((int) (millisUntilFinished));
                        txtTime.setText(String.valueOf(millisUntilFinished / 1000));
                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(MainActivity.this, "Record finished", Toast.LENGTH_SHORT).show();
                        recording = false;
                        setButtonStatus();
                        //
                        KiemTraXacThuc();
                        //
                    }
                }.start();
                //
                recorder.startRecording();
                //
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                Toast.makeText(MainActivity.this, "Record finished", Toast.LENGTH_SHORT).show();
                recording = false;
                setButtonStatus();
                //
                Toast.makeText(MainActivity.this, "analyzing", Toast.LENGTH_SHORT).show();
                KiemTraXacThuc();
                //
            }
        });
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentRegister);
            }
        });
    }

    private void getData() {
        Intent intent = getIntent();
        try {
            Bundle bundle = intent.getBundleExtra("BUNDLE");
            userName.add(bundle.getString("name"));
            userId.add(bundle.getString("id"));
            Log.d("\nMainAvtivity", bundle.toString());
        } catch (Exception e) {
            Log.d("\nMainAvtivity", "getData failed");
        }
    }

    void setButtonStatus() {
        if (recording) {
            btnRecord.setVisibility(View.INVISIBLE);
            btnStop.setVisibility(View.VISIBLE);
            pbTime.setVisibility(View.VISIBLE);
            txtTime.setVisibility(View.VISIBLE);
            btnNew.setVisibility(View.INVISIBLE);
        } else {
            btnStop.setVisibility(View.INVISIBLE);
            btnRecord.setVisibility(View.VISIBLE);
            pbTime.setVisibility(View.INVISIBLE);
            txtTime.setVisibility(View.INVISIBLE);
            btnNew.setVisibility(View.VISIBLE);
        }
    }

    private void AnhXa() {
        btnRecord = (ImageButton) findViewById(R.id.button_Record);
        btnStop = (ImageButton) findViewById(R.id.button_Stop);
        pbTime = (ProgressBar) findViewById(R.id.ProgressBarTime);
        btnNew = (ImageButton) findViewById(R.id.ImageButtonNew);
        txtTime = (TextView) findViewById(R.id.TextViewTimer);
    }

    private void KiemTraXacThuc() {
        filePathName = recorder.stopRecording();
        Log.d("\t\t\t", filePathName);
        //String status = "";
        String allProfile = API.getAllProfile();
        Log.d("\t\t\tMain", "Sau khi getAllProfile\n" + allProfile);
        String linkStatus = API.identification(filePathName, allProfile);
        Log.d("\t\t\tMain", "Sau khi identification\n" + linkStatus);
        StatusClass status = API.getOperationStatus(linkStatus);
        Log.d("\t\t\tMain", "Sau khi getOperationStatus\n" + status.toString());

        if (status.getStatusSucceeded() == false) {
            //neu get status bi failed thi in ra tin nhan
            Toast.makeText(MainActivity.this, status.getFailedMessage(), Toast.LENGTH_SHORT).show();
        } else if (status.getStatusSucceeded() == true && status.getEnrolling() == false) {
            if (status.getIdentifiedProfileId().equals("00000000-0000-0000-0000-000000000000")) {
                Toast.makeText(MainActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
            } else {
                String identifiedProfileId = status.getIdentifiedProfileId();
                for (int i = 0; i < userId.size(); i++) {
                    if (identifiedProfileId.equals(userId.get(i))) {
                        Bundle bundle = new Bundle();
                        bundle.putString("name", userName.get(i));
                        bundle.putString("id", identifiedProfileId);
                        intentUser.putExtra("USER", bundle);
                        startActivity(intentUser);
                        return;
                    }
                }
                //e.printStackTrace();
                Bundle bundle = new Bundle();
//                if (identifiedProfileId.equals("8eeddc4f-536a-439d-8582-b0a0fd0a3214")) {
//                    bundle.putString("name", "Minh Thông");
//                } else
                    bundle.putString("name", "Không tìm thấy tên trong hệ thống");
                bundle.putString("id", identifiedProfileId);
                intentUser.putExtra("USER", bundle);
                startActivity(intentUser);
            }
        }
    }
}

