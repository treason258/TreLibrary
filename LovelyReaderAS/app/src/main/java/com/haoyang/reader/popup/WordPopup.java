/**
 * 
 */
package com.haoyang.reader.popup;

import java.util.List;

import java.io.InputStream;

import android.graphics.Color;

import android.util.Log;
import android.view.View;

import android.widget.RelativeLayout;
import android.widget.LinearLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import android.animation.Animator;
import android.animation.ObjectAnimator;

import com.java.common.service.StringService;

import com.app.base.service.android.AssetService;
import com.app.base.service.android.AndroidAppService;
import com.app.base.service.android.SharedPreferenceService;

import com.app.base.common.util.Size;

import com.app.base.ui.AnimatorDefaultListener;

import com.haoyang.reader.common.BitmapService;

import com.haoyang.reader.sdk.ColorHandler;
import com.haoyang.reader.sdk.LineSpace;
import com.haoyang.reader.sdk.ReaderPageStyle;


/**
 * 文字样式设置。
 * 
 * @author tianyu912@yeah.net
 */
public class WordPopup extends PopupPanel {

	public final static String ID = "WordPopup";

	/**
	 * 行间距处理.
	 */
	private int lineSpaceNoneId;
	private int lineSpaceSmallId;
	private int lineSpaceTooBigId;
	private int lineSpaceBigId;
	private int lineSpaceStandId;

	/**
	 * 亮度调节.
	 */
	private int light_set_sliderId;

	/**
	 * 文字字号.
	 */
	private int toSmallId;
	private int toLargeId;

	/**
	 * 文字字体.
	 */
	private int typefaceId;

	// 分页动画
//	private int translationId;
//	private int simulationId;
//	private int rollId;
//	private int noneAnimationId;

	private int wordSetOutHYId;
	private int wordMengId;

	private int lightBrightId;
	private int lightDarkId;

	private SeekBar seekBar;

	private int wordSizeSetId;
	private int lineSpaceSetId;

	private AndroidAppService androidAppService;

	public WordPopup(ReaderPopupService readerPopupService) {

		super(ID, readerPopupService);
	}

	public void initUI(RelativeLayout relativeLayout) {

		if (windowView != null && activity == windowView.getContext()) {
			return;
		}

		this.androidAppService = new AndroidAppService(activity);

		int panel_menuLayout = this.androidAppService.getLayoutResource("hy_panel_word");		// 加载文字设置布局
		activity.getLayoutInflater().inflate(panel_menuLayout, relativeLayout);

		int word_panel = this.androidAppService.getIdResource("word_panelHY");				// 加载文字设置panel
		this.windowView = relativeLayout.findViewById(word_panel);

		this.createBackgroundChildLinearLayout();													// 创建切换背景的入口

		lightBrightId = this.androidAppService.getIdResource("lightBright");					// 亮度调节
		lightDarkId = this.androidAppService.getIdResource("lightDark");

		light_set_sliderId = this.androidAppService.getIdResource("light_set_sliderHY");

		lineSpaceNoneId = this.androidAppService.getIdResource("lineSpaceNoneHY");			// 行间距
		lineSpaceSmallId = this.androidAppService.getIdResource("lineSpaceSmallHY");
		lineSpaceBigId = this.androidAppService.getIdResource("lineSpaceBigHY");
		lineSpaceTooBigId = this.androidAppService.getIdResource("lineSpaceTooBigHY");

		lineSpaceStandId = this.androidAppService.getIdResource("lineSpaceStandHY");

		toSmallId = this.androidAppService.getIdResource("toSmallHY");						// 字号
		toLargeId = this.androidAppService.getIdResource("toLargeHY");

		typefaceId = this.androidAppService.getIdResource("typefaceHY");						// 字体设置入口

//		translationId = this.androidAppService.getIdResource("translation");						// 翻页动画
//		simulationId = this.androidAppService.getIdResource("simulation");
//		rollId = this.androidAppService.getIdResource("roll");
//		noneAnimationId = this.androidAppService.getIdResource("noneAnimation");

		wordMengId = this.androidAppService.getIdResource("wordMengHY");
		setupButton(wordMengId, windowView);
		wordSetOutHYId = this.androidAppService.getIdResource("wordSetOutHY");

		wordSizeSetId = this.androidAppService.getIdResource("wordSizeSetHY");
		lineSpaceSetId = this.androidAppService.getIdResource("lineSpaceSetHY");

		// -------------------------------------------------------------------------------------------------------

		setupButton(wordSetOutHYId, windowView);

		setupButton(lightBrightId, windowView);
		setupButton(lightDarkId, windowView);

//		setupButton(translationId, windowView);
//		setupButton(simulationId, windowView);
//		setupButton(rollId, windowView);
//		setupButton(noneAnimationId, windowView);

		setupButton(toSmallId, windowView);
		setupButton(toLargeId, windowView);
		setupButton(typefaceId, windowView);

		setupButton(lineSpaceNoneId, windowView);
		setupButton(lineSpaceSmallId, windowView);
		setupButton(lineSpaceBigId, windowView);
		setupButton(lineSpaceTooBigId, windowView);
		setupButton(lineSpaceStandId, windowView);

		// 显示当前数据  ------------------------------------------------------------------------------------------

		showTextSize();

		this.updateWordSetting();

		this.initScrreenBrightnessUI();
	}

