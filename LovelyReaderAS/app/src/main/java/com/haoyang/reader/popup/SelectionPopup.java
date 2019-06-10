package com.haoyang.reader.popup;

import android.app.Application;
import android.graphics.Point;
import android.text.ClipboardManager;
import android.view.View;

import android.graphics.Bitmap;

import android.widget.RelativeLayout;

import com.app.base.service.android.AndroidAppService;
import com.app.base.service.android.ToastService;

import com.haoyang.reader.sdk.BookStress;

/**
 * 选定内容后弹出的工具条面板:划线、重点、笔记、复制等等。
 *
 * @author tianyu912@yeah.net
 */
public class SelectionPopup extends PopupPanel {

    public final static String ID = "SelectionPopup";

    private AndroidAppService androidAppService;

    private int selection_panel_lineId;
    private int selection_panel_stressId;
    private int selection_panel_StressNoteId;
    private int selection_panel_copyId;
    private int selection_panel_checkerrorId;
    private int selection_panel_sharedId;

    public SelectionPopup(ReaderPopupService readerPopupService) {

        super(ID, readerPopupService);
    }

    public void initUI(RelativeLayout relativeLayout) {

        this.androidAppService = new AndroidAppService(activity);

        int selection_panelLayout = this.androidAppService.getLayoutResource("hy_selection_panel");

        // 加载弹出的选择框布局。
        activity.getLayoutInflater().inflate(selection_panelLayout, relativeLayout);

        int selection_text_panelId = this.androidAppService.getIdResource("selection_text_panel");

        this.windowView = relativeLayout.findViewById(selection_text_panelId);

        selection_panel_lineId = this.androidAppService.getIdResource("selection_panel_line");
        selection_panel_stressId = this.androidAppService.getIdResource("selection_panel_stress");
        selection_panel_StressNoteId = this.androidAppService.getIdResource("selection_panel_StressNote");
        selection_panel_copyId = this.androidAppService.getIdResource("selection_panel_copy");

        selection_panel_checkerrorId = this.androidAppService.getIdResource("selection_panel_checkerror");

        selection_panel_sharedId = this.androidAppService.getIdResource("selection_panel_shared");

//        View view = this.windowView.findViewById(selection_panel_sharedId);
//        view.setVisibility(View.GONE);

            // int id = androidAppService
            // .getIdResource("selection_panel_shared_sep");
            //
            // view = this.myWindow.findViewById(id);
            // view.setVisibility(View.GONE);

        // 设置布局中每个按钮的参数。
        setupButton(selection_panel_lineId, this.windowView);
        setupButton(selection_panel_stressId, this.windowView);
        setupButton(selection_panel_StressNoteId, this.windowView);
        setupButton(selection_panel_copyId, this.windowView);
        // setupButton(selection_panel_bookmarkId);
        setupButton(selection_panel_checkerrorId, this.windowView);
        setupButton(selection_panel_sharedId, this.windowView);

        this.setPosition(-100000, -100000);

        // 这一步是为了让弹出框行显示出来，然后我们就知道它的宽和高了，
        // 这样在选中文字时能计算出弹出框应该显示的位置

        this.show();
    }

