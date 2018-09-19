/**
 * 
 */
package com.app.base.service.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * 
 * @author tianyu912@yeah.net
 */
public class AssetService {

	Context context;

	public AssetService(Context context) {
		this.context = context;
	}

	public InputStream getAssertFileInputStream(String fileName) {

		AssetManager assetManager = context.getAssets();
		try {
			return assetManager.open(fileName);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * 进行IO流读写
	 * 
	 * @param inputStream
	 *            txt文件流
	 */
	public String read(InputStream inputStream) {

		try {
			ByteArrayOutputStream oStream = new ByteArrayOutputStream();
			int length;
			while ((length = inputStream.read()) != -1) {
				oStream.write(length);
			}
			return oStream.toString();
		} catch (IOException e) {
			return "读写失败";
		}
	}

}
