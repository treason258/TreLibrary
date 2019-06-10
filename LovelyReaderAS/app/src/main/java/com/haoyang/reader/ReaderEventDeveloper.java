package com.haoyang.reader;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Size;
import android.view.KeyEvent;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import android.content.Context;
import android.view.WindowManager;

import com.haoyang.reader.popup.ReaderPopupService;
import com.haoyang.reader.animation.AnimationProvider;
import com.haoyang.reader.animation.SimulationProvider;
import com.haoyang.reader.animation.SlideAnimationProvider;
import com.haoyang.reader.animation.ShiftAnimationProvider;

import com.haoyang.reader.sdk.PageNum;
import com.haoyang.reader.sdk.AnimationType;
import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.sdk.BookCatalog;
import com.haoyang.reader.sdk.BookStress;
import com.haoyang.reader.sdk.ConfigServiceSDK;
import com.haoyang.reader.sdk.ReaderDynamicSDK;
import com.haoyang.reader.sdk.ReaderEvent;
import com.haoyang.reader.sdk.TextHyperlink;
import com.haoyang.reader.sdk.PageAnimationService;
import com.haoyang.reader.sdk.StaticReadAreaConfig;

import java.util.List;

public class ReaderEventDeveloper implements ReaderEvent {

    private FragmentActivity activity;

    private RelativeLayout relativeLayout;
    private  Book book;
    private ConfigServiceSDK configServiceSDK;
    private ReaderDynamicSDK readerDynamicSDK;

    private PageAnimationService pageAnimationService;

    private ReaderLayoutService readerLayoutService;

    private ReaderPopupService readerPopupService;

    private AnimationProvider animationProvider; // 动画的实现引用。

    private AnimationType animationType; // 动画的类型。

    public ReaderEventDeveloper() {

        animationType = AnimationType.SlideAnimation; // 动画的类型。
    }

    public void  onExit() {

    }

    public boolean onClick(Point point) {

        if (this.readerPopupService.isActivePopup() == true) {

            if (this.readerDynamicSDK.isSelected() == true) {

                this.readerDynamicSDK.stopSelection();
                this.readerDynamicSDK.repaint();
            }

            // 如果有弹出框显示，包括选文字的光标有显示，就先隐藏掉.
            this.readerPopupService.hideCurrentPopup();
            this.showNavigationBar();

            return true;
        }

        boolean isSelectElement = this.readerDynamicSDK.isSelectElement(point);
        if (isSelectElement == true) { // 选中的是文字.

            TextHyperlink textHyperlink = this.readerDynamicSDK.clickHyperlink(point);

            if (textHyperlink != null) {

                this.readerDynamicSDK.entryHyperlinkPage(textHyperlink);

                return true;
            }

            BookStress bookStress = this.readerDynamicSDK.clickBookStress(point);

            if (bookStress != null &&
                    (bookStress.getStressType() == BookStress.StressType.Stress
                     || bookStress.getStressType() == BookStress.StressType.StressLine)) {

                this.readerPopupService.showSelectionDeletePanel(bookStress, point);

                return true;
            }

            if (bookStress != null && bookStress.getStressType() == BookStress.StressType.StressNote) {

                this.readerPopupService.showNotePanel(bookStress);

                return true;
            }
        }

        int width = this.configServiceSDK.getScreenWidth();

        int areaWidth = (int) (width / 3.0f);

        if (point.x >= areaWidth && point.x <= (areaWidth * 2)) {

            this.hideNavigationBar();
            this.readerPopupService.showMenuPanel();
            return false;
        }

        float half = width / 2;
        if (this.readerDynamicSDK.getPageNum() == PageNum.Two) {

            half = width;
        }

        if (animationType == AnimationType.SlideAnimation
                || animationType == AnimationType.CurlAnimation
                || animationType == AnimationType.ScrollAnimation) {

            int speed = 10;

            this.singleClickScroll(point, speed);   // 单击翻页。

            return false;
        }

        return false;
    }

