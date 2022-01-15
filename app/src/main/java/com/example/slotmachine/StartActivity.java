package com.example.slotmachine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class StartActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES_AUTH = "AUTH";
    private final String URL = "https://youtube.com";
    private WebView webView;
    private boolean isAuth;
    private boolean authFromFirebase;
    private SharedPreferences prefs;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        isAuth = prefs.getBoolean(APP_PREFERENCES_AUTH, false);

        if (!isAuth) {
            setRemoteConfig();
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {
                                authFromFirebase = mFirebaseRemoteConfig.getBoolean("auth");

                                if (authFromFirebase) {
                                    savePrefs(true);
                                    webView.loadUrl(URL);
                                } else {
                                    startGame();
                                }
                            }
                        }
                    });
        } else webView.loadUrl(URL);
    }

    private void setRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    }

    protected void savePrefs(boolean auth) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(APP_PREFERENCES_AUTH, auth);
        editor.apply();
    }

    protected void startGame() {
        Intent intent = new Intent(getBaseContext(),
                MainActivity.class);
        startActivity(intent);
        finish();
    }
}

