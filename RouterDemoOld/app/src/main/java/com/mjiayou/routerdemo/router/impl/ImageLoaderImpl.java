package com.mjiayou.routerdemo.router.impl;


import com.mjiayou.routerdemo.ImageLoader;
import com.mjiayou.thirdparty.router.IImageLoaderModule;

/**
 * Created by treason on 17-7-14.
 */
public class ImageLoaderImpl implements IImageLoaderModule {

    @Override
    public void display() {
        ImageLoader.display();
    }
}