    private void singleClickScroll(Point currentPoint, int speed) {

        final AnimationProvider animator = getAnimationProvider();
        if (animator == null) {
            return;
        }

        animator.init(this.configServiceSDK.getScreenWidth(),
                      this.configServiceSDK.getScreenHeight(),
                     10);

        if (animator.inAutoAnimating()) {
            return;
        }

        float half = this.configServiceSDK.getScreenWidth() / 2;
        if (this.readerDynamicSDK.getPageNum() == PageNum.Two) {

            half = this.configServiceSDK.getScreenWidth();
        }

        boolean forward = false; // 记录是进入下一页还是上一页。

        if (currentPoint.x > half) { // 进入下一页，否则进入上一页

            forward = true;
        }

        // 是否可以进入上一页或下一页
        if (this.readerDynamicSDK.isPageEnd(forward) == true) {

            animator.terminate();
            return;
        }

        animator.startManualScrolling(currentPoint);

        animator.singleClickScroll(currentPoint, speed, forward);

        this.readerDynamicSDK.postInvalidate();

        // 准备下一页或上一页
        final AnimationProvider.Mode currentMode = animator.getMode();

        if (currentMode == AnimationProvider.Mode.AutoScrollingForward) {

            this.readerDynamicSDK.prepareNextPage();

        } else if (currentMode == AnimationProvider.Mode.AutoScrollingBackward) {

            this.readerDynamicSDK.preparePreviousPage();
        }

        this.readerDynamicSDK.saveReadHistory();
    }

    public void startManualScrolling(Point point) {

        final AnimationProvider animator = getAnimationProvider();

        if (animator == null) {
            return;
        }

        animator.init(this.configServiceSDK.getScreenWidth(),
                      this.configServiceSDK.getScreenHeight(),
                    10);

        animator.startManualScrolling(point);

    }

    public void scrollManuallyTo(Point point) {

        final AnimationProvider animator = this.getAnimationProvider();
        if (animator == null) {
            return;
        }

        animator.scrollTo(point); // 先移动一下位置，然后根据这个位置查看是向那个方向翻页。

        boolean direction = animator.getPageToScrollTo();

        // 是否可以进入上一页或下一页
        if (this.readerDynamicSDK.isPageEnd(!direction) == true) {

            animator.terminate();
        }

        this.readerDynamicSDK.postInvalidate();
    }

    public void startAnimatedScrolling(Point point) {

        final AnimationProvider animator = getAnimationProvider();

        boolean forward = animator.getPageToScrollTo();

        // 是否可以进入上一页或下一页
        if (this.readerDynamicSDK.isPageEnd(!forward) == true) {

            animator.terminate();
            return;
        }

        if (animator.inAutoAnimating()) { // 动画结束后才能继续。
            return;
        }

        animator.startAnimatedScrolling(point);

        this.readerDynamicSDK.postInvalidate();

        // 自动动画开始之后开始绘制需要重绘的页面
        final AnimationProvider.Mode currentMode = animator.getMode();

        if (currentMode == AnimationProvider.Mode.AutoScrollingForward) {

            this.readerDynamicSDK.prepareNextPage();

        } else if (currentMode == AnimationProvider.Mode.AutoScrollingBackward) {

            this.readerDynamicSDK.preparePreviousPage();
        }

        this.readerDynamicSDK.saveReadHistory();
    }

    public void onDoubleClick(Point point) {

    }

    @Override
    public void onLongPress(Point point) {

        boolean isSelect = this.readerDynamicSDK.isSelectElement(point);

        if (isSelect == true) {

            this.readerDynamicSDK.initSelection(point);

            this.readerDynamicSDK.repaint();
        }
    }

    @Override
    public void onMoveLongPress(Point point) {

        boolean isSelected = this.readerDynamicSDK.isSelected();

        if (isSelected == true) {

            this.readerDynamicSDK.moveSelection(point);

            this.readerDynamicSDK.repaint();
        }

    }

