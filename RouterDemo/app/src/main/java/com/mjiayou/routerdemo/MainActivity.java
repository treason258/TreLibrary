package com.mjiayou.routerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mjiayou.module1.Hello1Utils;
import com.mjiayou.module2.Hello2Utils;

public class MainActivity extends AppCompatActivity {

    private TextView tvHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHello = (TextView) findViewById(R.id.tvHello);

        StringBuilder builder = new StringBuilder();
        builder.append(Hello1Utils.getHelloFromModule2()).append("\n");
        builder.append(Hello2Utils.getHelloFromModule1()).append("\n");
        tvHello.setText(builder.toString());
    }
}
