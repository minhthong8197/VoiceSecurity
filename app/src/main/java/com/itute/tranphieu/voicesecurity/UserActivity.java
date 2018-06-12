package com.itute.tranphieu.voicesecurity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {


    TextView txtWelcome, txtUserName;
    String name = "", id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        txtUserName = findViewById(R.id.txt_username);
        txtWelcome = findViewById(R.id.txt_welcome);

        getData();
        txtWelcome.setText("Welcome\n" + id);
        txtUserName.setText(name);
        //Toast.makeText(UserActivity.this, id, Toast.LENGTH_SHORT).show();
    }

    private void getData() {
        Intent intent = getIntent();
        try {
            Bundle bundle = intent.getBundleExtra("USER");
            name = bundle.getString("name");
            id = bundle.getString("id");
            Log.d("\nUserAvtivity" ,bundle.toString());
        } catch (Exception e) {
            Log.d("\nUserAvtivity" ,"getData failed");
        }
    }
}
