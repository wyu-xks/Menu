package eegsmart.hellofromjni;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Jni.sleepAlgo(4,new char[]{'a','b','c','d'});
        Toast.makeText(this, Jni.sleepAlgo(4,new char[]{'a','b','c','d'}), Toast.LENGTH_SHORT).show();
        Log.e("MainActivity", Jni.sleepAlgo(4,new char[]{'a','b','c','d'}));
    }
}
