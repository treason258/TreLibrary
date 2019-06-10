package com.haoyang.reader.ui;

import java.util.List;
import java.util.ArrayList;

import java.util.HashSet;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.app.base.service.android.AndroidAppService;
import com.haoyang.reader.sdk.BookCatalog;

public abstract class TreeAdapter extends BaseAdapter implements
		AdapterView.OnItemClickListener {

	private BookCatalog treeRoot; // 树根
	private List<BookCatalog> willShowTrees;

	// 用来记录已经打开子树的tree。
	private HashSet<BookCatalog> myOpenItems;
	private AndroidAppService androidAppService;

	protected Activity activity;

	protected TreeAdapter(Activity activity) {

		this.activity = activity;
		androidAppService = new AndroidAppService(this.activity);
	}

	protected void init(List<BookCatalog> showTrees,
						HashSet<BookCatalog> openItems,
						BookCatalog treeRoot) {

		this.willShowTrees = showTrees;
		this.myOpenItems = openItems;

		this.treeRoot = treeRoot;

		if (!this.treeRoot.hasChildren()) {

			return;
		}
	}

	/**
	 * 展开或关闭目录项。
	 * 
	 * @param tree
	 * @param position
	 */
	public final void expandOrCollapseTree(BookCatalog tree, int position) {

		if (!tree.hasChildren()) {
			return;
		}
		List<BookCatalog> subtrees = tree.subCatalogList();
		if (isOpen(tree)) { // 收缩:
							// 点击时如果处理打开的状态的话，那么把除当前点击的tree,tree下的所有都从willShowTrees中删除掉。
			this.removeOpenStatusTree(tree); // tree下的所有都从willShowTrees中删除掉,
												// 除了tree。
			this.notifyDataSetChanged();
			return;
		}

		// 展开
		myOpenItems.add(tree);

		int i = 1;
		for (BookCatalog t : subtrees) {

			willShowTrees.add(position + i++, t);
		} // end for
		notifyDataSetChanged();
	}

	/**
	 * 删除某个节点下的所有已显示的节点。
	 * 
	 * @param tree
	 */
	private void removeOpenStatusTree(BookCatalog tree) {

		List<BookCatalog> subtrees = tree.subCatalogList();
		for (BookCatalog subTree : subtrees) {

			if (this.isOpen(subTree)) {

				myOpenItems.remove(subTree);
				if (subTree.hasChildren()) { // open情况下
					this.removeOpenStatusTree(subTree);
				}
			}

			willShowTrees.remove(subTree);
		} // end for

		myOpenItems.remove(tree);
	}

	/**
	 * 当前目录是否是打开状态。
	 * 
	 * @param tree
	 * @return
	 */
	public final boolean isOpen(BookCatalog tree) {

		return myOpenItems.contains(tree);
	}

	public int calcTreePosition(BookCatalog tree) {

		int i = 0;
		for (BookCatalog t : willShowTrees) {

			if (t == tree) {
				return i;
			}

			i++;
		}

		return i;
	}

	/**
	 * 计算tree在ListView中的位置，并设置这个位置为选中状态。
	 *
	 * @param bookCatalog
	 */
	public final void selectItem(BookCatalog bookCatalog) {

		if (bookCatalog == null) {
			return;
		}
		
		if (myOpenItems.contains(bookCatalog)) {
			return;
		}
		
		if (bookCatalog.getParent() == null) {
			return;
		}
		
		if (myOpenItems.contains(bookCatalog.getParent())) {
			return;
		}

		if (this.willShowTrees.contains(bookCatalog)) {
			return;
		}

		BookCatalog parent = bookCatalog.getParent(); // 说明是一级目录，一级目录已经显示在目录中了，所以就不处理了。
		if (parent == null) {
			return;
		}

		BookCatalog finalParent = null;
		List<BookCatalog> willInsertTree = new ArrayList<BookCatalog>(); // 将要放入显示list中的数据。

		willInsertTree.add(bookCatalog);

		Log.d("aaa", " title = " + bookCatalog.getTitle());

		for (int i = 1; i > 0; i++) {

			bookCatalog = bookCatalog.getParent();
			if (bookCatalog == null) {
				break;
			}

			if (willShowTrees.contains(bookCatalog)
					|| myOpenItems.contains(bookCatalog)
					|| bookCatalog.getLevel() == 1) {

				Log.d("aaa", "finalParent title = " + bookCatalog.getTitle());
				finalParent = bookCatalog;
				break;
			}

			willInsertTree.add(bookCatalog);
		}

		// 查找 finalParent 所在的位置
		int index = 0;
		for (BookCatalog willTree : this.willShowTrees) {

			if (willTree == finalParent) {
				Log.d("aaa", "willTree == finalParent");
				break;
			}
			index++;
		}

		if (index >= this.willShowTrees.size()) {

			Log.d("aaa", "finalParent = " + finalParent);
			Log.d("aaa",
					"this.willShowTrees.size = " + this.willShowTrees.size());
			Log.d("aaa", "index value = " + index);
		}

		if (index == this.willShowTrees.size()) {

			// index --;
		}

		int startIndex = willInsertTree.size() - 1;
		for (; startIndex >= 0; startIndex--) {

			BookCatalog insertTree = willInsertTree.get(startIndex);

			index = this.addCatalogList(index, insertTree, this.willShowTrees);
		}
		// }

		this.notifyDataSetChanged();
	}

	private int addCatalogList(int index, BookCatalog bookCatalog, List<BookCatalog> list) {

		BookCatalog parent = bookCatalog.getParent();
		myOpenItems.add(parent);

		List<BookCatalog> subTreeList = parent.subCatalogList();
		int nextIndex = 0;
		int i = 1;
		for (BookCatalog subTree : subTreeList) {

			int location = index + i++;
			list.add(location, subTree);

			if (subTree == bookCatalog) {
				nextIndex = location;
			}
		}

		return nextIndex;
	}

	protected final void openTree(BookCatalog bookCatalog) {

		if (bookCatalog == null) {
			return;
		}

		while (!myOpenItems.contains(bookCatalog)) {

			if (bookCatalog == null) {
				break;
			}

			myOpenItems.add(bookCatalog);
			bookCatalog = bookCatalog.getParent();
		} // end while
	}

	/**
	 * 统计当前这个tree下有多少个子节点。
	 * 
	 * @param bookCatalog
	 * @return
	 */
	private int getCount(BookCatalog bookCatalog) {

		int count = 1;
		if (isOpen(bookCatalog)) {

			for (BookCatalog subtree : bookCatalog.subCatalogList()) {

				count += getCount(subtree);
			}
		}

		return count;
	}

	// 点击目录中的某一项时，
	public final void onItemClick(AdapterView<?> parent,
								  View view,
								  int position,
								  long id) {

		BookCatalog bookCatalog = this.willShowTrees.get(position);

		runTreeItem(bookCatalog, position, view);
	}

	protected boolean runTreeItem(BookCatalog bookCatalog, int position, View view) {

		if (bookCatalog.hasChildren()) { // 点击后如果有子，那么就执行展开或收回。

			expandOrCollapseTree(bookCatalog, position);
			return true;
		}

		// 执行点击操作。
		this.click(position, view);
		return false;
	}

	@Override
	public int getCount() {

		return this.willShowTrees.size();
	}

	public Object getItem(int position) {

		return this.willShowTrees.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	// ------------------------------------------------------

	public abstract void click(int position, View convertView);

	public abstract View getView(int position, View convertView,
			ViewGroup parent);

	protected final void setIcon(ImageView imageView, BookCatalog bookCatalog) {

		if (bookCatalog.hasChildren()) {

			if (isOpen(bookCatalog)) {

				int catalogOpenId = androidAppService
						.getDrawableResource("catalog_open");

				imageView.setImageResource(catalogOpenId);
			} else {
				int catalogCloseId = androidAppService
						.getDrawableResource("catalog_close");
				imageView.setImageResource(catalogCloseId);
			}
		} else {
			imageView.setImageDrawable(null);
		}

		// imageView.setPadding(25 * (tree.level - 1),
		// imageView.getPaddingTop(),
		// 0, imageView.getPaddingBottom());
	}
}
