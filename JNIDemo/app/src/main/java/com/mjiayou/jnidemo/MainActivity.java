package com.mjiayou.jnidemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvInfo = (TextView) findViewById(R.id.tv_info);

        /**
         * 注意
         * 1、在 Module Settings -> Project Structure -> SDK Location -> Android NDK location 中指定本地NDK路径；
         * 2、gradle.properties 中添加 android.useDeprecatedNdk=true
         * 3、app/build.gradle 中添加
         *  ndk {
                moduleName "testjni" // 生成的so名字
                abiFilters "armeabi", "armeabi-v7a", "x86", "mips" // 输出指定几种abi体系结构下的so库
            }
         */
        mTvInfo.setText(JNIUtil.get().getHello());
    }
}
