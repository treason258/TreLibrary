/**
 * 
 */
package com.haoyang.reader.ui;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.app.base.service.android.AndroidAppService;

/**
 * 
 * 
 * @author tianyu912@yeah.net
 */
public class ImageAdapter extends PagerAdapter {

	private List<String> imagePathList;
	private List<View> imageViewList;
	private AndroidAppService androidAppService;

	public ImageAdapter(List<String> imagePathList, List<View> imageViewList, AndroidAppService androidAppService) {

		this.imagePathList = imagePathList;
		this.imageViewList = imageViewList;
		this.androidAppService = androidAppService;
	}

	@Override
	public int getCount() {
		// 设置成最大，使用户看不到边界
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) { }

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		// 对ViewPager页号求模取出View列表中要显示的项
		position %= imagePathList.size();
		if (position < 0) {
			position = imagePathList.size() + position;
		}

		int imageViewIndex = position % imageViewList.size();

		View view = imageViewList.get(imageViewIndex);
		// 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
		ViewParent vp = view.getParent();
		if (vp != null) {
			ViewGroup parent = (ViewGroup) vp;
			parent.removeView(view);
		}

		int imageShowId = this.androidAppService.getIdResource("imageShow");

		ImageView imageView = (ImageView) view.findViewById(imageShowId);

		String imagePaths = imagePathList.get(position);
		String[] image = imagePaths.split(":");

		if (image.length == 2) {
			String path = image[0];

//			PhysicalFileService pf = new PhysicalFileService(path);
//
//			ZipEntryFileService zipEntryFileService = new ZipEntryFileService(
//					pf, image[1]);
//			InputStream inputStream = null;
//
//			try {
//				inputStream = zipEntryFileService.getInputStream();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			Bitmap bm = BitmapFactory.decodeStream(inputStream);
//			imageView.setImageBitmap(bm);
//			imageView.setScaleType(ScaleType.FIT_CENTER);
		}

		container.addView(view);

		return view;
	}
}