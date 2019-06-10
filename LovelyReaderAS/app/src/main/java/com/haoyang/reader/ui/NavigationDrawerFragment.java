package com.haoyang.reader.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.base.service.android.AndroidAppService;

import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.sdk.BookCatalog;
import com.haoyang.reader.sdk.BookStress;
import com.haoyang.reader.sdk.BookStressQuery;

import com.haoyang.reader.sdk.ReaderDynamicSDK;

import com.haoyang.reader.ui.swipyrefreshlayout.SwipyRefreshLayout;
import com.haoyang.reader.ui.swipyrefreshlayout.SwipyRefreshLayoutDirection;

import com.haoyang.reader.popup.ReaderPopupService;

import com.haoyang.reader.utils.ViewUtil;
import com.java.common.service.DateFormatService;
import com.java.common.utils.DateUtils;

public class NavigationDrawerFragment extends Fragment {

	private boolean catalogEnable; // 目录是否可以使用

	private BookCatalog catalogRoot;

	private NavigationDrawerCallbacks navigationDrawerCallback;

	private DrawerLayout mDrawerLayout;

	private ListView catalogListView;
	private ListView bookmarkListView;

	private SwipyRefreshLayout swipyRefreshView;

	private View noContentView;
	private TextView noContentTextView, noContentTextSubView;

	private BookCatalogAdapter catalogAdapter;
	private NoteAdapter noteAdapter;
	private BookmarkAdapter bookmarkAdapter;

	private int currentViewId;

	private AndroidAppService androidAppService;

	private View mFragmentContainerView;

	private View[] contentViewArray, naviArray, naviIndicatorArray;

	private TextView[] naviTextArray;

	private int[] naviIdArray;

	private int mCurrentSelectedPosition = 0;

	private int catalogNaviId;
	private int bookmarkNaviId;
	private int noteNaviId;

	private Book book;

