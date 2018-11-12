package com.mjiayou.routerdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mjiayou.thirdparty.ThirdPartyActivity;
import com.mjiayou.thirdparty2.ThirdParty2Activity;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private Button mBtnThirdParty;
    private Button mBtnThirdParty2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mBtnThirdParty = (Button) findViewById(R.id.btn_third_party);
        mBtnThirdParty2 = (Button) findViewById(R.id.btn_third_party_2);

        mBtnThirdParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ThirdPartyActivity.class));
            }
        });

        mBtnThirdParty2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ThirdParty2Activity.class));
            }
        });
    }
}
