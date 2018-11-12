//package com.haoyang.lovelyreader.ui;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Stack;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Environment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.haoyang.lovelyreader.R;
//import com.java.common.service.file.FileNameService;
//
//public class FileBrowseActivity extends Activity {
//
//	ListView listView;
//	Stack<File> pathStack = new Stack<File>();
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		// 去掉TitleBar
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//		setContentView(R.layout.file_browse);
//
//		listView = (ListView) this.findViewById(R.id.fileList);
//		File file = Environment.getExternalStorageDirectory();
//
//		pathStack.add(file);
//
//		List<String> listItem = makeListItem(file);
//
//		final EpubAdapter epubAdapter = new EpubAdapter(listItem);
//
//		listView.setAdapter(epubAdapter);
//		listView.setOnItemClickListener(epubAdapter);
//
//		TextView goBack = (TextView) this.findViewById(R.id.goBack);
//		goBack.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				if (pathStack.size() == 1) {
//
//					return;
//				}
//
//				pathStack.pop();
//
//				File file = pathStack.peek();
//
//				List<String> list = makeListItem(file);
//
//				epubAdapter.fileNamePathList = list;
//
//				epubAdapter.notifyDataSetChanged();
//			}
//		});
//	}
//
//	List<String> makeListItem(File file) {
//
//		FileNameService fileNameService = new FileNameService();
//
//		List<String> directoryList = new ArrayList<String>();
//		List<String> fileList = new ArrayList<String>();
//
//		File[] files = file.listFiles();
//		for (File f : files) {
//
//			String fileName = fileNameService.getFileName(f.getAbsolutePath());
//
//			if (fileName != null && "".equals(fileName.trim())) {
//				continue;
//			}
//
//			if (f.isDirectory()) {
//				directoryList.add(f.getAbsolutePath());
//				continue;
//			}
//
//			String fileExtendName = fileNameService.getFileExtendName(f
//					.getAbsolutePath());
//			if (!"epub".equals(fileExtendName)) {
//				continue;
//			}
//
//			fileList.add(f.getAbsolutePath());
//		} // end for
//
//		List<String> list = new ArrayList<String>();
//
//		Collections.sort(directoryList);
//
//		for (String fileNamePath : directoryList) {
//			list.add(fileNamePath);
//		}
//
//		Collections.sort(fileList);
//
//		for (String fileNamePath : fileList) {
//			list.add(fileNamePath);
//		}
//
//		return list;
//	}
//
//	class EpubAdapter extends BaseAdapter implements
//			AdapterView.OnItemClickListener {
//
//		List<String> fileNamePathList;
//		FileNameService fileNameService;
//
//		EpubAdapter(List<String> bookList) {
//
//			this.fileNamePathList = bookList;
//			this.fileNameService = new FileNameService();
//		}
//
//		@Override
//		public View getView(final int position, View convertView,
//				ViewGroup parent) {
//
//			final View view = (convertView != null) ? convertView
//					: LayoutInflater.from(parent.getContext()).inflate(
//							R.layout.file_item, parent, false);
//
//			String fileNamePath = this.fileNamePathList.get(position);
//
//			ImageView pIcon = (ImageView) view.findViewById(R.id.fileTypeIcon);
//			pIcon.setImageBitmap(null);
//
//			TextView epubPath = (TextView) view.findViewById(R.id.fileNamePath);
//
//			String abc = fileNameService.getFileNameFromAddress(fileNamePath);
//
//			epubPath.setText(abc);
//
//			return view;
//		}
//
//		@Override
//		public int getCount() {
//			return this.fileNamePathList.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			String book = this.fileNamePathList.get(position);
//			return book;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return 0;
//		}
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//
//			String fileNamePath = this.fileNamePathList.get(position);
//
//			File file = new File(fileNamePath);
//			if (!file.isDirectory()) {
//
//				Intent intent = new Intent();
//				// 获取用户计算后的结果
//
//				intent.putExtra("path", fileNamePath); // 将计算的值回传回去
//				// 通过intent对象返回结果，必须要调用一个setResult方法，
//				// setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
//				setResult(1, intent);
//
//				finish(); // 结束当前的activity的生命周期
//
//				return;
//			}
//
//			List<String> list = makeListItem(file);
//
//			this.fileNamePathList = list;
//			this.notifyDataSetChanged();
//
//			pathStack.add(file);
//		}
//	}
//
//}
