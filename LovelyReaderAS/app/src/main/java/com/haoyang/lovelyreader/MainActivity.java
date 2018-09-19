package com.haoyang.lovelyreader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.base.common.util.Size;
import com.app.base.exception.DeviceException;
import com.app.base.service.android.AndroidInfoService;
import com.haoyang.lovelyreader.entity.User;
import com.haoyang.lovelyreader.entity.UserBook;
import com.haoyang.lovelyreader.service.BookService;
import com.haoyang.lovelyreader.service.UserService;
import com.haoyang.reader.sdk.AnimationType;
import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.sdk.ColorService;
import com.haoyang.reader.sdk.DefaultCustomServivce;
import com.haoyang.reader.sdk.FeedBack;
import com.haoyang.reader.sdk.LineSpace;
import com.haoyang.reader.sdk.PageStyle;
import com.haoyang.reader.sdk.PageStyle.BackgroundFillMode;
import com.haoyang.reader.sdk.PageStyle.NightDayMode;
import com.haoyang.reader.sdk.PageStyle.Source;
import com.haoyang.reader.sdk.ReaderSDK;
import com.haoyang.reader.sdk.ReaderSDK.SDKInteractive;
import com.haoyang.reader.sdk.SDKParameterInfo;
import com.haoyang.reader.sdk.ShareEntity;
import com.haoyang.reader.service.bookservice.BookInfoService;
import com.java.common.utils.Utils;

/**
 * 到这一步就是数据都处理完了。就是显示。
 * 
 * @author tianyu
 *
 */
public class MainActivity extends Activity implements OnClickListener {

	// 声明四个Tab
	private LinearLayout mTabWeixin;
	private LinearLayout mTabFrd;
	// private LinearLayout mTabAddress;
	// private LinearLayout mTabSetting;

	// 声明四个ImageButton
	private ImageButton mWeixinImg;
	private ImageButton mFrdImg;
	// private ImageButton mAddressImg;
	// private ImageButton mSettingImg;

	// 声明ViewPager的适配器
	private PagerAdapter mAdpater;
	// 用于装载四个Tab的List
	private List<View> mTabs = new ArrayList<View>();
	private ViewPager mViewpager;
	private LinearLayout bookShelfLayout;
	private RelativeLayout userLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 去掉TitleBar
		setContentView(R.layout.activity_main);

		initViews();// 初始化控件
		initDatas();// 初始化数据
		initEvents();// 初始化事件

