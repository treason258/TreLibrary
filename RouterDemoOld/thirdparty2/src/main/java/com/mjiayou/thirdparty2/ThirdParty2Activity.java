package com.mjiayou.thirdparty2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mjiayou.thirdparty2.router.impl.ThirdParty2ModuleImpl;

/**
 * Created by treason on 2017/11/22.
 */

public class ThirdParty2Activity extends AppCompatActivity {

    private Context mContext;
    private Button mBtnDisplayThirdParty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_2);
        mContext = this;

        mBtnDisplayThirdParty = (Button) findViewById(R.id.btn_display_third_party_2);
        mBtnDisplayThirdParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThirdParty2ModuleImpl.getImageLoader().display();
            }
        });
    }
}