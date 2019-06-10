package com.haoyang.reader;

import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.view.View;
import android.view.WindowManager;

import com.app.base.common.util.Size;
import com.app.base.service.android.AndroidInfoService;
import com.haoyang.lovelyreader.R;
import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.sdk.BookCatalog;
import com.haoyang.reader.sdk.ReaderDynamicSDK;

import com.haoyang.reader.ui.NavigationDrawerFragment;
import com.haoyang.reader.popup.ReaderPopupService;

public class ReaderLayoutService implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private Book book;

    private FragmentActivity fragmentActivity;

    private NavigationDrawerFragment navigationDrawerFragment;

    public void init(Book book,
                     ReaderDynamicSDK readerDynamicSDK,
                     ReaderPopupService readerPopupService) {

        this.book = book;

        this.navigationDrawerFragment.init(book, readerDynamicSDK, readerPopupService);
    }

    public RelativeLayout initReaderLayout(FragmentActivity fragmentActivity) {

        this.fragmentActivity = fragmentActivity;

        this.fragmentActivity.setContentView(R.layout.hy_reader_main);

        this.navigationDrawerFragment = (NavigationDrawerFragment) fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

		DrawerLayout drawerLayout = (DrawerLayout)fragmentActivity.findViewById(R.id.drawer_layout);

		navigationDrawerFragment.setUp(R.id.navigation_drawer, drawerLayout);
        navigationDrawerFragment.close();

        navigationDrawerFragment.setNavigationDrawerCallback(this);

        AndroidInfoService androidInfo = new AndroidInfoService();
        Size size = androidInfo.getScreenSize(fragmentActivity);

        RelativeLayout relativeLayout = (RelativeLayout)this.fragmentActivity.findViewById(R.id.container);

        return relativeLayout;
    }

    // --------------------------------------------------------------------------------

    public void setCatalogEnable(boolean enable) {

        this.navigationDrawerFragment.setCatalogEnable(enable);
    }

    public void showCatalogPage() {

        this.navigationDrawerFragment.showCatalogPage();
    }

    public void showNotePage() {

        this.navigationDrawerFragment.showNotePage();
    }

    public void showBookmarkPage() {

        this.navigationDrawerFragment.showBookmarkPage();
    }

    // --------------------------------------------------------------------------------

    public void updateCatalog(BookCatalog catalogRoot) {

        this.navigationDrawerFragment.updateCatalog(catalogRoot);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

}
