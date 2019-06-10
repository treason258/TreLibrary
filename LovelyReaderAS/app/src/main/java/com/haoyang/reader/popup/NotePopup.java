package com.haoyang.reader.popup;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Point;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.base.service.android.AndroidAppService;

import com.haoyang.reader.ui.TextShowView;

import com.haoyang.reader.sdk.BookStress;

/**
 *
 * 
 * @author tianyu912@yeah.net
 */
public class NotePopup extends PopupPanel implements View.OnClickListener {

	public final static String ID = "NoteShowPopup";

	private BookStress bookStress;

	private ImageView arrowBottomView;
	private ImageView arrowTopView;
	private TextShowView textShowView;

	private AndroidAppService androidAppService;

	public NotePopup(ReaderPopupService readerPopupService) {

		super(ID, readerPopupService);
	}

	public void initUI(RelativeLayout relativeLayout) {

		if (windowView != null && activity == windowView.getContext()) {
			return;
		}

		this.androidAppService = new AndroidAppService(activity);

		int note_panelLayout = this.androidAppService.getLayoutResource("hy_note_panel");

		// 加载弹出的选择框布局。
		activity.getLayoutInflater().inflate(note_panelLayout, relativeLayout);
		int note_show_panelId = this.androidAppService.getIdResource("note_show_panel");

		this.windowView = relativeLayout.findViewById(note_show_panelId);

		int note_show_contentId = this.androidAppService.getIdResource("note_show_content");
		this.textShowView = (TextShowView) windowView.findViewById(note_show_contentId);

		int arrowBottomId = this.androidAppService.getIdResource("arrowBottom");
		this.arrowBottomView = (ImageView) windowView.findViewById(arrowBottomId);

		int arrowTopId = this.androidAppService.getIdResource("arrowTop");
		this.arrowTopView = (ImageView) windowView.findViewById(arrowTopId);

		this.setPosition(-10000, -10000);
	}

	public void setBookStress(BookStress bookStress) {

		if (bookStress == null) {
			return;
		}

		this.bookStress = bookStress;

		if (this.textShowView != null) {

			String content = bookStress.getCommentContent();

			textShowView.setText(content);
			textShowView.make();
			textShowView.postInvalidate();
		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dp2px(Context context, float dpValue) {

		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dp(Context context, float pxValue) {

		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 计算弹出的菜单显示的位置，并显示出来。
	 */
	public void followSelectionCoordinate() {

		if (this.windowView == null) {
			return;
		}

		int noteBarHeight = this.textShowView.getWindowHeight();
		int noteBarWidth = this.textShowView.getWindowWidth();

		Point startPoint = this.readerDynamicSDK.getStressStartPoint(this.bookStress);

		int textSize = this.configServiceSDK.getTextSize();

		int noteHeight = this.readerDynamicSDK.getNoteIcon().getHeight();

		// 计算弹出框的y坐标值。
		int y = 0;

		int arrowBottomHeight = this.arrowBottomView.getHeight();

		if (startPoint.y - noteBarHeight - arrowBottomHeight > 30) { // 显示在选中文字的上边。

			y = startPoint.y - noteBarHeight - arrowBottomHeight - 30;

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setTextSize(textSize);

			FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();

			int ascent = Math.abs(fontMetricsInt.ascent);

			y = startPoint.y - noteBarHeight - arrowBottomHeight - ascent;

			arrowBottomView.setVisibility(View.VISIBLE);
			this.arrowTopView.setVisibility(View.INVISIBLE);
		} else {

			y = startPoint.y + textSize + this.arrowTopView.getHeight();

			arrowBottomView.setVisibility(View.INVISIBLE);
			this.arrowTopView.setVisibility(View.VISIBLE);
		} // end if

		int leftRigthSpace = this.configServiceSDK.getLeftRigthSpace();
		int readWidth = this.configServiceSDK.getReadAreaWidth();

		// 计算弹出框的x坐标值。
		int x = 0;
		int barWidthHalf = noteBarWidth / 2;
		if (startPoint.x <= barWidthHalf) { // 在最左端

			x = leftRigthSpace / 2;
		} else if ((readWidth - startPoint.x) <= barWidthHalf) { // 在最右端

			x = readWidth + leftRigthSpace + leftRigthSpace / 2 - noteBarWidth;
		} else { // 跟着 start.XStart 走

			x = startPoint.x - barWidthHalf;
		}

		this.arrowBottomView.setX((startPoint.x + textSize - startPoint.x) / 2 + (startPoint.x - x) - (arrowBottomView.getWidth() / 2));
		this.arrowTopView.setX((startPoint.x + textSize - startPoint.x) / 2 + (startPoint.x - x) - (arrowTopView.getWidth() / 2));

		this.setPosition(x, y);
	}

	@Override
	public void show() {

		this.windowView.setVisibility(View.VISIBLE);

		this.relativeLayout.bringChildToFront(this.windowView);
	}

	@Override
	public void hide() {

		this.windowView.setVisibility(View.INVISIBLE);
		setPosition(-10000, -10000);
	}

	public void onClick(View view) {

		switch (view.getId()) {

		}
	}

}