    @Override
    public void onReleaseLongPress(Point point) {

        boolean isSelected = this.readerDynamicSDK.isSelected();

        if (isSelected == true) {

            this.readerPopupService.showSelectionPanel(point);

            this.readerDynamicSDK.repaint();
        }

    }

    @Override
    public boolean onPress(Point point) {

		if (this.readerDynamicSDK.isSelected() == true) {

			this.readerDynamicSDK.moveSelection(point);

            this.readerDynamicSDK.repaint();

			return true;
		} // end if

		if (AnimationType.NoneAnimation == this.animationType) {
			return true;
		}

		this.startManualScrolling(point); // 翻页动画的准备工作, 此时不用刷新界面。

        return false;
    }

    @Override
    public boolean onMove(Point point) {

        if (this.readerDynamicSDK.isSelected() == true) {

            this.readerDynamicSDK.moveSelection(point);

			this.readerDynamicSDK.repaint();

			return true;
		}

		if (AnimationType.NoneAnimation == this.animationType) {
            return true;
		}

		this.readerPopupService.hideCurrentPopup();

		// Log.d("tag", "onMove  point.x = " + point.x + "  point.y = " + point.y);

		this.scrollManuallyTo(point);

		return true;
    }

    @Override
    public void onRelease(Point point) {

        if (this.readerDynamicSDK.isSelected() == true) {

			this.readerPopupService.showSelectionPanel(point);

			return;
		}

		if (AnimationType.NoneAnimation == this.animationType) {
			return;
		}

		this.startAnimatedScrolling(point);

    }

    public void  onChapterReaderFinish(int chapterIndex) {

    }

