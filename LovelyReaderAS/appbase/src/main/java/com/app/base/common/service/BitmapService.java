/**
 * 
 */
package com.app.base.common.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;

import com.app.base.common.util.Size;


/**
 * Bitmap相关的服务类。
 * 
 * @author tianyu912@yeah.net
 */
public class BitmapService {

	public final String tag = this.getClass().getName();

	/**
	 * 加载指定的图片资源为Bitmap.
	 * 
	 * @param activity
	 *            当前上下文。
	 * @param pictureResourceId
	 *            图片资源id.
	 * @return
	 */
	public Bitmap loadBitmap(Activity activity, int pictureResourceId) {

		Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),
				pictureResourceId);

		return bitmap;
	}

	/**
	 * 加载指定url的图片，并生成Bitmap对象。
	 * 
	 * @param url
	 *            图片的url.
	 * @return url图片对应的Bitmap.
	 */
	public Bitmap loadBitmap(String url) {

		Bitmap bmp = null;
		try {
			System.out.println("loadBitmap start ");

			InputStream stream = null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			URL imageurl = new URL(url);
			for (int i = 0; i < 10; i++) {
				stream = imageurl.openStream();
				if (stream.available() != 0) {
					System.out.println("loadBitmap success !!!");
					break;
				}

				System.out.println(i + "    loadBitmap fail !!!!!! stream = "
						+ stream.available() + " url " + url);
			} // end for i

			byte[] b = new byte[1 * 1024];
			int len = 0;

			while ((len = stream.read(b, 0, 1024)) != -1) {
				baos.write(b, 0, len);
				baos.flush();
			}
			byte[] bytes = baos.toByteArray();
			Log.i(tag, "baos.toByteArray() = " + bytes.length);
			 
			bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			System.out.println("loadBitmap end !! ");
			return bmp;
		} catch (Exception e) {

			e.printStackTrace();
			return bmp;
		}
	}
	
	public Bitmap loadLocalImage(String path) {

		// 优先从数据库中取
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}

		Bitmap bitmap = null;
		InputStream stream = null;
		try {
			System.out.println("loadLocalImage start !! ");

			stream = new FileInputStream(file);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			System.out.println("file length = " + file.length());
			byte[] b = new byte[(int) file.length()];
			int len = 0;

			while ((len = stream.read(b, 0, 1024)) != -1) {
				baos.write(b, 0, len);
				baos.flush();
			}
			byte[] bytes = baos.toByteArray();

			Options options = new Options();
			options.inDither = false; /* 不进行图片抖动处理 */
			options.inPreferredConfig = null; /* 设置让解码器以最佳方式解码 */
			// options.inSampleSize=4; /*图片长宽方向缩小倍数*/

			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
					options);
			if (bitmap == null) {
				System.out.println("loadLocalImage bitmap = " + bitmap + " filePath = " + file.getAbsoluteFile());
			}
			System.out.println("loadLocalImage end !! ");
			return bitmap;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 获取图片尺寸。
	 * @param path
	 * @param wh 期望的尺寸。
	 * @return 图片进行缩放后的尺寸。
	 */
	public ImageThumbnail getImageSize(String path, Size wh) {
		
		ImageThumbnail imageThumbnail = new ImageThumbnail();
		File file = new File(path);
		
		if (!file.exists()) {
			
			imageThumbnail.thumbnailSize.height = -1;
			imageThumbnail.thumbnailSize.width = -1;
			
			return imageThumbnail;
		}
		
		Options options = new Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		// BitmapFactory.decodeStream(is, outPadding, opts).decodeFile(path, options); // 此时返回bm为空

		// 计算缩放比
		int h = (int) (options.outHeight / wh.height);
		if (h <= 0) {
			h = 1;
		}

		int w = (int) (options.outWidth / wh.width);
		if (w <= 0) {
			w = 1;
		}

		int max = Math.max(w, h);

		options = new Options();
		options.inSampleSize = max;
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, options);
		
		imageThumbnail.thumbnailSize.height = options.outHeight;
		imageThumbnail.thumbnailSize.width = options.outWidth;
		
		return imageThumbnail;
	}
	
	/**
	 * 保存bitmap到指定的本地文件中。
	 * 
	 * @param bitmap 
	 * @param filePath
	 *            保存的本地文件所在的路径
	 */
	public boolean saveBitmapToFile(Bitmap bitmap, String filePath)
			throws IOException {

		File file = new File(filePath);
		return saveBitmapToFile(bitmap, file);
	}
	
	/**
	 * 保存bitmap到指定的本地文件中。
	 * 
	 * @param bitmap 
	 * @param file 保存的本地文件对象
	 * @return true:保存成功;false:保存失败.
	 */
	public boolean saveBitmapToFile(Bitmap bitmap, File file)
			throws IOException {

		if (file.exists()) {
			file.delete();
		}

		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		try {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			return true;
		} finally {
			if (fOut != null) {
				fOut.close();
			}
		} // end finally
	}

	/**
	 * 读取按指定的位置旋转后的图片Bitmap。
	 * 
	 * @param point
	 *            指定的位置
	 * @param degree
	 *            旋转的角度。
	 * @return
	 */
	public Bitmap rotateBitmap(Point point, float degree, Bitmap bitmap) {
		Matrix matrix = new Matrix();

		matrix.preRotate(degree, point.x, point.y);// 这个方法是旋转的另一个方法，此方法后面还有两个参数，这两个参数是按照给定的x
		// y点进行旋转，按照本例，应该是以图片正中间的底部为圆心旋转图片
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);// 使用矩阵来构建旋转后的图片
		return bitmap;
	}

	/**
	 * 回收bitmap.
	 * 
	 * @param bitmap
	 */
	public void recycle(Bitmap bitmap) {

		if (bitmap != null) {
			bitmap.recycle();
		}
	}

}
