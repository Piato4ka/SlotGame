package com.example.slotmachine;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class StartActivity extends AppCompatActivity {
    private final String URL = "https://youtube.com";
    private WebView webView;
    private boolean auth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        webView = findViewById(R.id.webView);


        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);


        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            auth = mFirebaseRemoteConfig.getBoolean("auth");

                            if (auth) {
                                webView = findViewById(R.id.webView);
                                webView.getSettings().setJavaScriptEnabled(true);
                                webView.loadUrl(URL);

                            } else {
                                Intent intent = new Intent(getBaseContext(),
                                        MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    }


                });
    }
}