    public void onBookBeginning() {

        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(activity, "没有上一页!!", Toast.LENGTH_LONG);

            }
        });

    }

    /**
     * 到达电子书结束.
     */
    public void onBookEnd() {

        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(activity, "没有下一页!!", Toast.LENGTH_LONG);
            }
        });

    }

    @Override
    public void onCloudDataLoadFinish() {

        this.readerDynamicSDK.repaintALL();

    }

    public boolean onDrawPageAnimation(Canvas canvas) {

        final AnimationProvider animator = getAnimationProvider();

        animator.doStep(); // 此处调用，让动画往前走。并检查是否结束.

        boolean inProgress = animator.inProgress();

        if (inProgress) {
            // 如果条件成立，就绘制，绘制完在postInvalidate();
            // 然后在检查条件，这样重复，直接动画结束。
            animator.draw(canvas);

            // Log.d("tag", "animator.draw(canvas)");

            this.readerDynamicSDK.postInvalidate();

            if (animator.getMode().auto) {

                return true;
            }

            return true;
        }

        return false;
    }

    /**
     * 电子书解析完成.
     */
    public void onBookParseFinish(final BookCatalog bookRootCatalog,
                                  final List<BookCatalog> bookCatalogList) {

        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                readerPopupService.initData(bookRootCatalog, bookCatalogList);

                if (readerLayoutService != null) { // 目录可以使用了

                    readerLayoutService.setCatalogEnable(true);
                }

                if (readerPopupService == null) {

                    return;
                }

                readerPopupService.parseBookFinish(true);

            }
        });


    }

    public RelativeLayout onCreate(Bundle bundle,
                                   FragmentActivity fragmentActivity,
                                   Book book,
                                   ConfigServiceSDK configServiceSDK,
                                   ReaderDynamicSDK readerDynamicSDK) {

        this.activity = fragmentActivity;

        this.book = book;

        this.configServiceSDK = configServiceSDK;
        this.readerDynamicSDK = readerDynamicSDK;

        this.setWindowParam();

        this.readerLayoutService = new ReaderLayoutService();

        this.relativeLayout = this.readerLayoutService.initReaderLayout(activity);

        this.readerPopupService = new ReaderPopupService(readerLayoutService, book, activity);

        this.readerLayoutService.init(book,
                                      readerDynamicSDK,
                                      readerPopupService);

        this.readerPopupService.initPopup(this.relativeLayout,
                                          configServiceSDK,
                                          readerDynamicSDK);

        return this.relativeLayout;
    }

    public void setStatusBarHidden(Boolean isHidden) {

        if (isHidden) {

            this.activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏状态栏

            return;
        }

        this.activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 显示状态栏
    }

    private void setWindowParam() {

        this.activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        this.activity.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置为无标题。

        setStatusBarHidden(true);

        this.activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 1);
        this.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // hideBottomUIMenu();
    }


    /**
     * 隐藏虚拟按键，并且全屏
     */
    private void showNavigationBar() {

        //隐藏虚拟按键，并且全屏

        if (Build.VERSION.SDK_INT < 19) {

            View v = this.activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.VISIBLE);

        } else if (Build.VERSION.SDK_INT >= 19) {

            View decorView = this.activity.getWindow().getDecorView();

            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;

            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    private void hideNavigationBar() {

        if (Build.VERSION.SDK_INT < 19) {

            View v = this.activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);

        } else if (Build.VERSION.SDK_INT >= 19) {

            View decorView = this.activity.getWindow().getDecorView();

            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                                | View.SYSTEM_UI_FLAG_FULLSCREEN;

            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public StaticReadAreaConfig onStaticReadAreaConfig(FragmentActivity fragmentActivity) {

        WindowManager wm = (WindowManager) fragmentActivity.getSystemService(Context.WINDOW_SERVICE);

        Point p = new Point();

        wm.getDefaultDisplay().getRealSize(p);

        StaticReadAreaConfig staticReadAreaConfig = new StaticReadAreaConfig();


        staticReadAreaConfig.width = p.x;
        staticReadAreaConfig.height = p.y;

        int height = NavigationBarService.getNavigationBarHeight(fragmentActivity);

        staticReadAreaConfig.navigationBarHeight = height;

        return staticReadAreaConfig;
    }

    public void onStart(PageAnimationService pageAnimationService) {

        this.pageAnimationService = pageAnimationService;

        Typeface typeface = Typeface.createFromAsset(this.activity.getAssets(), "yahei.ttf");

        this.readerDynamicSDK.setCurrentTypeFace(typeface);

    }

    public void onResume() {


    }

    public void onPause() {


    }

    public void onStop() {


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (this.readerPopupService.isActivePopup()) { // 在关掉打开的popup

            this.readerPopupService.hideCurrentPopup();

            this.readerDynamicSDK.stopSelection();
            this.readerDynamicSDK.repaint();

            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            this.readerDynamicSDK.exitReader();
        }

        return false;
    }

        /**
         * 获取动画类型。
         *
         * @return
         */
        private AnimationProvider getAnimationProvider() {

            if (this.readerDynamicSDK == null) {
                return null;
            }

            // 如果用户没有设置过那么就用默认的。
            AnimationType type = null;

            // if (type == null) {
            type = AnimationType.SlideAnimation;
            // }

            if (this.animationType != null && this.animationProvider != null) {

                return this.animationProvider;
            }

            int speed = 10;
            float animationSpeedFactor = 1.3f;

            animationType = type;
            switch (type) {
                case NoneAnimation:

                    return null;
                case CurlAnimation:

                    this.animationProvider = new SimulationProvider(this.pageAnimationService,
                                                                    speed,
                                                                    animationSpeedFactor);
                    break;
                case SlideAnimation:

                    this.animationProvider = new SlideAnimationProvider(this.pageAnimationService,
                                                                        speed,
                                                                        animationSpeedFactor);
                    break;
                case ScrollAnimation:

                    this.animationProvider = new ShiftAnimationProvider(this.pageAnimationService,
                                                                        speed,
                                                                        animationSpeedFactor);
                    break;
            } // end switch

            return this.animationProvider;
        }
    }