	private void updateWordSetting() {

		this.activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				if (windowView == null) {
					return;
				}

				View view = windowView.findViewById(wordSizeSetId);
				view.setVisibility(View.VISIBLE);

				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view
						.getLayoutParams();

				view = windowView.findViewById(lineSpaceSetId);
				view.setVisibility(View.VISIBLE);

				int[] lineSpaceIds = { lineSpaceNoneId, lineSpaceSmallId,
						lineSpaceBigId, lineSpaceTooBigId, lineSpaceStandId };

				arrage(windowView, "lineSpaceSetHY", lineSpaceIds);

				LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) view
						.getLayoutParams(); // 取控件textView当前的布局参数
				linearParams.height = lp.height;// 控件的高强制设成20

				view.setLayoutParams(linearParams); //
			}
		});
	}

	private void initScrreenBrightnessUI() {

		// 亮度调节
		final View seekBarView = windowView.findViewById(light_set_sliderId);
		seekBarView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		seekBar = (SeekBar) seekBarView;

		int scrreenBrightness = configServiceSDK.getScreenBrightness();

		seekBar.setMax(100);
		seekBar.setProgress(scrreenBrightness);

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
										  boolean fromUser) {

				configServiceSDK.setScreenBrightness(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
	}


//	private void showCurrentPageAnimation() {
//
//		AnimationType animationType = configServiceSDK.getAnimationType(AnimationType.SlideAnimation);
//
//		switch (animationType) {
//		case NoneAnimation:
//
//			setCurrentPageAnimation(noneAnimationId);
//			break;
//		case CurlAnimation:
//
//			setCurrentPageAnimation(simulationId);
//			break;
//		case SlideAnimation:
//
//			setCurrentPageAnimation(translationId);
//			break;
//		case ScrollAnimation:
//
//			setCurrentPageAnimation(rollId);
//			break;
//		}
//	}

//	private void setCurrentPageAnimation(int viewId) {
//
//		int[] ids = { translationId, rollId, noneAnimationId, simulationId };
//		String[] nameHight = { "trans_highlight", "scroll_highlight",
//				"click_highlight", "simulation_highlight" };
//		String[] name = { "trans", "scroll", "click", "simulation" };
//
//		for (int i = 0; i < ids.length; i++) {
//
//			final ImageView view = windowView.findViewById(ids[i]);
//			if (view == null) {
//				continue;
//			}
//			if (ids[i] == viewId) {
//
//				int highLightId = androidAppService.getDrawableResource(nameHight[i]);
//				view.setImageResource(highLightId);
//				continue;
//			}
//
//			int normalId = androidAppService.getDrawableResource(name[i]);
//			view.setImageResource(normalId);
//		}
//	}

	// // 测试版 ---------------------------------------------------------
	// private void showCurrentLineSpace() {
	//
	// int lineSpace = ConfigServiceHandler.Instance().getLineSpace();
	// LineSpace lineSpaceEnum = LineSpace.valueOf(lineSpace);
	//
	// switch (lineSpaceEnum) {
	// case lineSpaceMiddle:
	// setCurrentLineSpace(lineSpaceStandId);
	// break;
	// }
	// }

	// 正式版 ---------------------------------------------------------
	private void showCurrentLineSpace() {

		int lineSpace = configServiceSDK.getLineSpace();

		Log.d("showCurrentLineSpace", " lineSpace vaue : " + lineSpace);

		LineSpace lineSpaceVaue = LineSpace.valueOf(lineSpace);

		switch (lineSpaceVaue) {
		case lineSpaceNone:

			setCurrentLineSpace(lineSpaceNoneId);
			break;
		case lineSpaceSmall:

			setCurrentLineSpace(lineSpaceSmallId);
			break;
		case lineSpaceMiddle:

			setCurrentLineSpace(lineSpaceStandId);
			break;
		case lineSpaceBig:

			setCurrentLineSpace(lineSpaceBigId);

			break;
		case lineSpaceTooBig:

			setCurrentLineSpace(lineSpaceTooBigId);
			break;
		}
	}

	private void setCurrentLineSpace(int viewId) {

		int[] lineSpaceIds = { lineSpaceNoneId, lineSpaceSmallId,
				lineSpaceBigId, lineSpaceTooBigId, lineSpaceStandId };

		String[] name = { "line_space_five", "line_space_four",
				"line_space_three", "line_space_second", "line_space_stand" };

		String[] nameHight = { "line_space_five_selected",
				"line_space_four_selected", "line_space_three_selected",
				"line_space_second_selected", "line_space_stand_selected" };

		for (int i = 0; i < lineSpaceIds.length; i++) {

			final ImageView view = (ImageView) windowView.findViewById(lineSpaceIds[i]);
			if (view == null) {
				continue;
			}
			if (lineSpaceIds[i] == viewId) {

				Log.d("setCurrentLineSpace", " nameHight[i]; :   " +  nameHight[i]);

				int highLightId = androidAppService.getDrawableResource(nameHight[i]);
				view.setImageResource(highLightId);

				continue;
			}

			int normalId = androidAppService.getDrawableResource(name[i]);
			view.setImageResource(normalId);
		}
	}

	private ObjectAnimator wordAnimator;
	private WordAnimatorListener wordAnimatorListener = new WordAnimatorListener();

	@Override
	public void hide() {

		if (wordAnimator != null && wordAnimator.isRunning()
				&& wordAnimator.isStarted()) { // 动画正在运行时不能处理。
			return;
		}

		final View view = windowView.findViewById(wordSetOutHYId);

		int screenHeight = this.configServiceSDK.getScreenHeight();

		wordAnimator = ObjectAnimator.ofFloat(view, "Y", screenHeight);
		wordAnimator.addListener(wordAnimatorListener);
		wordAnimator.setDuration(300);
		wordAnimator.start();
	}

	@Override
	public void show() {

		this.windowView.setVisibility(View.VISIBLE);

		this.relativeLayout.bringChildToFront(this.windowView);

		// this.showCurrentPageAnimation(); 														// 当前翻页动画的按钮高亮显示。
		// int[] ids = { noneAnimationId, simulationId, translationId, rollId };
		// arrage(windowView, "pageAnimation", ids);

		this.showCurrentLineSpace();

		int[] lineSpaceIds = { lineSpaceNoneId, lineSpaceSmallId,
				lineSpaceBigId, lineSpaceTooBigId, lineSpaceStandId };

		arrage(windowView, "lineSpaceSetHY", lineSpaceIds);

		// 菜单显示的时候，子菜单不显示。
		final View view = windowView.findViewById(wordSetOutHYId);
		view.bringToFront();

		// 显示动画处理
		int height = view.getHeight();

		int screenHeight = this.configServiceSDK.getScreenHeight();

		view.setY(screenHeight);
		wordAnimator = ObjectAnimator.ofFloat(view, "Y", screenHeight - height);
		wordAnimator.setDuration(300);
		wordAnimator.start();
	}

	private ReaderPageStyle currentReaderPageStyle;
	private ImageView currentImageView;

	private Size backgroundBitmapSize = new Size(80, 80);

	/**
	 * 背景切换入口View创建.
	 */
	private void createBackgroundChildLinearLayout() {

		int pageBackgrondContainerId = this.androidAppService.getIdResource("pageBackgrondContainer");

		LinearLayout linear = (LinearLayout) this.windowView.findViewById(pageBackgrondContainerId);

		final AssetService assetService = new AssetService(this.activity);

		List<ReaderPageStyle> pageStyleList = configServiceSDK.getReaderPageStyles();

		ReaderPageStyle pageStyle = configServiceSDK.getCurrentReaderPageStyle();

		int size = pageStyleList.size();
		for (int i = 0; i < size; i++) {

			LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
																				LinearLayout.LayoutParams.WRAP_CONTENT);
			linearLp.leftMargin = 0;
			linearLp.rightMargin = 50;

			LinearLayout myLinear = new LinearLayout(this.activity);
			myLinear.setOrientation(LinearLayout.VERTICAL);

			linear.addView(myLinear, linearLp);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
																			LinearLayout.LayoutParams.WRAP_CONTENT);

			ImageView imageView = new ImageView(this.activity);

			ReaderPageStyle readerPageStyle = pageStyleList.get(i);

			boolean isCurrent = false;

			if (pageStyle.styleName.equals(readerPageStyle.styleName)) {

				currentReaderPageStyle = readerPageStyle;
				currentImageView = imageView;

				isCurrent = true;
			}

			Bitmap background = makeBackgroundBitmap(isCurrent, assetService,
					readerPageStyle);

			imageView.setImageBitmap(background);

			imageView.setTag(readerPageStyle);
			myLinear.addView(imageView, lp);

			imageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					Bitmap background = makeBackgroundBitmap(false,
															 assetService,
															 currentReaderPageStyle);

					currentImageView.setImageBitmap(background);

					// =============================================

					ReaderPageStyle readerPageStyle = (ReaderPageStyle) (v.getTag());

					configServiceSDK.setCurrentReaderPageStyle(readerPageStyle);

					currentReaderPageStyle = readerPageStyle;
					currentImageView = (ImageView) v;

					final SharedPreferenceService preference = new SharedPreferenceService(activity);
					// 根据用户选的样式来设置当前是白天模式还是夜间模式。
				    if (readerPageStyle.nightDayMode == ReaderPageStyle.NightDayMode.night) {

				    	preference.putValue(MenuPopup.nightDayModeKey, false);

				    } else {

				    	preference.putValue(MenuPopup.nightDayModeKey, true);
				    }

					background = makeBackgroundBitmap(true,
													   assetService,
													   readerPageStyle);

				    currentImageView.setImageBitmap(background);

					readerDynamicSDK.repaint();

				}
			});
		}
	}

	private Bitmap makeBackgroundBitmap(boolean isCurrent,
										AssetService assetService,
										ReaderPageStyle readerPageStyle) {
		Bitmap background;

		if (readerPageStyle.backgroundMode != ReaderPageStyle.BackgroundMode.backgroundColor) {

			InputStream is = assetService.getAssertFileInputStream(readerPageStyle.backgroundValue);

			Bitmap wallpaper = null;
			if (is != null) {

				wallpaper = BitmapFactory.decodeStream(is);
			}

			if (isCurrent) {

				background = BitmapService.createRoundConerImage(10,
																 backgroundBitmapSize,
																 wallpaper,
																true,
																 Color.YELLOW,
																6);
			} else {
				background = BitmapService.createRoundConerImage(10,
																  backgroundBitmapSize,
																  wallpaper);
			}
		} else {

			StringService stringService = new StringService();

			int result = stringService.stringToInt(readerPageStyle.backgroundValue);

			ColorHandler colorHandler = new ColorHandler(result);

			int color = ColorHandler.rgb(colorHandler);
			if (isCurrent) {

				background = BitmapService.createRoundConerColor(10,
																 backgroundBitmapSize,
																 color,
																true,
																 Color.YELLOW,
																6);
			} else {
				background = BitmapService.createRoundConerColor(10,
																 backgroundBitmapSize,
																 color);
			}
		}

		return background;
	}


	/**
	 * 当前界面上各个功能按钮的处理事件.
	 * @param view
	 */
	public void onClick(View view) {

		int id = view.getId();
		if (id == wordMengId) {

			this.hide();
			return;
		} else if (id == wordSetOutHYId) {

			return;
		}
		else if (id == this.lightBrightId) {

			int scrreenBrightness = this.configServiceSDK.getScreenBrightness();
			if (scrreenBrightness + 3 <= 100) {

				this.seekBar.setProgress(scrreenBrightness + 3);
				this.configServiceSDK.setScreenBrightness(scrreenBrightness + 3);
			} else {

				this.seekBar.setProgress(100);
				this.configServiceSDK.setScreenBrightness(100);
			}

		} else if (id == this.lightDarkId) {

			int scrreenBrightness = this.configServiceSDK.getScreenBrightness();

			if (scrreenBrightness - 3 >= 0) {

				this.seekBar.setProgress(scrreenBrightness - 3);
				this.configServiceSDK.setScreenBrightness(scrreenBrightness - 3);
			} else {

				this.seekBar.setProgress(10);
				this.configServiceSDK.setScreenBrightness(10);
			}
		} else if (id == this.lineSpaceNoneId) { // 行间距

			this.configServiceSDK.setLineSpace(LineSpace.lineSpaceNone.getValue());
			this.readerDynamicSDK.updateConfig();

			this.showCurrentLineSpace();

		} else if (id == this.lineSpaceSmallId) {

			this.configServiceSDK.setLineSpace(LineSpace.lineSpaceSmall.getValue());
			this.readerDynamicSDK.updateConfig();

			this.showCurrentLineSpace();

		} else if (id == this.lineSpaceBigId) {

			this.configServiceSDK.setLineSpace(LineSpace.lineSpaceBig.getValue());
			this.readerDynamicSDK.updateConfig();

			this.showCurrentLineSpace();

		} else if (id == this.lineSpaceTooBigId) {

			this.configServiceSDK.setLineSpace(LineSpace.lineSpaceTooBig.getValue());
			this.readerDynamicSDK.updateConfig();

			this.showCurrentLineSpace();

		} else if (id == this.lineSpaceStandId) {

			this.configServiceSDK.setLineSpace(LineSpace.lineSpaceMiddle.getValue());
			this.readerDynamicSDK.updateConfig();

			this.showCurrentLineSpace();

		} else if (id == this.typefaceId) { 														// 进入切换字体界面

		} else if (id == this.toSmallId) { 															// 文字缩小

			int textSize = this.configServiceSDK.getTextSize();

			textSize = textSize - 1;
			if (textSize <= 9) { 																	// 最小字号是10

				textSize = 10;
				showTextSize();

				return;
			}

			showTextSize(); 																		// 当前字号更新到界面上.

			// 开始变更配置
			this.configServiceSDK.setTextSize(textSize);
			this.readerDynamicSDK.updateConfig();

		} else if (id == this.toLargeId) { 															// 文字放大

			int textSize = this.configServiceSDK.getTextSize();

			textSize = textSize + 1;
			if (textSize >= 81) { 																	// 最大字号是80

				textSize = 80;

				showTextSize();

				return;
			}

			showTextSize(); // 当前字号更新到界面上.

			// 开始变更配置
			this.configServiceSDK.setTextSize(textSize);
			this.readerDynamicSDK.updateConfig();
		}
	}

	private void showTextSize() {

		if (windowView == null) {
			return;
		}

		int wordSizeShowId = this.androidAppService.getIdResource("wordSizeShowHY");

		TextView textSizeShow = (TextView) windowView.findViewById(wordSizeShowId);

		int textSize = configServiceSDK.getTextSize();

		textSizeShow.setText(String.valueOf(textSize));
	}

	private void arrage(View rootView, String containerId, int[] ids) {

		int pageAnimationId = androidAppService.getIdResource(containerId);
		View wordSetAreaView = windowView.findViewById(pageAnimationId);

		int width = wordSetAreaView.getWidth();
		int height = wordSetAreaView.getHeight();

		View[] views = new View[ids.length];
		for (int index = 0; index < ids.length; index++) {
			int id = ids[index];
			View view = rootView.findViewById(id);
			views[index] = view;
		}

		int total = 0, x = 0;
		for (int i = 0; i < views.length; i++) { // 计算所有View总共的宽度。
			View v = views[i];
			total += v.getWidth();
		}

		int widthPer = (width - total) / (views.length - 1); // 计算每个view之间的间距
		for (int i = 0; i < views.length; i++) { // 设置各个View的位置。

			View v = views[i];
			int h = v.getHeight();
			int startY = (height - h) / 2;

			v.setX(x);
			v.setY(startY);

			x += widthPer + v.getWidth();
		} // end for i
	}

	class WordAnimatorListener extends AnimatorDefaultListener {

		public void onAnimationEnd(Animator animation) {

			if (windowView == null) {
				return;
			}

			windowView.setVisibility(View.INVISIBLE);
			setPosition(-10000, -10000);
		}
	}

}