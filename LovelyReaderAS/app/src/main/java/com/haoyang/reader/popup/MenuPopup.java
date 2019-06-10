/**
 * 
 */
package com.haoyang.reader.popup;

import java.util.List;

import android.animation.Animator;
import android.animation.ObjectAnimator;

import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.app.base.service.android.AndroidAppService;
import com.app.base.service.android.SharedPreferenceService;

import com.app.base.ui.AnimatorDefaultListener;

import com.haoyang.reader.sdk.BookCatalog;
import com.haoyang.reader.sdk.BookStress;
import com.haoyang.reader.sdk.ReaderPageStyle;

/**
 * 功能菜单：文字设置、笔记列表、目录等等。
 * 
 * @author tianyu912@yeah.net
 */
public class MenuPopup extends PopupPanel {

	public final static String ID = "MenuPopup";

	public static final String nightDayModeKey = "nightDayMode";

	private AndroidAppService androidAppService;

	private int menuId;

	/**
	 * 顶部工具栏.
	 */
	private int toolsBarId;

	private int toolsBarBackId;
	private int toolsBarBackImageId;

	private int nightDaySwitchId;

	/**
	 * 章节处理
	 */
	private SeekBar chapterProgressSeekBar = null;

	private int chapter_progress_sliderId;
	private int preChapterId;
	private int nextChapterId;

	private int chapterProgressPanelId;

	private int chapterNameId;
	private int pageIndexId;

	private int catalogIndex;		 	// 用来记当前目录的索引, 这个属性主要是点上一章下一章用来进行增加或减少的索引.

	/**
	 * 主菜单.
	 */
	private int catalogAllId;			// 目录

	private int moreAllId;				// 子目录
	private int wordAllId;				// 文字处理.

	/**
	 * 子菜单.
	 */
	private int addBookmarkId;
	private int cancelBookmarkId;

	private int bookmarkListId;
	private int noteListId;

	private int exitId;

	private List<BookCatalog> bookCatalogList = null;

	public MenuPopup(ReaderPopupService readerPopupService) {

		super(ID, readerPopupService);
	}

	public void setBookCatalogList(List<BookCatalog> bookCatalogList) {

		this.bookCatalogList = bookCatalogList;
	}