    /**
     * 计算弹出的菜单显示的位置，并显示出来。
     */
    public void followSelectionCoordinate() {

        if (super.windowView == null) {
            return;
        }

        int barHeight = super.windowView.getHeight();
        int barWidth = super.windowView.getWidth();

        Point start = super.readerDynamicSDK.getSelectionStartPoint();
        Point end = super.readerDynamicSDK.getSelectionEndPoint();

        // 计算弹出框的y坐标值。
        Bitmap cursorAbove = super.readerDynamicSDK.getSelectionAboveCursorIcon();
        Bitmap cursorBelow = super.readerDynamicSDK.getSelectionBelowCursorIcon();

        int cursorBelowHeight = cursorBelow.getHeight();
        int cursorAboveHeight = cursorAbove.getHeight();

        int y = 0;
        if (start.y - barHeight - cursorAboveHeight > 30) { // 显示在选中文字的上边。

            y = start.y - barHeight - cursorAboveHeight - 30;

            if (y < (barHeight + cursorAboveHeight)) {
                y = start.y + barHeight + cursorAboveHeight;
            }

        } else {

            y = end.y + cursorBelowHeight
                    + super.configServiceSDK.getLineSpace()/ 2;

            if (y > super.configServiceSDK.getReadAreaHeight()) {

                y = start.y + barHeight + cursorAboveHeight;
            }

        } // end if

        // 计算弹出框的x坐标值。
        int x = 0;

        int barWidthHalf = barWidth / 2;
        if (start.x <= barWidthHalf) {

            x = super.configServiceSDK.getLeftRigthSpace();

        } else if (start.x > barWidthHalf) {

            if ((start.x + barWidth > (super.configServiceSDK.getScreenWidth() - super.configServiceSDK.getLeftRigthSpace()))) {

                x = super.configServiceSDK.getScreenWidth() - super.configServiceSDK.getLeftRigthSpace() - barWidth;

            } else {

                x = (super.configServiceSDK.getScreenWidth() - super.configServiceSDK.getLeftRigthSpace()) - barWidth;
            }
        }

        this.setPosition(x, y);

        super.relativeLayout.bringChildToFront(super.windowView);
    }

    @Override
    public void show() {

        // ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.4f, 0.0f,
        // 1.4f,
        // Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // this.myWindow.setAnimation(scaleAnimation);
        // scaleAnimation.setDuration(500);
        // scaleAnimation.start();

        this.windowView.setVisibility(View.VISIBLE);

        this.relativeLayout.bringChildToFront(this.windowView);
    }

    @Override
    public void hide() {

        this.windowView.setVisibility(View.INVISIBLE);

        setPosition(-10000, -10000);
    }

    public void onClick(View view) {

        BookStress bookStress = this.readerDynamicSDK.makeBookStressFromSelection(BookStress.StressType.StressLine);

		int id = view.getId();

		if (id == this.selection_panel_lineId) {

            bookStress.setStressType(BookStress.StressType.StressLine);

		    super.readerDynamicSDK.paintLine(bookStress);

            super.readerDynamicSDK.saveCloudStress(bookStress);

		} else if (id == this.selection_panel_stressId) {

            bookStress.setStressType(BookStress.StressType.Stress);

            super.readerDynamicSDK.paintStress(bookStress);

            super.readerDynamicSDK.saveCloudStress(bookStress);

		} else if (id == this.selection_panel_StressNoteId) {

            bookStress.setStressType(BookStress.StressType.StressNote);

            super.readerDynamicSDK.paintNote(bookStress);

            this.readerPopupService.hideCurrentPopup(); // 先把SelectionPopup隐掉，然后在显示

            this.readerDynamicSDK.stopSelection();
            this.readerDynamicSDK.repaint();

            this.readerPopupService.showCommentPanel(bookStress);

            return;

		} else if (id == this.selection_panel_copyId) {

            ClipboardManager myClipboard;
            myClipboard = (ClipboardManager) super.activity.getSystemService(Application.CLIPBOARD_SERVICE);

            myClipboard.setText(bookStress.getContent());

            ToastService toastService = new ToastService(super.activity);
            toastService.showMsg("复制成功");
		}

		else if (id == this.selection_panel_checkerrorId) {

            bookStress.setStressType(BookStress.StressType.correct);

            super.readerDynamicSDK.paintNote(bookStress);

            this.readerPopupService.hideCurrentPopup();

            this.readerDynamicSDK.stopSelection();
            this.readerDynamicSDK.repaint();

            this.readerPopupService.showCommentPanel(bookStress);

            return;

		} else if (id == this.selection_panel_sharedId) {

            ToastService toastService = new ToastService(super.activity);
            toastService.showMsg("在这里实现分享功能");

		}

        this.readerPopupService.hideCurrentPopup();

        this.readerDynamicSDK.stopSelection();
        this.readerDynamicSDK.repaint();

    }
}
