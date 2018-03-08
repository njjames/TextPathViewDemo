package com.nj.textpathviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SyncTextPathView syncTextPathView = findViewById(R.id.synctextpathview);

        syncTextPathView.setText("牛逼");
        syncTextPathView.initTextPath();

    }
}
