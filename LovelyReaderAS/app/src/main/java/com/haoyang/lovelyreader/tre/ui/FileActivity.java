package com.haoyang.lovelyreader.tre.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
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
import com.java.common.service.file.FileNameService;
import com.mjiayou.trecorelib.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by xin on 18/9/24.
 */

public class FileActivity extends BaseActivity {

    public static final String EXTRA_FILE_LIST = "file_list";
    public static final String EXTRA_FILE_BEAN = "file_bean";
    public static final String EXTRA_FILE_PATH = "file_path";
    public static final String EXTRA_FILE_NAME = "file_name";
    public static final String EXTRA_FILE_SUFFIX = "file_suffix";

    private final String FILE_SUFFIX_EPUB = "epub";

    private RelativeLayout rlTitle;
    private TextView tvTitle;
    private ImageView ivBack;

    private ListView lvFile;
    private TextView tvCount;
    private TextView tvSubmit;

    private FileAdapter mFileAdapter;
    private List<FileBean> mList;
    private Stack<File> mFileStack = new Stack<>();
    private ArrayList<FileBean> mSelectedFileBeanList = new ArrayList<>();

    private FileNameService mFileNameService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        // findViewById
        rlTitle = (RelativeLayout) findViewById(R.id.rlTitle);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        lvFile = (ListView) findViewById(R.id.lvFile);
        tvCount = (TextView) findViewById(R.id.tvCount);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);

        // mFileNameService
        mFileNameService = new FileNameService();

        initView();
    }

    @Override
    public void onBackPressed() {
        if (mFileStack.size() == 1) { // 说明只剩下根目录，再返回则直接退出页面
            super.onBackPressed();
        } else {
            mFileStack.pop();
            File file = mFileStack.peek();
            mList = getFileList(file);
            mFileAdapter.setList(mList);
            mSelectedFileBeanList.clear();
            tvCount.setText("已选择了" + mSelectedFileBeanList.size() + "个文件");
        }
    }

    @Override
    protected void initView() {
        super.initView();

        // tvTitle
        tvTitle.setText("选择电子书");
        // ivBack
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // lvFile
        File rootFile = Environment.getExternalStorageDirectory();
        mFileStack.push(rootFile); // 把根目录压入堆栈
        mList = getFileList(rootFile);
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
                    File file = new File(fileBean.getPath());
                    // 如果文件是目录，则递归显示目录内文件；如果文件不是目录，则直接返回该文件
                    if (file.isDirectory()) {
                        mFileStack.add(file);
                        mList = getFileList(file);
                        mFileAdapter.setList(mList);
                        mSelectedFileBeanList.clear();
                        tvCount.setText("已选择了" + mSelectedFileBeanList.size() + "个文件");
                    } else {
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
    private List<FileBean> getFileList(File folder) {
        if (folder == null) {
            return null;
        }

        // 获取该目录下的所有文件，根据是目录还是文件进行分类
        File[] files = folder.listFiles();
        List<String> folderList = new ArrayList<>();
        List<String> fileList = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) { // 文件夹
                folderList.add(file.getAbsolutePath());
            } else { // 文件
                fileList.add(file.getAbsolutePath());
            }
        }

        // 根据文件名称排序
        Collections.sort(folderList);
        Collections.sort(fileList);

        // 汇总
        List<FileBean> fileBeanList = new ArrayList<>();
        for (String filePath : folderList) {
            String fileName = mFileNameService.getFileName(filePath);
            String fileSuffix = mFileNameService.getFileExtendName(filePath);
            if (TextUtils.isEmpty(fileName)) {
                continue;
            }

            FileBean fileBean = new FileBean();
            fileBean.setName(fileName);
            fileBean.setSuffix(fileSuffix);
            fileBean.setPath(filePath);
            fileBean.setFolder(true);
            fileBeanList.add(fileBean);
        }
        for (String filePath : fileList) {
            String fileName = mFileNameService.getFileName(filePath);
            String fileSuffix = mFileNameService.getFileExtendName(filePath);
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
            fileBeanList.add(fileBean);
        }
        return fileBeanList;
    }
}
