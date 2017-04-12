package com.megvii.livenesslib.util;

import android.util.Log;

import com.megvii.livenessdetection.DetectionFrame;
import com.megvii.livenessdetection.Detector;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 文件工具类
 */
public class IFile {

	private final String TAG = IFile.class.getSimpleName();

	public IFile() {
	}

	/**
	 * 把图片保存到文件夹
	 */
	public boolean save(Detector mDetector, String session,
						JSONObject jsonObject) {

		Log.i(TAG, "save");
		List<DetectionFrame> frames = mDetector.getValidFrame();
		if (frames.size() == 0) {
			Log.i(TAG, "frames.size() == 0");
			Log.i(TAG, "return false");
			return false;
		}

		try {
			String dirPath = Constant.dirName + "/" + session;
			Log.i(TAG, "dirPath -> " + dirPath);
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			for (int i = 0; i < frames.size(); i++) {
				File file = new File(dir, session + "-" + i + ".jpg");
 				FileOutputStream fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(frames.get(i).getCroppedFaceImageData());
//				JSONArray jsonArray = jsonObject.getJSONArray("imgs");
//				jsonArray.put(file.getAbsoluteFile());
				fileOutputStream.flush();
				fileOutputStream.close();
				Log.i(TAG, "i -> " + i + " | file.getAbsolutePath() -> " + file.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, e.toString());
			Log.i(TAG, "return false");
			return false;
		}
		Log.i(TAG, "return true");
		return true;
	}

	/**
	 * 把LOG保存到本地
	 */
	public boolean saveLog(String session, String name) {
		try {
			String dirPath = Constant.dirName + "/" + session;
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, "Log.txt");
			FileOutputStream fileOutputStream = new FileOutputStream(file, true);
			String str = "\n" + session + ",  " + name;
			fileOutputStream.write(str.getBytes());
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