	public void initUI(final RelativeLayout relativeLayout) {

		if (this.windowView != null ) {
			return;
		}

		this.androidAppService = new AndroidAppService(activity);

		int panel_menuLayout = this.androidAppService.getLayoutResource("hy_panel_menu");			// 获取布局
		this.activity.getLayoutInflater().inflate(panel_menuLayout, relativeLayout);

		int menu_panel = this.androidAppService.getIdResource("menu_panelHY");					// 获取当前panel对象
		this.windowView = relativeLayout.findViewById(menu_panel);

		menuId = this.androidAppService.getIdResource("menuHY"); // 菜单区id

		// 主菜单
		catalogAllId = this.androidAppService.getIdResource("catalogAllHY");
		wordAllId = this.androidAppService.getIdResource("wordAllHY");
		moreAllId = this.androidAppService.getIdResource("moreAllHY");

		// 夜间和白天两种模式切换。
		nightDaySwitchId = this.androidAppService.getIdResource("nightDaySwitchHY");

		// 子菜单
		addBookmarkId = this.androidAppService.getIdResource("addBookMarkHY");
		cancelBookmarkId = this.androidAppService.getIdResource("cancelBookMarkHY");
		bookmarkListId = this.androidAppService.getIdResource("bookMarkListHY");
		noteListId = this.androidAppService.getIdResource("noteListHY");
		exitId = this.androidAppService.getIdResource("exitHY");

		// 顶部工具栏
		toolsBarId = this.androidAppService.getIdResource("toolsBarHY"); 						// 工具条.
		toolsBarBackImageId = this.androidAppService.getIdResource("toolsBarBackImageHY"); 	// 工具条上的返回按钮.
		toolsBarBackId = this.androidAppService.getIdResource("toolsBarBackHY"); 				// 工具条上的返回按钮.

		// 章节处理
		chapterProgressPanelId = this.androidAppService.getIdResource("chapterProgressPanelHY");

		chapterNameId = this.androidAppService.getIdResource("chapterNameHY");
		pageIndexId = this.androidAppService.getIdResource("pageIndexHY");

		chapter_progress_sliderId = this.androidAppService.getIdResource("chapter_progress_sliderHY"); // 章节进度条。

		preChapterId = this.androidAppService.getIdResource("preChapterHY"); 					// 章节进度条。
		nextChapterId = this.androidAppService.getIdResource("nextChapterHY"); 				// 章节进度条。

		View seekBarView = windowView.findViewById(chapter_progress_sliderId);
		chapterProgressSeekBar = (SeekBar) seekBarView;

		// 章节切换的滚动条处理事件
		chapterProgressSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
												  int progress,
												  boolean fromUser) {

						// Log.d("aa", "progress : " + progress);

						if (progress == 0) {
							progress = 1;
						}

						if (readerDynamicSDK.getCurrentPageIndex() != progress) {

							readerDynamicSDK.entryPageIndexPage(progress);
							readerDynamicSDK.postInvalidate();
						}

						int bookCatalogIndex = readerDynamicSDK.getCurrentPositionBookCatalogIndex();

						catalogIndex = bookCatalogIndex;

						// Log.d("aa", bookCatalogIndex + " : bookCatalogIndex");

						BookCatalog bookCatalog = bookCatalogList.get(bookCatalogIndex);

						// Log.d("aa", bookCatalog.getTitle() + " : bookCatalog");

						if (progress == 0) {
							progress = 1;
						}

						int total = readerDynamicSDK.getTotalPageCount();

						updateChapterProgressText(bookCatalog, progress, total);

						if (progress >= total) {

							return;
						}
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

						View view = windowView.findViewById(nightDaySwitchId);
						view.setVisibility(View.GONE);

						view = windowView.findViewById(chapterProgressPanelId);
						view.setVisibility(View.VISIBLE);
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

						int pageIndex = seekBar.getProgress();

						if (pageIndex == 0) {
							pageIndex = 1;
						}

						int total = readerDynamicSDK.getTotalPageCount();
						if (pageIndex >= total) {
							pageIndex = total - 1;
						}

						readerDynamicSDK.entryPageIndexPage(pageIndex);
						readerDynamicSDK.postInvalidate();

						View view = windowView.findViewById(chapterProgressPanelId);
						view.setVisibility(View.GONE);

						view = windowView.findViewById(nightDaySwitchId);
						view.setVisibility(View.VISIBLE);

					}
				});

		setupButton(menuId, windowView);

		setupButton(nightDaySwitchId, windowView);
		setupButton(toolsBarBackId, windowView);

		setupButton(preChapterId, windowView);
		setupButton(nextChapterId, windowView);

		setupButton(catalogAllId, windowView);
		setupButton(wordAllId, windowView);
		setupButton(moreAllId, windowView);

		setupButton(addBookmarkId, windowView);
		setupButton(cancelBookmarkId, windowView);
		setupButton(bookmarkListId, windowView);
		setupButton(noteListId, windowView);
		setupButton(exitId, windowView);

		// --------------------------------------------------------------------------------------------------------------

		setCurrentDayNightImage();

		this.setPosition(-10000, -10000); 														// 不在屏幕内显示
	}

	/**
	 * 设置白天夜晚显示模式
	 */
	private void setCurrentDayNightImage() {

		final SharedPreferenceService preference = new SharedPreferenceService(this.activity);

		// true:白天;false:晚上
		boolean nightDay = preference.getValue(nightDayModeKey, true);

		ImageView nightDaySwitch = (ImageView) windowView.findViewById(nightDaySwitchId);

		int resourceId;
		if (nightDay == true) { 																	// 白天

			resourceId = androidAppService.getDrawableResource("night_reader");

		} else { 																					// 晚上.
			resourceId = androidAppService.getDrawableResource("day_reader");
		}

		nightDaySwitch.setImageResource(resourceId);
	}

	private void updateChapterProgressText(BookCatalog catalog,
										   int current,
										   int total) {

		TextView catalogTextView = (TextView) this.windowView.findViewById(chapterNameId);
		TextView pageIndextTextView = (TextView) this.windowView.findViewById(pageIndexId);

		if (catalog != null) {

			catalogTextView.setText(catalog.getTitle());
		}

		pageIndextTextView.setText(current + " / " + total);
	}



	@Override
	public void hide() {

		if (menuAnimator != null && menuAnimator.isRunning()
				&& menuAnimator.isStarted()) { // 动画正在运行时不能处理。
			return;
		}

		if (windowView == null) {
			return;
		}


		float xx = this.windowView.getX();
		float yy = this.windowView.getY();

		Log.d("menu", "x : " + xx + " y : " + yy);

		this.activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 			// 隐藏状态栏

		this.manageSubMenu(true);

		// 动画处理
		final View view = windowView.findViewById(menuId);
		int screenHeight = this.configServiceSDK.getScreenHeight();

		menuAnimator = ObjectAnimator.ofFloat(view, "Y", screenHeight);
		menuAnimator.addListener(menuAnimatorListener);
		menuAnimator.setDuration(300);
		menuAnimator.start();

		View toolsBarview = windowView.findViewById(this.toolsBarId);
		toolsBarAnimator = ObjectAnimator.ofFloat(toolsBarview,
												 "Y",
												  -toolsBarview.getHeight());
		toolsBarAnimator.setDuration(300);
		toolsBarAnimator.start();
	}


	private ObjectAnimator menuAnimator, toolsBarAnimator;
	private MenuAnimatorListener menuAnimatorListener = new MenuAnimatorListener();

	// 子菜单动画。
	private ObjectAnimator subMenuAnimator;
	private SubMenuAnimatorListener subMenuAnimatorListener = new SubMenuAnimatorListener();


	@Override
	public void show() {

		if (menuAnimator != null
				&& menuAnimator.isRunning()
				&& menuAnimator.isStarted()) { 														// 动画正在运行时不能处理。
			return;
		}

		this.windowView.setVisibility(View.VISIBLE);

		this.relativeLayout.bringChildToFront(this.windowView);

		// this.activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 			// 显示状态栏

		View wordAllButton = windowView.findViewById(this.wordAllId);

		wordAllButton.setVisibility(this.isParseBookFinish == false ? View.INVISIBLE : View.VISIBLE);

		setCurrentDayNightImage(); 																	// 每次打开菜单的时候都要更新DayNight按钮的图。

		this.setChapterProgress();

		int sub_menuId = this.androidAppService.getIdResource("sub_menu_HY");					// 菜单显示的时候，子菜单不显示。
		final View view = windowView.findViewById(menuId);
		view.bringToFront();

		View subMenuView = windowView.findViewById(sub_menuId);
		subMenuView.setVisibility(View.INVISIBLE);

		showBookMarkOper(); 																		// 处理子菜单中的书签，得到当前是添加书签还是删除书签

		final View toolsBarView = windowView.findViewById(this.toolsBarId);
		final View toolsBarBackView = windowView.findViewById(this.toolsBarBackImageId);

		int toolsBarViewHeight = toolsBarBackView.getHeight();
		int statusBarHeight = this.getStatusBarHeight();

		int m = (toolsBarView.getHeight() - statusBarHeight - toolsBarViewHeight) / 2 - 1;
		int y = statusBarHeight + m;

		toolsBarBackView.setY(y);																	// 定位返回按钮在Y轴的位置。

		int height = view.getHeight();																// 动画处理
		int screenHeight = this.configServiceSDK.getScreenHeight();

		view.setY(screenHeight);

		menuAnimator = ObjectAnimator.ofFloat(view, "Y", screenHeight - height);
		menuAnimator.setDuration(300);
		menuAnimator.start();

		toolsBarAnimator = ObjectAnimator.ofFloat(toolsBarView, "Y", 0);
		toolsBarAnimator.setDuration(300);
		toolsBarAnimator.start();
	}

	public void setChapterProgress() {

		int currentPageIndex = this.readerDynamicSDK.getCurrentPageIndex();
		chapterProgressSeekBar.setProgress(currentPageIndex);

		// 显示的时候，设置章节进度条。
		int totalPageCount = this.readerDynamicSDK.getTotalPageCount();
		chapterProgressSeekBar.setMax(totalPageCount);

		int bookCatalogIndex = this.readerDynamicSDK.getCurrentPositionBookCatalogIndex();
		this.catalogIndex = bookCatalogIndex;

		BookCatalog catalog = this.bookCatalogList.get(bookCatalogIndex);

		updateChapterProgressText(catalog, currentPageIndex, totalPageCount);
	}

	/**
	 * 功能处理事件.
	 * @param view
	 */
	public void onClick(View view) {

		int id = view.getId();

		if (menuId == id) {

			return;
		}

		if (nightDaySwitchId == id) {																// 白天或夜晚模式切换

			this.dayNightSwitch(view);
			return;
		}

		if (preChapterId == id) {

			switchToCatalog(PREVIOUS_CHAPTER);
			return;
		}

		if (nextChapterId == id) {

			switchToCatalog(NEXT_CHAPTER);
			return;
		}

		if (id == catalogAllId) { 					// 进入目录。

			hideSubMenuAndMenu();

			this.readerPopupService.showCatalogPage();

			return;
		}

		if (id == moreAllId) {						// 打开子菜单

			if (subMenuAnimator != null && subMenuAnimator.isRunning()
					&& subMenuAnimator.isStarted()) { // 动画正在运行时不能处理。
				return;
			}

			final View subView = getSubMenuView();
			if (subView.getVisibility() == View.VISIBLE) {

				manageSubMenu(true);
			} else if (subView.getVisibility() == View.INVISIBLE) {

				subView.setVisibility(View.VISIBLE);
				manageSubMenu(false);
			}
			return;
		}

		if (id == wordAllId) {						// 进入文字设置面板

			hideSubMenuAndMenu();

			this.readerPopupService.showWordPanel();

			return;
		}

		if (id == cancelBookmarkId) { 				// 删除当前页的书签

			List<BookStress> bookStressList = this.readerDynamicSDK.deleteCurrentPageBookMarkFromMemeory();

			for (BookStress bookStress : bookStressList) { // 正常情况下一个页只有一个书签。也有不正常情况

				this.readerDynamicSDK.deleteCloudStress(bookStress);
			}

			manageSubMenu(true);

			this.readerDynamicSDK.repaint();
			return;
		}

		if (id == addBookmarkId) { 					// 添加书签

			BookStress bookStress = this.readerDynamicSDK.addBookMark();

			this.readerDynamicSDK.saveCloudStress(bookStress);

			this.readerDynamicSDK.repaint();

			manageSubMenu(true);

			return;
		}

		if (id == bookmarkListId) { 					// 进入书签列表

			hideSubMenuAndMenu();

			this.readerPopupService.showBookmarkPage();

			return;

		}

		if (id == noteListId) {						// 笔记列表

			hideSubMenuAndMenu();

			this.readerPopupService.showNotePage();

			return;
		}

		if (id == exitId || id == toolsBarBackId) {  // 退出阅读的入口

			this.readerDynamicSDK.exitReader();
		}
	}

	private void dayNightSwitch(View view) {

		ReaderPageStyle readerPageStyle = null;
		int resourceId;
		final SharedPreferenceService preference = new SharedPreferenceService(this.activity);

		boolean nightDayMode = preference.getValue(nightDayModeKey, true);
		if (nightDayMode == true) { 															// 如果是白天，让它变成晚上

			// 存储当前样式，当切换到夜间模式时.

			ReaderPageStyle pageStyle = this.configServiceSDK.getCurrentReaderPageStyle();

			preference.putValue("PRE_DAY_NIGHT_STYLENAME_KEY_HY", pageStyle.styleName);

			resourceId = androidAppService.getDrawableResource("day_reader");
			readerPageStyle = getNightDayModePageStyle(ReaderPageStyle.NightDayMode.night);

			preference.putValue(nightDayModeKey, false);
		} else {

			resourceId = androidAppService.getDrawableResource("night_reader");
			preference.putValue(nightDayModeKey, true);

			// 切换到白天模式时，下边代码是根据逻辑拿到白天模式的样式对象。
			String styleName = preference.getValue("PRE_DAY_NIGHT_STYLENAME_KEY_HY", "");

			if (styleName != null) {

				List<ReaderPageStyle> pageStyles = configServiceSDK.getReaderPageStyles();

				for (ReaderPageStyle pStyle : pageStyles) {

					if (styleName.equals(pStyle.styleName)) {

						readerPageStyle = pStyle;

						if (readerPageStyle.nightDayMode == ReaderPageStyle.NightDayMode.night) {
							readerPageStyle = null;
						}
						break;
					}
				} // end for

				if (readerPageStyle == null) {
					readerPageStyle = getNightDayModePageStyle(ReaderPageStyle.NightDayMode.day);
				}
			} else {

				readerPageStyle = getNightDayModePageStyle(ReaderPageStyle.NightDayMode.day);
			}
		}

		ImageView nightDaySwitch = (ImageView) view;
		nightDaySwitch.setImageResource(resourceId);

		this.configServiceSDK.setCurrentReaderPageStyle(readerPageStyle);

		this.readerDynamicSDK.repaint();


	}

	public int getStatusBarHeight() {

		int result = 0;
		int resourceId = this.activity.getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = this.activity.getResources().getDimensionPixelSize(resourceId);
		}

		return result;
	}

	private void showBookMarkOper() {

		final View cancelView = windowView.findViewById(cancelBookmarkId);
		final View addView = windowView.findViewById(addBookmarkId);

		boolean isExist = this.readerDynamicSDK.isExistBookMarkFromCurrentPage();
		if (isExist == true) {

			// 显示取消。
			cancelView.setVisibility(View.VISIBLE);
			addView.setVisibility(View.GONE);
		} else {
			// 显示添加。
			cancelView.setVisibility(View.GONE);
			addView.setVisibility(View.VISIBLE);
		}
	}

	private static final int PREVIOUS_CHAPTER = 0, NEXT_CHAPTER = 1;

	private void switchToCatalog(int go) {

		if (go != PREVIOUS_CHAPTER && go != NEXT_CHAPTER) {
			return;
		}

		if (go == PREVIOUS_CHAPTER) {

			if (this.catalogIndex == 0) { // 没有上一章了
				return;
			}
			this.catalogIndex -= 1;
		}

		if (go == NEXT_CHAPTER) {

			if (this.catalogIndex == this.bookCatalogList.size() - 1) { // 没有下一章了
				return;
			}
			this.catalogIndex += 1;
		}

		BookCatalog bookCatalog = this.bookCatalogList.get(this.catalogIndex);

		this.readerDynamicSDK.entryBookCatalogPage(bookCatalog);
		this.readerDynamicSDK.postInvalidate();

		int pageIndex = this.readerDynamicSDK.getCurrentPageIndex();
		int totalPageCount = this.readerDynamicSDK.getTotalPageCount();

		this.chapterProgressSeekBar.setProgress(pageIndex);

		updateChapterProgressText(bookCatalog, pageIndex, totalPageCount);
	}

	private ReaderPageStyle getNightDayModePageStyle(ReaderPageStyle.NightDayMode nightDayMode) {

		List<ReaderPageStyle> list = this.configServiceSDK.getReaderPageStyles();

		for (ReaderPageStyle readerPageStyle : list) {

			if (readerPageStyle.nightDayMode == nightDayMode) {

				return readerPageStyle;
			}
		}
		return null;
	}

	private void hideSubMenuAndMenu() {

		final View subView = getSubMenuView();
		if (subView.getVisibility() == View.VISIBLE) {

			manageSubMenu(true, true);
		} else {

			this.readerPopupService.hideCurrentPopup();
		}
	}

	private View getSubMenuView() {

		int sub_menuId = this.androidAppService.getIdResource("sub_menu_HY");
		final View subView = this.windowView.findViewById(sub_menuId);

		return subView;
	}

	private void manageSubMenu(boolean isClose) {

		this.manageSubMenu(isClose, false);
	}

	private void manageSubMenu(boolean isClose, boolean isCloseMenu) {

		final View subView = getSubMenuView();
		final View parentView = windowView.findViewById(this.menuId);

		int subViewHeight = subView.getHeight();

		this.subMenuAnimatorListener.setOpenOrClose(isClose);
		this.subMenuAnimatorListener.setSubMenuView(subView);

		if (isClose) { // 隐藏

			float showY = subView.getY();
			float hideY = showY + subViewHeight;
			subMenuAnimator = ObjectAnimator.ofFloat(subView, "Y", hideY);

			if (isCloseMenu) {
				this.hide();
			}
		} else { // 显示

			float parentY = parentView.getY();
			float showY = parentY - subViewHeight;
			subView.setY(parentY); // 动画从一级菜单的y点开始。

			showBookMarkOper();

			subMenuAnimator = ObjectAnimator.ofFloat(subView, "Y", showY);
		}

		subMenuAnimator.addListener(this.subMenuAnimatorListener);
		subMenuAnimator.setDuration(300);
		subMenuAnimator.start();
	}

	// ---------------------------------------------------------------------------------------------

	class MenuAnimatorListener extends AnimatorDefaultListener {

		public void onAnimationEnd(Animator animation) {

			if (windowView == null) {
				return;
			}

			windowView.setVisibility(View.INVISIBLE);

			setPosition(-10000, -10000);
		}
	}

    class SubMenuAnimatorListener extends AnimatorDefaultListener {

		private boolean openOrClose;
		private View subMenuView;

		public void onAnimationEnd(Animator animation) {

			if (this.subMenuView == null) {
				return;
			}

			if (this.openOrClose) {
				this.subMenuView.setVisibility(View.INVISIBLE);
			}
		}

		public void setOpenOrClose(boolean openOrClose) {

			this.openOrClose = openOrClose;
		}

		public void setSubMenuView(View subMenuView) {

			this.subMenuView = subMenuView;
		}
	}

}
