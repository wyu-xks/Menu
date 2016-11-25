package eegsmart.jnitest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, JNIUtils.sleepalgo(4, new char[]{'a', 'b', 'c', 'd'}), Toast.LENGTH_SHORT).show();
        Log.e("MainActivity", JNIUtils.sleepalgo(4, new char[]{'a', 'b', 'c', 'd'}));
    }
}
