package com.haoyang.reader.popup;

import android.view.View;
import android.widget.RelativeLayout;

import android.graphics.Point;

import com.app.base.service.android.AndroidAppService;

import com.haoyang.reader.sdk.BookStress;

import com.haoyang.reader.sdk.PageAnimationService;

/**
 * 选定内容后弹出的工具条面板:划线、重点、笔记、复制等等。
 * 
 * @author tianyu912@yeah.net
 */
public class SelectionDeletePopup extends PopupPanel  {

	public final static String ID = "SelectionDeletePopup";

	private AndroidAppService androidAppService;

	private BookStress bookStress;
	private int selectionPanelDeleteId;

	public SelectionDeletePopup(ReaderPopupService readerPopupService) {

		super(ID, readerPopupService);

	}

	public void initUI(RelativeLayout relativeLayout) {

		if (this.windowView != null && this.activity == this.windowView.getContext()) {
			return;
		}

		this.androidAppService = new AndroidAppService(activity);

		int selectionDeletePaneLayoutId = this.androidAppService.getLayoutResource("hy_selection_delete_panel");

		// 加载弹出的选择框布局。
		this.activity.getLayoutInflater().inflate(selectionDeletePaneLayoutId, relativeLayout);

		int selection_text_panelId = this.androidAppService.getIdResource("selection_delete_panel");

		this.windowView  = relativeLayout.findViewById(selection_text_panelId);

		this.setPosition(-10000, -10000);

		this.selectionPanelDeleteId = androidAppService.getIdResource("selection_panel_deleteId");

		setupButton(this.selectionPanelDeleteId, this.windowView);
	}

	/**
	 * 计算弹出的菜单显示的位置，并显示出来。
	 */
	public void followSelectionCoordinate(Point point) {

		if (this.windowView == null) {
			return;
		}

		int barHeight = windowView.getHeight();
		int barWidth = windowView.getWidth();

		// 计算弹出框的y坐标值。
		// Bitmap cursorAbove = this.readerService.cursorAbove;

		Point start = this.readerDynamicSDK.getStressStartPoint(this.bookStress);

		int h = 0; //- cursorAbove.getHeight();
		int y = 0;

		if (start.y - barHeight - h > 30) {
			// 显示在选中文字的上边。

			y = start.y - barHeight - h - 30;

			if (y < (barHeight + h)) {

				y = start.y + barHeight + h;
			}

		} else {

			y = start.y + h + this.configServiceSDK.getLineSpace() / 2;

			if (y > this.configServiceSDK.getReadAreaHeight()) {

				y = start.y + barHeight + h;
			}

		} // end if

		int leftRigthSpace = this.configServiceSDK.getLeftRigthSpace();

		// 计算弹出框的x坐标值。
		int x = 0;

		int barWidthHalf = barWidth / 2;
		if (start.x <= barWidthHalf) {

			x = leftRigthSpace;
		} else if (start.x > barWidthHalf) {

			if (this.configServiceSDK.getReadAreaWidth() - start.x > barWidthHalf) {

				x = start.x - barWidthHalf;
			} else {

				x = this.configServiceSDK.getReadAreaWidth() - barWidth;
			}
		}

		this.setPosition(x, y);

//		super.rootView.bringChildToFront(myWindow);
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

		int id = view.getId();

		if (id == this.selectionPanelDeleteId) {

			this.readerDynamicSDK.deleteStressFromMemory(this.bookStress);

			this.readerDynamicSDK.repaint();

			this.readerPopupService.hideCurrentPopup();

			this.readerDynamicSDK.deleteCloudStress(this.bookStress);
		}
	}

	public void setBookStress(BookStress bookStress) {

		this.bookStress = bookStress;
	}
}
