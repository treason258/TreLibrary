package com.haoyang.reader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;

import android.view.WindowManager;

import com.app.base.service.android.AndroidInfoService;
import com.haoyang.lovelyreader.R;
import com.haoyang.reader.sdk.AnimationType;
import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.sdk.LineSpace;
import com.haoyang.reader.sdk.ReaderPageStyle;
import com.haoyang.reader.sdk.ReaderPageStyle.BackgroundMode;
import com.haoyang.reader.sdk.ReaderPageStyle.NightDayMode;

import com.haoyang.reader.sdk.ReaderSDK;

import com.haoyang.reader.sdk.BookMetaService;

import com.java.common.service.file.FileNameService;

/**
 * 阅读器SDK Demo
 * Demo设计思路: 先把assert/book中的电子书拷到指定目录下，
 *              然后读取电子书的元数据，并以列表的形式显示出来.
 */
public class MainActivity extends AppCompatActivity {

    private BookAdapter bookAdapter;

    private List<String> epubFileList = new ArrayList<String>();
    private List<Book> bookList = new ArrayList<Book>();

    private ProgressBar progressBar;

    // 电子书列表，电子书文件存放在assert/book中

    static Map<String, String> pathToFileNameMap = new HashMap<String, String>();

    static {

        pathToFileNameMap.put("不能说的秘密111.epub", "不能说的秘密111");
        pathToFileNameMap.put("大校的女儿111.epub", "大校的女儿111");
        pathToFileNameMap.put("盗墓笔记222.epub", "盗墓笔记222");
        pathToFileNameMap.put("阿甘正传111.epub", "阿甘正传111");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo_layout);

        ActionBar bar = this.getActionBar();
        if (bar != null) {
            bar.hide();
        }
//
//                View decorView = this.getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
//
//        decorView.setSystemUiVisibility(uiOptions);
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        ListView bookListView = (ListView) this.findViewById(R.id.bookList);

        bookListView.setFadingEdgeLength(100); // fadingEdgeLength用来设置边框渐变的长度。
        bookListView.setVerticalFadingEdgeEnabled(true); // scrollView上下滚item淡入淡出效果

        bookAdapter = new BookAdapter(bookList);
        bookListView.setAdapter(bookAdapter);
        bookListView.setOnItemClickListener(bookAdapter);

