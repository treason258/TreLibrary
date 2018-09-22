package com.haoyang.lovelyreader.tre;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.haoyang.lovelyreader.R;

/**
 * Created by xin on 18/9/22.
 */
public class MineFragment extends BaseFragment {

  private ImageView ivAvatar;
  private TextView tvNickname;
  private LinearLayout llMember;
  private LinearLayout llFeedback;
  private TextView tvLogout;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_mine, null);
    ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
    tvNickname = (TextView) view.findViewById(R.id.tvNickname);
    llMember = (LinearLayout) view.findViewById(R.id.llMember);
    llFeedback = (LinearLayout) view.findViewById(R.id.llFeedback);
    tvLogout = (TextView) view.findViewById(R.id.tvLogout);
    initView();
    return view;
  }

  @Override protected void initView() {
    // ivAvatar
    ivAvatar.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ToastUtil.show(mContext, "头像");
      }
    });
    // tvNickname
    tvNickname.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ToastUtil.show(mContext, "点击登录");
      }
    });
    // llMember
    llMember.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ToastUtil.show(mContext, "升级成会员");
      }
    });
    // llFeedback
    llFeedback.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ToastUtil.show(mContext, "意见反馈");
      }
    });
    // tvLogout
    tvLogout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ToastUtil.show(mContext, "切换账号");
      }
    });
  }
}
