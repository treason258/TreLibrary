package com.haoyang.lovelyreader.tre;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.bean.BookBean;
import com.mjiayou.trecorelib.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xin on 18/9/22.
 */
public class HomeFragment extends BaseFragment {

    private ImageView ivSearch;
    private EditText etSearch;
    private ImageView ivDelete;
    private GridView gvBook;

    private HomeAdapter mHomeAdapter;
    private List<BookBean> mList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        ivSearch = (ImageView) view.findViewById(R.id.ivSearch);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        gvBook = (GridView) view.findViewById(R.id.gvBook);
        initView();
        return view;
    }

    @Override
    protected void initView() {

        mList = new ArrayList<>();
        mList.add(new BookBean("三国演义"));
        mList.add(new BookBean("水浒传"));
        mList.add(new BookBean("西游记"));
        mList.add(new BookBean("红楼梦"));

        mHomeAdapter = new HomeAdapter(mContext, mList);
        gvBook.setAdapter(mHomeAdapter);
        gvBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.show(mList.get(position).getName());
            }
        });
    }
}