	private ReaderPopupService readerPopupService;
	private ReaderDynamicSDK readerDynamicSDK;

	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	/**
	 * 初始化参数和数据
	 * @param book
	 * @param readerDynamicSDK
	 * @param readerPopupService
	 */
	public void init(Book book, ReaderDynamicSDK readerDynamicSDK, ReaderPopupService readerPopupService) {

		this.book = book;
		this.readerDynamicSDK = readerDynamicSDK;
		this.readerPopupService = readerPopupService;

		catalogAdapter = new BookCatalogAdapter(getActivity(), catalogListView);
		noteAdapter = new NoteAdapter(getActivity(), swipyRefreshView);
		bookmarkAdapter = new BookmarkAdapter(getActivity(), this.bookmarkListView);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.androidAppService = new AndroidAppService(this.getActivity());

		int layoutId = androidAppService.getLayoutResource("hy_navigation");
		View view = inflater.inflate(layoutId, container, false);
		view.setOnClickListener(null);

		// 导航内容view
		int catalogListId = androidAppService.getIdResource("catalogListHY");
		catalogListView = (ListView) view.findViewById(catalogListId);

		int bookmarkListId = androidAppService.getIdResource("bookmarkListHY");
		bookmarkListView = (ListView) view.findViewById(bookmarkListId);

		// int hideItemId = androidAppService.getIdResource("hideItem");
		// int showItemId = androidAppService.getIdResource("showItem");
		//
		// bookmarkListView.setShowId(showItemId);
		// bookmarkListView.setHideId(hideItemId);

		// int bookmarkDeleteId =
		// androidAppService.getIdResource("bookmarkDelete");
		// int[] functionIds = {bookmarkDeleteId};
		// bookmarkListView.setFunctionIds(functionIds);

		int swipyrefresh = androidAppService.getIdResource("swipyrefreshHY");
		swipyRefreshView = (SwipyRefreshLayout) view.findViewById(swipyrefresh);

		contentViewArray = new View[] { catalogListView, bookmarkListView,
				swipyRefreshView };

		// 没有内容的提示
		int noContent = androidAppService.getIdResource("noContentHY");
		noContentView = view.findViewById(noContent);

		int noContentTextId = androidAppService
				.getIdResource("noContentTextHY");
		noContentTextView = (TextView) view.findViewById(noContentTextId);

		int noContentTextSubId = androidAppService
				.getIdResource("noContentTextSubHY");
		noContentTextSubView = (TextView) view.findViewById(noContentTextSubId);

		// 导航按钮.
		catalogNaviId = androidAppService.getIdResource("catalogHY");
		bookmarkNaviId = androidAppService.getIdResource("bookmarkHY");
		noteNaviId = androidAppService.getIdResource("noteHY");

		naviIdArray = new int[] { catalogNaviId, bookmarkNaviId, noteNaviId };

		int catalogTextId = androidAppService.getIdResource("catalogTextHY");
		int bookmarTextId = androidAppService.getIdResource("bookmarkTextHY");
		int noteTextId = androidAppService.getIdResource("noteTextHY");

		int[] naviTextIdArray = new int[] { catalogTextId, bookmarTextId,
				noteTextId };

		int catalogIndicatorId = androidAppService
				.getIdResource("catalogIndicatorHY");
		int bookmarIndicatorId = androidAppService
				.getIdResource("bookmarkIndicatorHY");
		int noteIndicatorId = androidAppService
				.getIdResource("noteIndicatorHY");

		int[] naviIndicatorIdArray = new int[] { catalogIndicatorId, bookmarIndicatorId, noteIndicatorId };

		naviTextArray = new TextView[naviIdArray.length];
		naviIndicatorArray = new View[naviIdArray.length];
		naviArray = new View[naviIdArray.length];

		for (int index = 0; index < naviArray.length; index++) {

			naviTextArray[index] = (TextView) view
					.findViewById(naviTextIdArray[index]);
			naviIndicatorArray[index] = view
					.findViewById(naviIndicatorIdArray[index]);

			naviArray[index] = view.findViewById(naviIdArray[index]);
			naviArray[index].setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					setListViewShow(v.getId());
				}
			});
		} // end for index

		// setListViewShow(this.catalogNaviId); // 默认显示目录页。
		currentViewId = this.catalogNaviId;
		return view;
	}

	public void setCatalogEnable(boolean enable) {

		this.catalogEnable = enable;
	}

	public void showCatalogPage() {

		if (this.catalogEnable == false) {

			noContentView.setVisibility(View.VISIBLE);
			noContentTextView.setText("目录解析中");

			return;
		}

		if (this.catalogAdapter == null) {
			return;
		}

		this.open();

		this.bookmarkAdapter.reLoadData();

		this.noteAdapter.reLoadData();

		setListViewShow(this.catalogNaviId); // 设置当前显示内容为目录页。

		if (this.catalogRoot != null) {

			if (this.catalogRoot.subCatalogList().size() == 0) {

				noContentView.setVisibility(View.VISIBLE);
				noContentTextView.setText("本书没有目录！！");

				return;
			}
		}

		this.catalogAdapter.setCatalogForCurrent(); // 设置当前正在阅读页面所在的目录高亮显示。

		noContentView.setVisibility(View.GONE);
	}

	public void showNotePage() {

		if (this.noteAdapter == null) {
			return;
		}

		this.open();

		setListViewShow(this.noteNaviId); // 设置当前显示内容为笔记页。
		this.bookmarkAdapter.reLoadData();
		this.noteAdapter.reLoadData();
	}

	public void showBookmarkPage() {

		if (this.bookmarkAdapter == null) {
			return;
		}

		this.open();

		setListViewShow(this.bookmarkNaviId); // 设置当前显示内容为书签页。

		this.bookmarkAdapter.reLoadData();
		this.noteAdapter.reLoadData();
	}

	private void setListViewShow(int viewId) {

		for (int index = 0; index < naviIdArray.length; index++) {

			int id = naviIdArray[index];
			View view = contentViewArray[index];
			if (viewId == id) {

				view.setVisibility(View.VISIBLE);
				naviTextArray[index].setTextColor(Color.parseColor("#fa8854"));
				naviIndicatorArray[index].setVisibility(View.VISIBLE);
				currentViewId = id;

				if (viewId == this.noteNaviId) {
					this.noteAdapter.showNoContentICON();
				}

				if (viewId == this.bookmarkNaviId) {
					this.bookmarkAdapter.showNoContentICON();
				}
				if (viewId == this.catalogNaviId) { // 进入到目录页的时候,没有笔记或书签的“提示”要隐藏掉.
					noContentView.setVisibility(View.GONE);
				}

				continue;
			}

			view.clearAnimation();// 如果不这样做会导致GONE失效.
			view.setVisibility(View.GONE);
			naviTextArray[index].setTextColor(Color.parseColor("#757575"));
			naviIndicatorArray[index].setVisibility(View.GONE);
		} // end for index
	}

	public void setUp(int fragmentId, final DrawerLayout drawerLayout) {

		mFragmentContainerView = getActivity().findViewById(fragmentId);

		mDrawerLayout = drawerLayout;

		int drawerShadowId = androidAppService
				.getDrawableResource("drawer_shadow");

		mDrawerLayout.setDrawerShadow(drawerShadowId, GravityCompat.START);
		mDrawerLayout.openDrawer(mFragmentContainerView);

		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		// drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

		// drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
		// @Override
		// public void onDrawerSlide(View drawerView, float slideOffset) {
		// }
		//
		// @Override
		// public void onDrawerOpened(View drawerView) {
		// drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		// }
		//
		// @Override
		// public void onDrawerClosed(View drawerView) {
		// drawerLayout
		// .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		// }
		//
		// @Override
		// public void onDrawerStateChanged(int newState) {
		// }
		// });
	}

	public void updateCatalog(BookCatalog catalogRoot) {

		if (this.catalogAdapter != null) {

			this.catalogRoot = catalogRoot;

			this.catalogAdapter.init(catalogRoot);
		}
	}

	public void open() {

		mDrawerLayout.openDrawer(mFragmentContainerView);
	}

	public void close() {

		mDrawerLayout.closeDrawer(mFragmentContainerView);
	}

	public boolean isDrawerOpen() {

		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	public void setNavigationDrawerCallback(NavigationDrawerCallbacks navigationDrawerCallback) {
		this.navigationDrawerCallback = navigationDrawerCallback;
	}

	public interface NavigationDrawerCallbacks {

		void onNavigationDrawerItemSelected(int position);
	}

	private final class BookCatalogAdapter extends TreeAdapter {

		private volatile List<BookCatalog> willShowTrees = new ArrayList<BookCatalog>();
		private volatile HashSet<BookCatalog> openItems = new HashSet<BookCatalog>(); // 用来存储已经打开的目录。

		private ListView listView;
		private int currentPosition;
		private BookCatalog selectedItem;

		BookCatalogAdapter(Activity activity, ListView listView) {

			super(activity);
			this.listView = listView;

			this.listView.setFadingEdgeLength(100);
			this.listView.setVerticalFadingEdgeEnabled(true);
		}

		public void init(BookCatalog catalogRoot) {

			willShowTrees.clear();
			openItems.clear();

			// 一级目录显示到页面上。
			List<BookCatalog> subtrees = catalogRoot.subCatalogList();

			if (subtrees != null) {																	// subtrees有为空的可能.

				willShowTrees.addAll(subtrees);
			}

			super.init(willShowTrees, openItems, catalogRoot);

			listView.setAdapter(this);
			listView.setOnItemClickListener(this);

			listView.addOnLayoutChangeListener(new OnLayoutChangeListener() {

				@Override
				public void onLayoutChange(View v,
										   int left,
										   int top,
										   int right,
										   int bottom,
										   int oldLeft,
										   int oldTop,
										   int oldRight,
										   int oldBottom) {

					listView.postDelayed(new Runnable() {
						@Override
						public void run() {

							if (currentPosition == -1) {
								return;
							}

							listView.smoothScrollToPosition(currentPosition);
							currentPosition = -1;
						}
					}, 300);
				}
			});

			this.notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			int general_catalog_tree_itemLayout = androidAppService
					.getLayoutResource("hy_general_catalog_tree_item");

			final View view = (convertView != null) ? convertView
					: LayoutInflater.from(parent.getContext()).inflate(
							general_catalog_tree_itemLayout, parent, false);

			final BookCatalog tree = (BookCatalog) getItem(position);

			int toc_tree_item_iconId = androidAppService
					.getIdResource("toc_tree_item_icon");

			setIcon(ViewUtil.findImageView(view, toc_tree_item_iconId), tree);

			int toc_tree_item_textId = androidAppService
					.getIdResource("toc_tree_item_text");
			TextView textView = ViewUtil.findTextView(view,
					toc_tree_item_textId);

			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < tree.getLevel(); i++) {
				sb.append("    ");
			}

			if (tree == selectedItem) {

				int catalogItermSelectedTextColorId = androidAppService
						.getColorResource("catalogItermSelectedTextColor");

				textView.setTextColor(this.activity.getResources().getColor(
						catalogItermSelectedTextColorId));
				this.listView.setSelection(position);
			} else {

				int catalogItermTextColorId = androidAppService
						.getColorResource("catalogItermTextColor");

				textView.setTextColor(this.activity.getResources().getColor(
						catalogItermTextColorId));
			}

			String title = tree.getTitle();
			if (title != null) {
				title = title.trim();
			}
			textView.setText(sb.toString() + title);
			// textView.setText(title);

			return view;
		}

		@Override
		public void click(int position, View convertView) {

			final BookCatalog bookCatalog = (BookCatalog) getItem(position);

			selectedItem = bookCatalog;

			readerDynamicSDK.entryBookCatalogPage(bookCatalog);

			close();
		}

		public void setCatalogForCurrent() {

//			int pageIndex = PageIndexService.getCurrentPageIndex();
//			ReaderService readerService = ReaderService.Instance();
//			BookParserFinishedData.Catalog catalog = readerService.getCatalog(pageIndex);
//
//			if (catalog == null) {
//				return;
//			}
//
//			this.selectItem(catalog);
//
//			currentPosition = this.calcTreePosition(catalog); // 计算当前目录所在的位置,用于将目录定位到当前位置。
//			selectedItem = catalog;
//
//			this.notifyDataSetChanged();
		}
	}

	private final class NoteAdapter extends BaseAdapter implements
			SwipyRefreshLayout.OnRefreshListener {

		private Activity activity;
		private SwipyRefreshLayout swipyRefreshLayout;

		private int countPerPage = 10; // 去数据库拿数据，一次拿几条。
		private int totalPageCount = 0; // 总页数。

		private List<BookStress> stressNoteList = new ArrayList<BookStress>(); // 数据库中拿到的数据存到这里.
		private BookStressQuery bookStressQuery; // 查询数据的条件参数。

		NoteAdapter(Activity activity, SwipyRefreshLayout swipyRefreshLayout) {

			this.activity = activity;
			this.swipyRefreshLayout = swipyRefreshLayout;
			this.init();
		}

		private void init() {

			// 刷新的样式
			this.swipyRefreshLayout.setColorScheme(
					android.R.color.holo_red_light,
					android.R.color.holo_green_light,
					android.R.color.holo_blue_bright,
					android.R.color.holo_orange_light);

			this.swipyRefreshLayout.setOnRefreshListener(this);

			int noteListId = androidAppService.getIdResource("noteListHY");
			ListView listView = (ListView) this.swipyRefreshLayout
					.findViewById(noteListId);
			listView.setAdapter(this);

			listView.setFadingEdgeLength(100);
			listView.setVerticalFadingEdgeEnabled(true);

			listView.setOverScrollMode(View.OVER_SCROLL_NEVER); // 去掉滑动列表到两头时的动画效果。

			this.swipyRefreshLayout.setColorSchemeResources(
					android.R.color.holo_blue_bright,
					android.R.color.holo_green_light,
					android.R.color.holo_orange_light,
					android.R.color.holo_red_light);

			// 下边是加载数据。
			String userId = book.userId;
			String bookId = book.bookId;

			this.bookStressQuery = new BookStressQuery(userId,
														bookId,
														BookStress.StressType.StressNote,
														countPerPage,
														0);

			reLoadData(); // 启动后先加载一页数据。
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			int note_itemLayout = androidAppService.getLayoutResource("hy_note_item");

			final View view = (convertView != null) ? convertView
					: LayoutInflater.from(parent.getContext()).inflate(
							note_itemLayout, parent, false);

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					final BookStress bookStress = (BookStress) getItem(position);

					readerDynamicSDK.entryBookStressPage(bookStress);

					close();
				}
			});

			final BookStress bookStress = (BookStress) getItem(position);

			int noteModifyId = androidAppService.getIdResource("noteModifyHY");
			View noteModifyView = view.findViewById(noteModifyId);

			noteModifyView.setOnClickListener(new View.OnClickListener() { // 修改笔记

						@Override
						public void onClick(View v) {

							// 进入到当前笔记的页面，并弹出修改页面。

							readerDynamicSDK.entryBookStressPage(bookStress);

							close();

							readerPopupService.showCommentPanel(bookStress);
						}
					});

			int noteDeleteId = androidAppService.getIdResource("noteDeleteHY");

			View noteDeleteView = view.findViewById(noteDeleteId);
			noteDeleteView.setOnClickListener(new View.OnClickListener() { // 删除笔记

						@Override
						public void onClick(View v) {

							stressNoteList.remove(position);

							readerDynamicSDK.deleteCloudStress(bookStress);

							notifyDataSetChanged();
						}
					});

			int note_textId = androidAppService.getIdResource("note_text");

			TextView textView = ViewUtil.findTextView(view, note_textId);
			textView.setText(bookStress.getContent());

			int noteUserTextId = androidAppService.getIdResource("note_text_user");

			textView = ViewUtil.findTextView(view, noteUserTextId);
			textView.setText(bookStress.getCommentContent());

			int note_timeId = androidAppService.getIdResource("note_time");
			textView = ViewUtil.findTextView(view, note_timeId);
			String time = DateFormatService.formatDate(bookStress.getCreateTime(), DateUtils.formattime);

			textView.setText(time);

			return view;
		}

		public void showNoContentICON() {

			if (stressNoteList.size() == 0) {

				noContentView.setVisibility(View.VISIBLE);
				noContentTextView.setText("暂时没有笔记");
				noContentTextSubView.setText("阅读时长按选中文字弹出菜单中加笔记");
			} else {
				noContentView.setVisibility(View.GONE);
			}
		}

		@Override
		public int getCount() {
			return stressNoteList.size();
		}

		@Override
		public Object getItem(int position) {

			return stressNoteList.get(position);
		}

		@Override
		public long getItemId(int position) {

			BookStress stress = stressNoteList.get(position);

			return stress.getId();
		}

		@Override
		public void onRefresh(SwipyRefreshLayoutDirection direction) {

			int size = this.stressNoteList.size();

			if (size == 0) {
				swipyRefreshLayout.setRefreshing(false);
				return;
			}

			if (direction == SwipyRefreshLayoutDirection.TOP) { // 如果是往下拉

				bookStressQuery.previousPage();

				if (bookStressQuery.getPageIndex() < 0) { // 到数据顶端了

					// 发消息提示用户已经到了最上边了。
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {

							swipyRefreshLayout.setRefreshing(false);
//
// 							ToastService t = new ToastService(activity);
//							t.showMsg("已经到头了");
						}
					});

					bookStressQuery.nextPage(); // 回到上一次的状态
					return;
				}

				// 如果超过三页就把超出的个数，从list的尾部开始删除。为了不站用太多内存
				int c = size - this.countPerPage * 3;
				if (c > 0) {

					int len = size - c;
					for (int i = size - 1; i > len; i--) {
						this.stressNoteList.remove(i);
					}
				}
				loadData();
				return;
			}

			bookStressQuery.nextPage();

			if (bookStressQuery.getPageIndex() == totalPageCount) {

				// 到最后一条数据了。
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						swipyRefreshLayout.setRefreshing(false);
//						ToastService t = new ToastService(activity);
//						t.showMsg("已经到最后一页");
					}
				});

				bookStressQuery.previousPage();
				return;
			}

			// 如果超过三页就把超出的个数，从list的头部开始删除。为了不站用太多内存.
			int c = size - this.countPerPage * 3;
			if (c > 0) {

				for (int i = 0; i < c; i++) {
					this.stressNoteList.remove(i);
				}
			}

			loadData();
		}

		public void reLoadData() {

			this.stressNoteList.clear();
			this.loadData();
		}

		/**
		 * 加载一页数据。
		 */
		private void loadData() {

			new Thread(new Runnable() {

				@Override
				public void run() {

					List<BookStress> list = readerDynamicSDK.loadBookStress(bookStressQuery);

					if (list == null) {
						return;
					}

					stressNoteList.addAll(list);

					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {

							if (stressNoteList.size() == 0) {

								if (currentViewId == noteNaviId) {
									noContentView.setVisibility(View.VISIBLE);
									// swipyRefreshLayout.setVisibility(View.GONE);
								}
							} else {

								notifyDataSetChanged();
								noContentView.setVisibility(View.GONE);
								swipyRefreshLayout.setRefreshing(false);
								// swipyRefreshLayout.setVisibility(View.VISIBLE);
							}
						}
					});
				}
			}).start();
		}
	}

	private final class BookmarkAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

		private Activity activity;
		private ListView listView;

		// private int isVisible = View.GONE;

		List<BookStress> stressList = new ArrayList<BookStress>();

		BookmarkAdapter(Activity activity, ListView listView) {

			this.activity = activity;

			this.listView = listView;

			if (listView != null) {

				this.listView.setFadingEdgeLength(100);
				this.listView.setVerticalFadingEdgeEnabled(true);

				this.listView.setAdapter(this);
			}

			stressList.clear();
			loadData();
		}

		public void reLoadData() {
			stressList.clear();
			loadData();
		}

		private void loadData() {
			new Thread(new Runnable() { // 加载数据。

						@Override
						public void run() {

							String userId = book.userId;
							String bookId = book.bookId;

							BookStressQuery bookStressQuery = new BookStressQuery(userId,
																				  bookId,
																				  BookStress.StressType.BookMark,
																					20,
																				 0);

							List<BookStress> list = readerDynamicSDK.loadBookStress(bookStressQuery);

							stressList.addAll(list);

							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									if (stressList.size() == 0) {

										if (currentViewId == bookmarkNaviId) {
											noContentView
													.setVisibility(View.VISIBLE);
										}
									} else {

										notifyDataSetChanged();
										noContentView.setVisibility(View.GONE);
									}

								}
							});
						}
					}).start();
		}

		public void showNoContentICON() {

			if (stressList.size() == 0) {

				noContentView.setVisibility(View.VISIBLE);

				noContentTextView.setText("暂时没有书签");
				noContentTextSubView.setText("选菜单中的添加书签即可加书签");
			} else {

				noContentView.setVisibility(View.GONE);
			}
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			int general_bookmark_itemId = androidAppService.getLayoutResource("demo_bookmark_item");


			final View view = (convertView != null) ? convertView
					: LayoutInflater.from(parent.getContext()).inflate(
							general_bookmark_itemId, parent, false);

			int bookmarkDeleteId = androidAppService.getIdResource("bookmarkDelete");
			final View delteteView = view.findViewById(bookmarkDeleteId);

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					final BookStress bookStress = (BookStress) getItem(position);

					readerDynamicSDK.entryBookStressPage(bookStress);

					close();
				}
			});

			final BookStress bookStress = (BookStress) getItem(position);

			delteteView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) { // 删除

					stressList.remove(bookStress);

					readerDynamicSDK.deleteCloudStress(bookStress);

					notifyDataSetChanged();
				}
			});

			int bookmark_textId = androidAppService.getIdResource("bookmark_text");

			TextView textView = ViewUtil.findTextView(view, bookmark_textId);
			textView.setText(bookStress.getContent());

			int bookmark_timeId = androidAppService.getIdResource("bookmark_time");
			textView = ViewUtil.findTextView(view, bookmark_timeId);

			String time = DateFormatService.formatDate(bookStress.getCreateTime(), DateUtils.formattime);

			textView.setText(time);

			return view;
		}

		@Override
		public int getCount() {
			return stressList.size();
		}

		@Override
		public Object getItem(int position) {

			return stressList.get(position);
		}

		@Override
		public long getItemId(int position) {

			BookStress stress = stressList.get(position);

			return stress.getId();
		}

		@Override
		public void onItemClick(AdapterView<?> parent,
								View view,
								int position,
								long id) {

		}

	}
}
