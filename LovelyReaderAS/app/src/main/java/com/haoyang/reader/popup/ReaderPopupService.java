package com.haoyang.reader.popup;

import android.app.Activity;
import android.graphics.Point;

import android.widget.RelativeLayout;

import com.haoyang.reader.ReaderLayoutService;

import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.sdk.BookCatalog;
import com.haoyang.reader.sdk.ConfigServiceSDK;
import com.haoyang.reader.sdk.ReaderDynamicSDK;
import com.haoyang.reader.sdk.BookStress;
import com.haoyang.reader.service.text.struct.entity.ElementArea;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ReaderPopupService {

    private Activity activity;

    private RelativeLayout relativeLayout;

    private Book book;

    private boolean canReader;
    private boolean canUpdateConfig;    // 是否可以更改配置

    private ConfigServiceSDK configServiceSDK;
    private ReaderDynamicSDK readerDynamicSDK;

    private ReaderLayoutService readerLayoutService;

    public final HashMap<String, PopupPanel> popupMap = new HashMap<String, PopupPanel>();

    private List<PopupPanel> popupPanelList;

    public ReaderPopupService(ReaderLayoutService readerLayoutService, Book book, Activity activity) {

        this.activity = activity;
        this.book = book;

        this.readerLayoutService = readerLayoutService;

        this.popupPanelList = new ArrayList<>();
    }

    public void initData(BookCatalog catalogRoot, List<BookCatalog> bookCatalogList) {

        MenuPopup menuPopup = (MenuPopup) this.getPopupById(MenuPopup.ID);

        menuPopup.setBookCatalogList(bookCatalogList);

        this.readerLayoutService.updateCatalog(catalogRoot);
    }

    public void showCatalogPage() {

        this.hideCurrentPopup();

        this.readerLayoutService.showCatalogPage();
    }


    public void showNotePage() {

        this.hideCurrentPopup();

        this.readerLayoutService.showNotePage();
    }

    public void showBookmarkPage() {

        this.hideCurrentPopup();

        this.readerLayoutService.showBookmarkPage();
    }

    // ----------------------------------------------------------------------

    public void canReader(boolean canReader) {

        this.canReader = canReader;
    }
    public boolean getCanReader() {

        return this.canReader;
    }

    /**
     * 显示选择内容后的弹出面板。
     */
    public void showSelectionPanel(Point point) {

        SelectionPopup selectionPopup = (SelectionPopup) this
                .getPopupById(SelectionPopup.ID);

        this.showPopup(SelectionPopup.ID);

        selectionPopup.followSelectionCoordinate(); // 菜单移动到指定的位置上。
    }

    /**
     * 显示选择内容后的弹出面板。
     */
    public void showSelectionDeletePanel(BookStress bookStress, Point point) {

        SelectionDeletePopup selectionDeletePopup = (SelectionDeletePopup) this
                .getPopupById(SelectionDeletePopup.ID);

        selectionDeletePopup.setBookStress(bookStress);
        this.showPopup(SelectionDeletePopup.ID);

        selectionDeletePopup.followSelectionCoordinate(point); // 菜单移动到指定的位置上。
    }

    /**
     * 显示选择内容后的弹出面板。
     */
    public void showNotePanel(BookStress bookStress) {

        NotePopup notePopup = (NotePopup) this
                .getPopupById(NotePopup.ID);

        notePopup.setBookStress(bookStress);

        notePopup.setPosition(0, 0);
        notePopup.followSelectionCoordinate(); // 菜单移动到指定的位置上。

        this.showPopup(NotePopup.ID);
    }

    /**
     * 点菜单中的批注按钮后,弹出输入批注内容的面板。
     */
    public void showCommentPanel(BookStress bookStress) {

        CommonPublishPopup commonPublishPopup = (CommonPublishPopup) this
                .getPopupById(CommonPublishPopup.CommonPublishId);

        commonPublishPopup.setBookStress(bookStress);

        commonPublishPopup.setPosition(0, 0); // 菜单移动到指定的位置上。

        this.showPopup(CommonPublishPopup.CommonPublishId);
    }

    /**
     * 点菜单中的批注按钮后,弹出输入批注内容的面板。
     */
    public void showImagePanel(ElementArea elementArea) {

        ImagePopup imagePopup = (ImagePopup) this
                .getPopupById(ImagePopup.ID);

        imagePopup.elementArea = elementArea;

        this.showPopup(ImagePopup.ID);
        // imagePopup.setPosition(0, 0);
    }

    /**
     * 显示选择内容后的弹出面板。
     */
    public void showFeedBackPopup() {

        FeedBackPopup feedBackPopup = (FeedBackPopup) this
                .getPopupById(FeedBackPopup.ID);

        // feedBackPopup.setPosition(0, 0);

        this.showPopup(FeedBackPopup.ID);
    }

    /**
     * 显示选择内容后的弹出面板。
     */
    public void showWordPanel() {

        WordPopup wordPopup = (WordPopup) this.getPopupById(WordPopup.ID);

        wordPopup.setPosition(0, 0);

        this.showPopup(WordPopup.ID);
    }

    /**
     * 显示选择内容后的弹出面板。
     */
    public void showMenuPanel() {

        MenuPopup menuPopup = (MenuPopup) this.getPopupById(MenuPopup.ID);

        menuPopup.setPosition(0, 0);

        this.showPopup(MenuPopup.ID);
    }

    /**
      * 初始化各个组件，并与BookService建议连接。
      */
    public void initPopup(RelativeLayout relativeLayout,
                          ConfigServiceSDK configServiceSDK,
                          ReaderDynamicSDK readerDynamicSDK) {

        this.relativeLayout = relativeLayout;

        this.popupPanelList.clear();
        this.popupMap.clear();

        if (this.getPopupById(SelectionDeletePopup.ID) == null) {

            SelectionDeletePopup selectionDeletePopup = new SelectionDeletePopup(this);
        }

        if (this.getPopupById(SelectionPopup.ID) == null) {

            SelectionPopup selectionPopup = new SelectionPopup(this);
        }

        if (this.getPopupById(NotePopup.ID) == null) {

            NotePopup notePopup = new NotePopup( this);
        }

        if (this.getPopupById(MenuPopup.ID) == null) {

            new MenuPopup(this);
        }

        // 笔记 和 纠错
        if (this.getPopupById(CommonPublishPopup.CommonPublishId) == null) {

            CommonPublishPopup commonPublishPopup = new CommonPublishPopup(CommonPublishPopup.CommonPublishId, this);
        }

        if (this.getPopupById(FeedBackPopup.ID) == null) {

            FeedBackPopup feedBackPopup = new FeedBackPopup( this);
        }

        if (this.getPopupById(WordPopup.ID) == null) {

            new WordPopup(this);
        }

        Collection<PopupPanel> panels = this.popupPanels();
        for (PopupPanel panpel : panels) {

            panpel.setConfigServiceSDK(configServiceSDK);
            panpel.setReaderDynamicSDK(readerDynamicSDK);

            panpel.init(activity, this.relativeLayout);
        }
    }

    public final void parseBookFinish(boolean isParseBookFinish) {

        Collection<PopupPanel> panels = this.popupPanels();
        for (PopupPanel panpel : panels) {

            panpel.parseBookFinish(isParseBookFinish);
        }
    }

    public final boolean isActivePopup() {

        return this.popupPanelList.size() > 0;
    }

    public final boolean isExistSelectionPopup() {

        if (this.popupPanelList.size() == 0) {
            return false;
        }

        for (PopupPanel popupPanel : this.popupPanelList) {

            if (popupPanel instanceof SelectionPopup) {

                return true;
            }
        }

        return false;
    }

    public void putPopup(String id, PopupPanel popup) {

        this.popupMap.put(id, popup);
    }

    public final PopupPanel getPopupById(String id) {

        return popupMap.get(id);
    }

    public final Collection<PopupPanel> popupPanels() {

        return popupMap.values();
    }

    public final void hideCurrentPopup() {

        if (this.popupPanelList.size() == 0) {
            return;
        }

        for (PopupPanel popupPanel : this.popupPanelList) {

            popupPanel.hide();
        }

        this.popupPanelList.clear();
    }

    public final void showPopup(String id) {

        PopupPanel popupPanel = getPopupById(id);

        this.popupPanelList.add(popupPanel);

        popupPanel.show();
    }

}