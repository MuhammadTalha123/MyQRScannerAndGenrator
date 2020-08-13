package com.example.myqrscannerandgenrator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button scan;
    private TextView currentCode,userData,info,codeInfo;
    private TimerTask timerTask;
    private String secretCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        scan = findViewById(R.id.scan);
        currentCode = findViewById(R.id.currentCode);
        userData = findViewById(R.id.userData);
        info = findViewById(R.id.info);
        codeInfo = findViewById(R.id.codeInfo);


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String str = result.getContents();
            String[] arrOfStr = str.split("otpauth://totp/");
            str = arrOfStr[1];
            arrOfStr = str.split(":");
            String appName = arrOfStr[0];
            str = arrOfStr[1];
            arrOfStr = str.split("secret=");
            String userEmail = arrOfStr[0];
            userEmail = userEmail.substring(0, userEmail.length() - 1);
            str = arrOfStr[1];
            arrOfStr = str.split("&issuer");
            secretCode = arrOfStr[0];
            String appDetails = appName + "(" + userEmail + ")";

            userData.setText(appDetails);
            userData.setVisibility(View.VISIBLE);
            info.setVisibility(View.VISIBLE);
            codeInfo.setVisibility(View.VISIBLE);
            String otpCode = Utils.getTOTPCode(secretCode);
            startTimer();
            currentCode.setText(otpCode);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startTimer() {
//set a new Timer

        Timer timer = new Timer();
//initialize the TimerTask's job --> change yourView text data

        timerTask = new TimerTask() {
            public void run() {
               updateUI();
            }
        };
//schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 0, 500); //
    }

    private void updateUI () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String code = Utils.getTOTPCode(secretCode);
                currentCode.setText(code);
                currentCode.setVisibility(View.VISIBLE);
            }
        });
    }

}