		initBookLayout();
	}

	private void initEvents() {

		// 设置四个Tab的点击事件
		mTabWeixin.setOnClickListener(this);
		mTabFrd.setOnClickListener(this);
		// mTabAddress.setOnClickListener(this);
		// mTabSetting.setOnClickListener(this);

		// 添加ViewPager的切换Tab的监听事件
		mViewpager.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				// 获取ViewPager的当前Tab
				int currentItem = mViewpager.getCurrentItem();
				// 将所以的ImageButton设置成灰色
				resetImgs();
				// 将当前Tab对应的ImageButton设置成绿色
				switch (currentItem) {
				case 0:
					mWeixinImg.setImageResource(R.drawable.tab_weixin_pressed);
					break;
				case 1:
					mFrdImg.setImageResource(R.drawable.tab_find_frd_pressed);
					break;
				// case 2:
				// mAddressImg.setImageResource(R.drawable.tab_address_pressed);
				// break;
				// case 3:
				// mSettingImg.setImageResource(R.drawable.tab_settings_pressed);
				// break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	private void initDatas() {
		// 初始化ViewPager的适配器
		mAdpater = new PagerAdapter() {
			@Override
			public int getCount() {
				return mTabs.size();
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View view = mTabs.get(position);
				container.addView(view);
				return view;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// super.destroyItem(container, position, object);
				container.removeView(mTabs.get(position));
			}
		};
		// 设置ViewPager的适配器
		mViewpager.setAdapter(mAdpater);
	}

	// 初始化控件
	private void initViews() {

		mViewpager = (ViewPager) findViewById(R.id.id_viewpager);

		mTabWeixin = (LinearLayout) findViewById(R.id.id_tab_weixin);
		mTabFrd = (LinearLayout) findViewById(R.id.id_tab_frd);
		// mTabAddress = (LinearLayout) findViewById(R.id.id_tab_address);
		// mTabSetting = (LinearLayout) findViewById(R.id.id_tab_setting);

		mWeixinImg = (ImageButton) findViewById(R.id.id_tab_weixin_img);
		mFrdImg = (ImageButton) findViewById(R.id.id_tab_frd_img);
		// mAddressImg = (ImageButton) findViewById(R.id.id_tab_address_img);
		// mSettingImg = (ImageButton) findViewById(R.id.id_tab_setting_img);

		// 获取到四个Tab
		LayoutInflater inflater = LayoutInflater.from(this);
		RelativeLayout bookShelfALLLayout = (RelativeLayout) inflater.inflate(
				R.layout.tab1, null);

		bookShelfLayout = (LinearLayout) bookShelfALLLayout
				.findViewById(R.id.aaaa);
		userLayout = (RelativeLayout) inflater.inflate(R.layout.tab2, null);

		// LayoutParams layoutParams = new RelativeLayout.LayoutParams(20, 20);
		// tab1.addView(child, layoutParams);

		// View tab3 = inflater.inflate(R.layout.tab3, null);
		// View tab4 = inflater.inflate(R.layout.tab4, null);

		// 将四个Tab添加到集合中
		mTabs.add(bookShelfALLLayout);
		// mTabs.add(userLayout);
		// mTabs.add(tab3);
		// mTabs.add(tab4);
	}

	private void clearBookLayout() {
		this.bookShelfLayout.removeAllViews();
	}

	private void initBookLayout() {

		BookService bookService = new BookService(this);
		List<UserBook> bookList = bookService.loadBooks(UserService.user);

		// for (int i = 0; i < 25; i++) {
		// UserBook b = new UserBook();
		//
		// b.bookName = "孙子兵法";
		// bookList.add(b);
		// } // end for i

		UserBook b = new UserBook();
		b.bookId = "-1";
		bookList.add(b);

		AndroidInfoService androidInfoService = new AndroidInfoService();
		com.app.base.common.util.Size screenSize = androidInfoService
				.getScreenSize(this);

		int width = 300;
		int height = 450;

		// 计算需要几列.
		int mod = screenSize.width % width;
		int column = screenSize.width / width;

		if (mod < 100) {
			column--;
		}

		int yu = screenSize.width - column * width;

		int space = yu / (column + 1);
		int x = space, y = 60;

		LayoutInflater inflater = LayoutInflater.from(this);

		RelativeLayout bookRowLayout = (RelativeLayout) inflater.inflate(
				R.layout.book_row, null);

		int index = 0;
		for (UserBook book : bookList) {

			FrameLayout bookItemLayout = (FrameLayout) inflater.inflate(
					R.layout.book_item, null);

			ImageView imageView = (ImageView) bookItemLayout
					.findViewById(R.id.bookCover);
			Bitmap imageBitmap = null;
			if (book.coverPath != null && !"".equals(book.coverPath)) {
				imageBitmap = BitmapFactory.decodeFile(book.coverPath);
			}
			if (imageBitmap == null) {

				Drawable drawable = this.getDrawable(R.drawable.cover);
				imageView.setImageDrawable(drawable);

				TextView bookNameNOCoverView = (TextView) bookItemLayout
						.findViewById(R.id.bookNameNOCover);
				bookNameNOCoverView.setText(book.bookName);
			} else {

				imageView.setImageBitmap(imageBitmap);
			}

			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					width, height);

			bookItemLayout.setX(x);
			bookItemLayout.setY(y);
			bookItemLayout.setTag(book);

			bookItemLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					UserBook book = (UserBook) v.getTag();
					if ("-1".equals(book.bookId)) {
						// 打开目录

						Intent intent = new Intent();
						intent.setClass(MainActivity.this,
								FileBrowseActivity.class);
						MainActivity.this.startActivityForResult(intent, 1);
						return;
					}

					Book willReadBook = new Book();

					willReadBook.path = book.bookPath;
					willReadBook.bookName = book.bookName;
					willReadBook.bookCover = book.coverPath;

					startReader(willReadBook, UserService.user);
				}
			});

			bookRowLayout.addView(bookItemLayout, layoutParams);

			x += width + space;
			index++;
			if (index >= column) {
				x = space;
				index = 0;

				this.bookShelfLayout.addView(bookRowLayout);
				bookRowLayout = (RelativeLayout) inflater.inflate(
						R.layout.book_row, null);
			}
		} // end for

		if (bookList.size() % column != 0) {

			this.bookShelfLayout.addView(bookRowLayout);
		}
	}

	@Override
	public void onClick(View v) {

		// 先将四个ImageButton都设置成灰色
		resetImgs();
		switch (v.getId()) {
		case R.id.id_tab_weixin:
			// 设置viewPager的当前Tab
			mViewpager.setCurrentItem(0);
			// 将当前Tab对应的ImageButton设置成绿色
			mWeixinImg.setImageResource(R.drawable.tab_weixin_pressed);
			break;
		case R.id.id_tab_frd:
			mViewpager.setCurrentItem(1);
			mFrdImg.setImageResource(R.drawable.tab_find_frd_pressed);
			break;
		// case R.id.id_tab_address:
		// mViewpager.setCurrentItem(2);
		// mAddressImg.setImageResource(R.drawable.tab_address_pressed);
		// break;
		// case R.id.id_tab_setting:
		// mViewpager.setCurrentItem(3);
		// mSettingImg.setImageResource(R.drawable.tab_settings_pressed);
		// break;
		}
	}

	// 将四个ImageButton设置成灰色
	private void resetImgs() {

		mWeixinImg.setImageResource(R.drawable.tab_weixin_normal);
		mFrdImg.setImageResource(R.drawable.tab_find_frd_normal);

		// mAddressImg.setImageResource(R.drawable.tab_address_normal);
		// mSettingImg.setImageResource(R.drawable.tab_settings_normal);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		final Bundle extras = data.getExtras();

		if (extras == null) {
			return;
		}

		String filePath = (String) extras.get("path");

		UserBook userBook = new UserBook();
		Book book = new Book();

		book.path = filePath;

		BookInfoService bookInfoService = new BookInfoService();
		bookInfoService.init(filePath);

		int result = bookInfoService.getBookInfo(book, "ePub");
		InputStream is = bookInfoService.getCoverInputStream(filePath);

		try {
			if (is != null) {

				String newFilePath = readBookImage(filePath, is);
				userBook.coverPath = newFilePath;
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		bookInfoService.clear();

		userBook.userId = UserService.user.userId;

		userBook.bookPath = filePath;
		userBook.bookName = book.bookName;
		userBook.times = System.currentTimeMillis();

		BookService bookService = new BookService(this);

		UserBook ub = bookService.loadBookByPath(filePath);
		if (ub != null) { // 提示已经以书架中了，

			return;
		}

		bookService.saveBook(userBook);

		this.clearBookLayout();
		this.initBookLayout();
	}

	public final static String defultPath = "";

	private String readBookImage(String filePath, InputStream is) {

		AndroidInfoService androidInfoService = new AndroidInfoService();
		String documentPath = "";
		try {

			documentPath = androidInfoService
					.getDownLoadPath(getApplicationContext());
		} catch (DeviceException e1) {
			e1.printStackTrace();
			return defultPath;
		}

		String newPath = documentPath + File.separator + "bookimage"
				+ File.separator;

		File file = new File(newPath);
		if (!file.exists()) {
			boolean r = file.mkdir();
			if (!r) {
				return defultPath;
			}
		}

		String md5Id = Utils.md5(filePath.getBytes());
		newPath += md5Id;

		file = new File(newPath);
		if (file.exists()) {
			file.delete();
		}

		OutputStream out = null;
		try {

			file.createNewFile();
			out = new FileOutputStream(file);
			byte[] buffer = new byte[1024];

			int len = 0;
			while ((len = is.read(buffer)) != -1) {

				out.write(buffer, 0, len);
			} // end while

			return newPath;
		} catch (IOException e) {
			e.printStackTrace();
			return defultPath;
		} finally {

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} // end finally
	}

	/**
	 * 启动阅读器进行阅读。
	 * 
	 * @param book
	 */
	private void startReader(Book book, User user) {

		final ReaderSDK readerSDK = ReaderSDK.getInstance();

		SDKParameterInfo parameter = new SDKParameterInfo();

		parameter.appId = "773278";
		parameter.appKey = "10f2a8b3759b4304a5414269c5c4bf63";

		parameter.userId = user.userId; // 接入时需提供应用下唯一的。否则会出现数据错乱。
		parameter.userName = user.nickName;

		readerSDK.initSDK(this, parameter);

		String[] wordColorArray = { "0x372d21", "0xb6bbbe", "0x322b23",
				"0x444444", "0x444444", "0x444444", "0x444444", "0x342d25",
				"0x293a27", "0x000200", "0x5f707a", "0x96938e" }; // 阅读器文字颜色，与背景资源是一一对应的。

		String[] wallPaperArray = { "wallpapers/background_2.jpg",
				"wallpapers/background_1.jpg", "wallpapers/background_3.jpg",
				"wallpapers/background_4.jpg", "wallpapers/background_5.jpg",
				"wallpapers/background_6.jpg", "wallpapers/background_7.jpg",
				"0xf6efe7", "0xceefd0", "0x596476", "0x001d28", "0x39312f" }; // 阅读器背景资源。

		BackgroundFillMode[] wallPaperType = { BackgroundFillMode.fullscreen,
				BackgroundFillMode.tileMirror, BackgroundFillMode.fullscreen,
				BackgroundFillMode.fullscreen, BackgroundFillMode.fullscreen,
				BackgroundFillMode.fullscreen, BackgroundFillMode.fullscreen,
				BackgroundFillMode.backgroundColor,
				BackgroundFillMode.backgroundColor,
				BackgroundFillMode.backgroundColor,
				BackgroundFillMode.backgroundColor,
				BackgroundFillMode.backgroundColor }; // 阅读器背景资源类型。

		NightDayMode[] nightDayMode = { NightDayMode.day, NightDayMode.none,
				NightDayMode.none, NightDayMode.none, NightDayMode.none,
				NightDayMode.none, NightDayMode.none, NightDayMode.none,
				NightDayMode.none, NightDayMode.none, NightDayMode.none,
				NightDayMode.night }; // 定义每种背景及文字是夜间模式还是白天模式。

		List<PageStyle> pageStyleList = new ArrayList<PageStyle>();

		for (int index = 0; index < wallPaperArray.length; index++) { // 生成样式对象。

			String background = wallPaperArray[index];

			PageStyle pageStyle = new PageStyle();
			pageStyle.styleName = background;
			pageStyle.backgroundValue = background;
			pageStyle.backgroundFillMode = wallPaperType[index];
			pageStyle.textColor = wordColorArray[index];
			pageStyle.catalogColor = wordColorArray[index];
			pageStyle.pageNumberColor = wordColorArray[index];
			pageStyle.timeColor = wordColorArray[index];
			pageStyle.nightDayMode = nightDayMode[index];

			pageStyle.source = Source.DEVELOPER;
			pageStyleList.add(pageStyle);
		} // end for index

		// 设置默认的样式，这个方法需要在setPageStyle方法之前执行
		//
		readerSDK.setCurrentPageStyle("wallpapers/background_4.jpg");

		// 将样式对象列表传给阅读器，阅读器根据这个来定义背景切换。
		// 如果在之前没有执行setCurrentPageStyle这个方法，setPageStyle就会把第一个做为默认样式
		readerSDK.setPageStyle(pageStyleList);

		// readerSDK.setTextSize(textSize);
		// ---------------------------------------------------------

		readerSDK.setAnimationType(AnimationType.SlideAnimation); // 设置默认翻页动画。

		readerSDK.setAnimationSpeed(10); // 设置翻页动画移动时，每次移动的距离基础数字。
		readerSDK.setAnimationSpeedFactor(1.2f); // 设置翻页动画速度不断加快的因子。

		readerSDK.setTypefaceSwitchEnable(true); // 是否开启字体切换功能。

		Typeface typeface = Typeface.createFromAsset(getAssets(), "yahei.ttf");
		readerSDK.setDefaultTypeface(typeface); // 设置当前的默认字体。

		// 设置阅读的各种属性.
		readerSDK.setUpDownSpace(100); // 设置阅读器上下屏幕边与文字之间的间距。
		readerSDK.setLeftRightSpace(50); // 设置阅读器左右屏幕边与文字之间的间距。

		readerSDK.setLineSpacing(LineSpace.lineSpaceMiddle); // 设置当前行间距。

		readerSDK.setStressLineThickness(3); // 设置对文字画线时线条的粗细程度。
		readerSDK.setStressLineColor(new ColorService(230, 45, 150)); // 设置对文字画线时线条的颜色。

		readerSDK.setShowCatalog(); // 设置阅读器显示目录。
		// readerSDK.setHideCatalog(); // 设置阅读器隐藏目录。

		readerSDK.setShowBattery(true); // 设置阅读器显示电池。
		readerSDK.setShowPageNumber(true); // 设置阅读器显示页码。
		readerSDK.setShowTime(true); // 设置阅读器显示当前时间。

		readerSDK.setShareEnable(false); // 是否需要分享功能。

		readerSDK.setTypefaceSwitchEnable(false); // 是否需要有切换字体功能。

		// 设置阅读器内部一些功能由开发者来实现的接口。
		// 这里需要开发者实现SDKInteractive接口。

		readerSDK.setSDKInteractive(new SDKInteractive() {

			@Override
			public void share(ShareEntity shareEntity) { // 打开分享界面

				// PageStarterService pageStarterService = new
				// PageStarterService(
				// MainActivity.this);
				// pageStarterService.nextPage(TestActivity.class);
			}

			@Override
			public void typefaceSwitch() { // 打开切换字体界面。

				// PageStarterService pageStarterService = new
				// PageStarterService(
				// MainActivity.this);
				// pageStarterService.nextPage(TestActivity.class);
			}

			@Override
			public void toolsBar(RelativeLayout relativeLayout) {

				// view.setBackgroundColor(Color.BLUE);

				LayoutInflater mInflater = LayoutInflater
						.from(MainActivity.this);
				View contentView = mInflater.inflate(R.layout.custom, null);

				relativeLayout.addView(contentView);

				View view = contentView.findViewById(R.id.pinglun);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Toast toast = Toast.makeText(MainActivity.this,
								"评论在这里展示", Toast.LENGTH_LONG);
						toast.show();
					}
				});
				view = contentView.findViewById(R.id.moneny);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Toast toast = Toast.makeText(MainActivity.this,
								"购买在这里实现", Toast.LENGTH_LONG);
						toast.show();
					}
				});
			}

			@Override
			public void feedback(FeedBack feedBack) {
				Toast toast = Toast.makeText(MainActivity.this, "开发者在这里接收意见反馈",
						Toast.LENGTH_LONG);
				toast.show();
			}

			@Override
			public void decrypt(String sourceFilePath, String targetFilePath) {

			}
		});

		readerSDK.setCustomServivce(new CustomServiceImpl());

		// book.bookName = "用人之道";
		// book.id = "123456"; // 这个值是不同的电子书，id 的值是不一样的。

		String id = book.path;

		if (book.path != null) {
			int index = book.path.lastIndexOf(File.separator);
			if (index != -1) {
				id = book.path.substring(index + 1);
			}
		}

		// =============================================================================

		String md5Id = Utils.md5(id.getBytes());
		// book.id = md5Id;

		book.id = id;

		// AndroidInfoService info = new AndroidInfoService();
		// String abc = info.getPath(MainActivity1.this);

		// FileNameService fileNameService = new FileNameService();
		// fileNameService.getFileNameFromAddress(book.path);

		// Log.d("aaaa", "bookId : " + book.id);
		// Log.d("aaaa", "bookPath : " + book.path);
		// Log.d("aaaa", "userId : " + parameter.userId);

		try {

			readerSDK.startReader(MainActivity.this, book,
					ReaderSDK.FilePathType.Local_File);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图片处理方式，开发者自定义。
	 * 
	 * @author tianyu912@yeah.net
	 *
	 *         Mar 10, 2018
	 */
	class CustomServiceImpl extends DefaultCustomServivce {

		@Override
		public Size getImageSize(float imageWidth, float imageHeight,
				float readAdeaWidth, float readAreaHeight) {

			float height = 0.0f, width = 0.0f;

			float unit = readAdeaWidth / 4.0f;

			if (imageWidth <= unit) {

				width = unit;
				float c = width / imageWidth;
				height = c * imageHeight;
			} else {

				width = readAdeaWidth;
				float c = width / imageWidth;
				height = c * imageHeight;
			}

			if (height > readAreaHeight) {

				float rr = readAreaHeight / height;
				height = (int) (rr * height);
				width = width * rr;
			}

			Size size = new Size((int) width, (int) height);
			return size;
		}
	}
}
