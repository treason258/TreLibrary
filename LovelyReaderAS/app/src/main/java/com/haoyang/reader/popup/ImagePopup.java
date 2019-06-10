/**
 * 
 */
package com.haoyang.reader.popup;

import android.view.View;
import android.widget.RelativeLayout;

import com.app.base.service.android.AndroidAppService;

import com.haoyang.reader.service.text.struct.entity.ElementArea;

/**
 * 功能菜单：退出、意见反馈、笔记、目录等等。
 * 
 * @author tianyu912@yeah.net
 */
public class ImagePopup extends PopupPanel implements View.OnClickListener {

	public final static String ID = "ImagePopup";

	private AndroidAppService androidAppService;

	private int imagePanelId;

	private int imageTopId;
	private int imageBottomId;
	private int imageShowId;

	public ElementArea elementArea;

	public ImagePopup(ReaderPopupService readerPopupService) {
		super(ID, readerPopupService);

		androidAppService = new AndroidAppService(activity);
	}

	public void initUI(RelativeLayout relativeLayout) {

		if (this.windowView != null && activity == windowView.getContext()) {
			return;
		}

//		int panelImageLayout = androidAppService
//				.getLayoutResource("panel_image");
//
//		// 加载弹出的选择框布局。
//		activity.getLayoutInflater().inflate(panelImageLayout, root);
//
//		imagePanelId = androidAppService.getIdResource("image_panelHY");
//
//		myWindow = (SimplePopupWindow) root.findViewById(imagePanelId);
//
//		setPosition(-10000, -10000);
//
//		// imageTopId = androidAppService.getIdResource("imageTop");
//		// imageBottomId = androidAppService.getIdResource("imageBottom");
//
//		imageShowId = androidAppService.getIdResource("imagePreview");
//
//		// PhotoView photoView = (PhotoView) root.findViewById(imageShowId);
//
//		setupButton(imagePanelId);
//		setupButton(imageShowId);
//		// setupButton(imageBottomId);
	}

	@Override
	public void hide() {


//		ReaderService.Instance().setCurrentShowImagePath(null);
//		ReaderService.Instance().getReaderUIInterface().getReadAreaInterface()
//				.repaint();
	}



	// fromXDelta,fromYDelta 起始时X，Y座标,屏幕右下角的座标是X:320,Y:480
	// toXDelta， toYDelta 动画结束时X,Y的座标 --> <!--
	// interpolator 指定动画插入器
	// 常见的有加速减速插入器 accelerate_decelerate_interpolator
	// 加速插入器 accelerate_interpolator，
	// 减速插入器 decelerate_interpolator。
	// fromXScale,fromYScale， 动画开始前X,Y的缩放，0.0为不显示， 1.0为正常大小
	// toXScale，toYScale， 动画最终缩放的倍数， 1.0为正常大小，大于1.0放大
	// pivotX， pivotY 动画起始位置，相对于屏幕的百分比,两个都为50%表示动画从屏幕中间开始
	// startOffset， 动画多次执行的间隔时间，如果只执行一次，执行前会暂停这段时间，
	// 单位毫秒 duration，一次动画效果消耗的时间，单位毫秒，
	// 值越小动画速度越快 repeatCount，动画重复的计数，动画将会执行该值+1次
	// repeatMode，动画重复的模式，reverse为反向，当第偶次执行时，动画方向会相反。
	// restart为重新执行，方向不变 -->

	@Override
	public void show() {
		//super.show_();

		//this.show();
	}

//	private void show() {
//
//		if (elementArea == null) {
//			return;
//		}
//
//		TextImageElement imageElement = (TextImageElement) (elementArea.textElement);
//		if (imageElement.path == null) {
//			return;
//		}
//		// PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
//		// photoView.setImageResource(R.drawable.image);
//		ImageView imageView = (ImageView) myWindow.findViewById(imageShowId);
//		// PhotoView imageView = (PhotoView) myWindow.findViewById(imageShowId);
//
//		PhysicalFileService pf = new PhysicalFileService(imageElement.path);
//
//		ZipEntryFileService zipEntryFileService = new ZipEntryFileService(pf,
//				imageElement.fileName);
//		InputStream inputStream = null;
//
//		try {
//			inputStream = zipEntryFileService.getInputStream();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		if (inputStream == null) {
//			return;
//		}
//
//		Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//		if (bitmap == null || bitmap.isRecycled()) {
//			return;
//		}
//
//		float width = bitmap.getWidth();
//		float height = bitmap.getHeight();
//
//		ReadAreaConfig rc = ConfigServiceHandler.Instance().getReadAreaConfig();
//
//		if (width < rc.readWidth) {
//
//			float c = rc.readWidth / width;
//			height = height * c;
//			width = rc.readWidth;
//		}
//
//		if (height > rc.readHeight) {
//
//			float c = rc.readHeight / height;
//			width = width * c;
//			height = rc.readHeight;
//		}
//
//		bitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height,
//				true);
//
//		imageView.setImageBitmap(bitmap);
//
//		// Size imageSize = ImageSizeService.getImageSize(imageElement.path);
//		//
//		// if (imageSize.width == rc.readWidth
//		// || imageSize.height == rc.readHeight) {
//		// return;
//		// }
//
//		AnimationSet animationSet = new AnimationSet(true);
//
//		float y = (elementArea.YEnd - elementArea.YStart) / height;
//		float x = (elementArea.XEnd - elementArea.XStart) / width;
//		if (x > 1.0f) {
//			x = 1.0f;
//		}
//		if (y > 1.0f) {
//			y = 1.0f;
//		}
//
//		int screenHeight = ConfigServiceHandler.Instance().getReadAreaConfig().screenHeight;
//		float yCenter = elementArea.YStart
//				+ (elementArea.YEnd - elementArea.YStart) / 2;
//		float v = yCenter / screenHeight;
//		ScaleAnimation scaleAnimation = new ScaleAnimation(x, 1, y, 1,
//				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, v);
//
//		animationSet.addAnimation(scaleAnimation);
//		animationSet.setDuration(500);
//		imageView.startAnimation(animationSet);
//	}

	public void onClick(View view) {

//		int id = view.getId();
//
//		if (id == imageShowId) {
//			return;
//		} else if (id == imagePanelId) {
//
//			this.readerPopupService.hideActivePopup();
//			return;
//		}
	}

}