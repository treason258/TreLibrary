package com.haoyang.lovelyreader.tre.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseActivity;
import com.haoyang.lovelyreader.tre.bean.FileBean;
import com.haoyang.lovelyreader.tre.bean.store.LocalFileStore;
import com.haoyang.lovelyreader.tre.helper.Configs;
import com.haoyang.lovelyreader.tre.helper.DBHelper;
import com.haoyang.lovelyreader.tre.helper.Global;
import com.haoyang.lovelyreader.tre.ui.adapter.FileAdapter;
import com.java.common.service.file.FileNameService;
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by treason on 2018/11/24.
 */

public class FileFilterActivity extends BaseActivity {

    public static final String EXTRA_FILE_LIST = "file_list";

    private final String FILE_SUFFIX_EPUB = "epub";

    private RelativeLayout rlTitle;
    private TextView tvTitle;
    private ImageView ivBack;
    private TextView tvMenu;

    private ListView lvFile;
    private TextView tvCount;
    private TextView tvSubmit;

    private FileAdapter mFileAdapter;
    private List<FileBean> mList;
    private ArrayList<FileBean> mSelectedFileBeanList = new ArrayList<>();

    private FileNameService mFileNameService;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_file;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {
        // findViewById
        rlTitle = (RelativeLayout) findViewById(R.id.rlTitle);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvMenu = (TextView) findViewById(R.id.tvMenu);
        lvFile = (ListView) findViewById(R.id.lvFile);
        tvCount = (TextView) findViewById(R.id.tvCount);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);

        // mFileNameService
        mFileNameService = new FileNameService();

        initView();

        LocalFileStore localFileStore = DBHelper.getLocalFileStore();
        if (localFileStore != null && localFileStore.getData() != null) {
            List<FileBean> fileBeanList = localFileStore.getData();
            mList.clear();
            mList.addAll(fileBeanList);
            mFileAdapter.setList(mList);
        } else {
            searchLocalFile();
        }
    }

    @Override
    public void initView() {
        super.initView();

        // tvTitle
        tvTitle.setText("选择电子书");
        tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Global.showEpubFilePath = !Global.showEpubFilePath;
                mFileAdapter.setList(mList);
                return false;
            }
        });
        // ivBack
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // tvMenu
        tvMenu.setVisibility(View.VISIBLE);
        tvMenu.setText("刷新");
        tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.clear();
                mFileAdapter.setList(mList);
                searchLocalFile();
            }
        });

        // lvFile
        mList = new ArrayList<>();
        mFileAdapter = new FileAdapter(mContext, mList);
        lvFile.setAdapter(mFileAdapter);
        lvFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mFileAdapter != null
                        && mFileAdapter.getList() != null
                        && mFileAdapter.getList().size() > position
                        && mFileAdapter.getList().get(position) != null) {
                    FileBean fileBean = mFileAdapter.getList().get(position);

                    if (fileBean.isSelected()) {
                        fileBean.setSelected(false);
                        if (mSelectedFileBeanList.contains(fileBean)) {
                            mSelectedFileBeanList.remove(fileBean);
                        }
                    } else {
                        fileBean.setSelected(true);
                        if (!mSelectedFileBeanList.contains(fileBean)) {
                            mSelectedFileBeanList.add(fileBean);
                        }
                    }
                    tvCount.setText("已选择了" + mSelectedFileBeanList.size() + "个文件");
                    mFileAdapter.notifyDataSetChanged();
                }
            }
        });

        // tvSubmit
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedFileBeanList.size() == 0) {
                    ToastUtils.show("请选择电子书");
                    return;
                }
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(EXTRA_FILE_LIST, mSelectedFileBeanList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 获取文件列表
     */
    private List<FileBean> getEpubFileList(File folder) {
        List<FileBean> epubFileList = new ArrayList<>();
        File[] files = folder.listFiles();
        for (File file : files) {
            LogUtils.i(TAG, "file -> " + file.getAbsolutePath());

            if (file.isDirectory()) {
                epubFileList.addAll(0, getEpubFileList(file));
            } else {
                String filePath = file.getAbsolutePath();
                String fileName = mFileNameService.getFileName(filePath);
                String fileSuffix = mFileNameService.getFileExtendName(filePath);
                if (filePath.contains(Configs.DIR_SDCARD_PROJECT)) {
                    continue;
                }
                if (TextUtils.isEmpty(fileName)) {
                    continue;
                }
                if (TextUtils.isEmpty(fileSuffix)) {
                    continue;
                }
                if (!fileSuffix.equals(FILE_SUFFIX_EPUB)) {
                    continue;
                }

                FileBean fileBean = new FileBean();
                fileBean.setName(fileName);
                fileBean.setSuffix(fileSuffix);
                fileBean.setPath(filePath);
                fileBean.setFolder(false);
                epubFileList.add(0, fileBean);
                LogUtils.i(TAG, "file add");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mList.add(0, fileBean);
                        mFileAdapter.setList(mList);
                    }
                });
            }
        }
        return epubFileList;
    }

    private void searchLocalFile() {
        // 正在搜索电子书
        showLoading(true);
        ToastUtils.show("电子书搜索需要几分钟完成，请耐心等待");
        new Thread(new Runnable() {
            @Override
            public void run() {
                File rootFile = Environment.getExternalStorageDirectory();
                mList = getEpubFileList(rootFile);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showLoading(false);
                        ToastUtils.show("搜索完成");
                        mFileAdapter.setList(mList);
                        LocalFileStore localFileStore = new LocalFileStore();
                        localFileStore.setData(mList);
                        DBHelper.setLocalFileStore(localFileStore);
                    }
                });

            }
        }).start();
    }
}