        this.loadBook();
    }

    private void loadBook() {

        new Thread() {

            public void run() {

                AndroidInfoService androidInfoService = new AndroidInfoService();
                String path = androidInfoService.getPath(MainActivity.this);

                copyFile(path);

                for (String epubFilePath : epubFileList) {

                    readBookMeta(epubFilePath);
                }

                MainActivity.this.runOnUiThread(new Runnable() {

                    public void run() {

                        bookAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

        }.start();
    }

    /**
     * 启动阅读器进行阅读。
     *
     * @param book
     */
    private void startReader(Book book, String userId) {

        final ReaderSDK readerSDK = ReaderSDK.getInstance();

        readerSDK.initSDK(book, userId, this);

        String[] wordColorArray = {"0x372d21",
                "0xb6bbbe",
                "0x322b23",
                "0x444444",
                "0x444444",
                "0x444444",
                "0x444444",
                "0x342d25",
                "0x293a27",
                "0x000200",
                "0x5f707a",
                "0x96938e"}; // 阅读器文字颜色，与背景资源是一一对应的。
        String[] lineColorArray = {"0x472d21", "0xc6bbbe", "0x422b23",
                "0x544444", "0x544444", "0x544444", "0x544444", "0x544444",
                "0x393a27", "0x100200", "0x6f707a", "0xa6938e"}; // 阅读器文字颜色，与背景资源是一一对应的。

        String[] wallPaperArray = {"wallpapers/background_2.jpg",
                "wallpapers/background_1.jpg", "wallpapers/background_3.jpg",
                "wallpapers/background_4.jpg", "wallpapers/background_5.jpg",
                "wallpapers/background_6.jpg", "wallpapers/background_7.jpg",
                "0xf6efe7", "0xceefd0", "0x596476", "0x001d28", "0x39312f"}; // 阅读器背景资源。

        BackgroundMode[] wallPaperType = {BackgroundMode.fullscreen,
                BackgroundMode.tile,
                BackgroundMode.fullscreen,
                BackgroundMode.fullscreen,
                BackgroundMode.fullscreen,
                BackgroundMode.fullscreen,
                BackgroundMode.fullscreen,
                BackgroundMode.backgroundColor,
                BackgroundMode.backgroundColor,
                BackgroundMode.backgroundColor,
                BackgroundMode.backgroundColor,
                BackgroundMode.backgroundColor}; // 阅读器背景资源类型。

        NightDayMode[] nightDayMode = {NightDayMode.day, NightDayMode.none,
                NightDayMode.none, NightDayMode.none, NightDayMode.none,
                NightDayMode.none, NightDayMode.none, NightDayMode.none,
                NightDayMode.none, NightDayMode.none, NightDayMode.none,
                NightDayMode.night}; // 定义每种背景及文字是夜间模式还是白天模式。

        List<ReaderPageStyle> pageStyleList = new ArrayList<ReaderPageStyle>();

        for (int index = 0; index < wallPaperArray.length; index++) { // 生成样式对象。

            String background = wallPaperArray[index];

            ReaderPageStyle pageStyle = new ReaderPageStyle();
            pageStyle.styleName = background;
            pageStyle.backgroundValue = background;
            pageStyle.backgroundMode = wallPaperType[index];
            pageStyle.textColor = wordColorArray[index];
            pageStyle.catalogColor = wordColorArray[index];
            pageStyle.pageNumberColor = wordColorArray[index];
            pageStyle.timeColor = wordColorArray[index];

            pageStyle.stressColor = "0x00ffff";

            pageStyle.stressLineColor = lineColorArray[index];
            pageStyle.stressLineThickness = 3;                // 设置对文字画线时线条的粗细程度。
            pageStyle.hyperlinkTextColor = "0xff0000";        // 设置对文字画线时线条的颜色。

            // 设置界面上的各种标志
            pageStyle.selectTextAboveIconId = R.drawable.select_word_above;
            pageStyle.selectTextBelowIconId = R.drawable.select_word_below;
            pageStyle.bookmarkIconId = R.drawable.page_bookmark;
            pageStyle.noteLabelIconId = R.drawable.note_label;

            pageStyle.nightDayMode = nightDayMode[index];

            pageStyleList.add(pageStyle);
        } // end for index

        // 设置默认的样式，这个方法需要在setPageStyle方法之前执行
        readerSDK.setCurrentPageStyle("wallpapers/background_3.jpg");

        // 将样式对象列表传给阅读器，阅读器根据这个来定义背景切换。
        // 如果在之前没有执行setCurrentPageStyle这个方法，setPageStyle就会把第一个做为默认样式
        readerSDK.setPageStyle(pageStyleList);

        // ---------------------------------------------------------

        // 设置阅读的各种属性.
        readerSDK.setUpDownSpace(100); // 设置阅读器上下屏幕边与文字之间的间距。
        readerSDK.setLeftRightSpace(50); // 设置阅读器左右屏幕边与文字之间的间距。

        readerSDK.setDefaultTextSize(30);
        readerSDK.setDefaultLineSpace(LineSpace.lineSpaceMiddle.getValue()); // 设置当前行间距。

        readerSDK.setShowTime(true);
        readerSDK.setShowPageNumber(true);
        readerSDK.setShowBattery(true);

        readerSDK.setReaderEventClassPath("com.haoyang.reader.ReaderEventDeveloper");

        try {

            readerSDK.startReader(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readBookMeta(String filePath) {

        try {

            final Book book = new Book();
            book.bookPath = filePath;

            BookMetaService bookMetaService = new BookMetaService(book);

            bookMetaService.open();

            int result = bookMetaService.getBookInfo();

            Log.d("aa", "result : " + result);

            bookMetaService.close();


            if (book.bookName == null || "".equals(book.bookName)) { // 有的电子书取不到书名.

                FileNameService fns = new FileNameService();

                String fileName = fns.getFileNameFromAddress(filePath);
                String chineseName = pathToFileNameMap.get(fileName);

                book.bookName = chineseName;
            } else {

                book.bookName = book.bookName + "  " + book.authors + "   " + book.publisher;
            }

            book.bookType = Book.BookType.EPUB;

            bookList.add(book);

        } catch (Exception e) {

            e.printStackTrace();
            return;
        }
    }

    /**
     * 电子书列表.
     */
    class BookAdapter extends BaseAdapter implements
            AdapterView.OnItemClickListener {

        List<Book> bookList;

        private int currentSelectPosition = -1;
        private View preView = null;

        BookAdapter(List<Book> bookList) {
            this.bookList = bookList;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            final View view = (convertView != null) ? convertView
                    : LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.demo_book_item, parent, false);

            if (position == currentSelectPosition) {

                view.setBackgroundColor(Color.parseColor("#ffce42"));
            } else {

                view.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            Book book = this.bookList.get(position);

            ImageView bookIcon = (ImageView) view.findViewById(R.id.bookmark_item_icon);

            BookMetaService bookMetaService = new BookMetaService(book);

            bookMetaService.open();

            byte[] coverImagedata = bookMetaService.getCoverData();

            if (coverImagedata != null) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(coverImagedata, 0, coverImagedata.length);

                if (bitmap != null) {
                    bookIcon.setImageBitmap(bitmap);
                }
            }

            TextView title = (TextView) view.findViewById(R.id.booktitle);
            title.setText(book.bookName);

            TextView epubPathTextView = (TextView) view.findViewById(R.id.epubPath);
            epubPathTextView.setText(book.bookPath);

            return view;
        }

        @Override
        public int getCount() {
            return this.bookList.size();
        }

        @Override
        public Object getItem(int position) {
            Book book = this.bookList.get(position);
            return book;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

			currentSelectPosition = position;

			view.setBackgroundColor(Color.parseColor("#ffce42"));

			Book book = this.bookList.get(position);

            book.bookId = "141";

            book.userId = "3"; // 接入时需提供应用下唯一的。否则会出现数据错乱。

            String usreId = "3";

            startReader(book, usreId);
        }
    }

    // 下边是拷贝文件 ---------------------------------------------------------------------------------

    private void copyFile(String targetPath) {

        AssetManager assetManager = this.getAssets();

        String[] epubList = null;
        try {

            epubList = this.getAssets().list("book");
        } catch (IOException e1) {

            e1.printStackTrace();
        }

        if (epubList == null) {
            return;
        }

        for (String epubPath : epubList) {

            String epubTargetPath = targetPath + File.separator + epubPath;

            File file = new File(epubTargetPath);
            if (file.exists()) {
                epubFileList.add(file.getAbsolutePath());
                continue;
            }

            try {

                InputStream inputStream = assetManager.open("book"
                        + File.separator + epubPath);

                if (inputStream == null) {
                    return;
                }

                writeBytesToFile(inputStream, file);
                epubFileList.add(file.getAbsolutePath());

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

    private static void writeBytesToFile(InputStream is, File file)
            throws IOException {

        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file);
            while ((nbread = is.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
        } catch (Exception ex) {
            Log.d("tag", ex.getMessage());
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

}
