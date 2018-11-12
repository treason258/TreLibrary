package com.mjiayou.routerdemo.router.impl;


import com.mjiayou.routerdemo.ImageLoader;
import com.mjiayou.thirdparty2.router.IImageLoader2Module;

/**
 * Created by treason on 17-7-14.
 */
public class ImageLoader2Impl implements IImageLoader2Module {

    @Override
    public void display() {
        ImageLoader.display();
    }
}
