package com.eegsmart.changcolor.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.eegsmart.changcolor.R;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();



    private TextView boom;
    private TextView slidemenu;
    private TextView prtlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boom = (TextView) findViewById(R.id.boom);
        slidemenu = (TextView) findViewById(R.id.slidemenu);
        prtlist = (TextView) findViewById(R.id.prtlist);

        boom.setOnClickListener(this);
        slidemenu.setOnClickListener(this);
        prtlist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.boom:
                startActivity(new Intent(MainActivity.this, BoomActivity.class));
                break;
            case R.id.slidemenu:
                startActivity(new Intent(MainActivity.this, SlideActivity.class));
                break;
            case R.id.prtlist:
                startActivity(new Intent(MainActivity.this, PrtListActivity.class));
                break;
        }
    }
}
