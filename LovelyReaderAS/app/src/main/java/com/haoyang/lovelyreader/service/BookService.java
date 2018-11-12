///**
// *
// */
//package com.haoyang.lovelyreader.service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.app.Activity;
//
//import com.app.base.service.android.DefaultDownLoadDBService;
//import com.app.base.service.business.BusinessJsonResultListener;
//import com.haoyang.lovelyreader.db.BookDBService;
//import com.haoyang.lovelyreader.entity.UserBook;
//import com.haoyang.lovelyreader.entity.User;
//import com.java.common.download.DownLoadInfo;
//import com.java.common.download.DownloadListener;
//import com.java.common.download.FileDownLoader;
//import com.java.common.service.CommonKeys;
//
///**
// * 电子书管理。
// *
// * @author tianyu
// *
// */
//public class BookService extends AbstractService {
//
//	private Activity activity;
//
//	public BookService(Activity activity) {
//		this.activity = activity;
//	}
//
//	public List<UserBook> loadBooks(User user) {
//
//		BookDBService bookDBService = new BookDBService(this.activity);
//
//		List<UserBook> books = bookDBService.loadBookByTimes(user);
//
//		return books;
//	}
//
//	public void saveBook(UserBook book) {
//
//		BookDBService bookDBService = new BookDBService(this.activity);
//		bookDBService.saveBook(book);
//	}
//
//	public UserBook loadBookByPath(String path) {
//
//		BookDBService bookDBService = new BookDBService(this.activity);
//		return bookDBService.loadBookByPath(path);
//	}
//
//	public void upLoad(User user, UserBook book,
//			BusinessJsonResultListener businessJsonResultListener) {
//
//		Map<String, String> para = new HashMap<String, String>();
//
//		para.put(CommonKeys.USERNAME, user.token);
//
//		this.submit(para, DataInterfaceService.UPDATEBOOK,
//				businessJsonResultListener);
//
//	}
//
//	public void download(User user, UserBook book,
//			BusinessJsonResultListener businessJsonResultListener) {
//
//		DownloadListener downloadListener = null;
//
//		DefaultDownLoadDBService downLoadDBService = DefaultDownLoadDBService
//				.getInstance(this.activity);
//		DownLoadInfo downLoadInfo = new DownLoadInfo(book.bookPath, null, null,
//				1024 * 1024 * 3);
//
//		FileDownLoader fileDownLoader = new FileDownLoader(downLoadInfo,
//				downLoadDBService, downloadListener);
//
//		fileDownLoader.init();
//		fileDownLoader.download();
//	}
//
//	/**
//	 */
//	public void delete(User user, UserBook book,
//			BusinessJsonResultListener businessJsonResultListener) {
//
//		Map<String, String> para = new HashMap<String, String>();
//
//		para.put(CommonKeys.USERNAME, user.token);
//		para.put(CommonKeys.PASSWORD, book.bookId);
//
//		this.submit(para, DataInterfaceService.DELETEBOOK,
//				businessJsonResultListener);
//	}
//}
