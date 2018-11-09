package com.haoyang.lovelyreader.tre.ui.frgament;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseFragment;
import com.haoyang.lovelyreader.tre.bean.UserBean;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.CommonParam;
import com.haoyang.lovelyreader.tre.helper.DBHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.http.MyRequestEntity;
import com.haoyang.lovelyreader.tre.ui.FeedbackActivity;
import com.haoyang.lovelyreader.tre.ui.LoginActivity;
import com.mjiayou.trecorelib.dialog.DialogHelper;
import com.mjiayou.trecorelib.dialog.TCAlertDialog;
import com.mjiayou.trecorelib.event.UserLoginStatusEvent;
import com.mjiayou.trecorelib.http.RequestSender;
import com.haoyang.lovelyreader.tre.http.RequestCallback;
import com.mjiayou.trecorelib.util.ToastUtils;
import com.mjiayou.trecorelib.util.UserUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by xin on 18/9/22.
 */
public class MineFragment extends BaseFragment {

    private ImageView ivAvatar;
    private TextView tvNickname;
    private LinearLayout llMember;
    private LinearLayout llFeedback;
    private TextView tvLogout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_mine, null);

        // findViewById
        ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
        tvNickname = (TextView) view.findViewById(R.id.tvNickname);
        llMember = (LinearLayout) view.findViewById(R.id.llMember);
        llFeedback = (LinearLayout) view.findViewById(R.id.llFeedback);
        tvLogout = (TextView) view.findViewById(R.id.tvLogout);

        initView();
        return view;
    }

    @Override
    protected void initView() {
        if (UserUtils.checkLoginStatus()) { // 已登录状态
            // 昵称
            String nickname = "null";
            UserBean userBean = DBHelper.getUserBean();
            if (userBean != null && !TextUtils.isEmpty(userBean.getNickName())) {
                nickname = userBean.getNickName();
            }
            tvNickname.setText(nickname);
            tvNickname.setOnClickListener(null);

            // 切换账号
            tvLogout.setVisibility(View.VISIBLE);
            tvLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogHelper.createTCAlertDialog(mContext, "", "确认退出登录？", "确定", "取消", false
                            , new TCAlertDialog.OnTCActionListener() {
                                @Override
                                public void onOkAction() {
                                    // 退出登录
                                    CommonParam commonParam = new CommonParam();
                                    commonParam.setData("");

                                    MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiUserLogout);
                                    myRequestEntity.setContentWithHeader(ApiRequest.getContent(commonParam));
                                    RequestSender.get().send(myRequestEntity, new RequestCallback<Object>() {
                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public void onSuccess(int code, Object object) {
                                            ToastUtils.show("已退出登录");
                                        }

                                        @Override
                                        public void onFailure(int code, String msg) {
                                            ToastUtils.show(msg);
                                        }
                                    });
                                    // 清除用户信息
                                    DBHelper.setUserBean(null);
                                    // 通知注销成功
                                    UserUtils.doLogout();
                                    // 页面跳转
                                    startActivity(new Intent(mContext, LoginActivity.class));
                                }

                                @Override
                                public void onCancelAction() {

                                }
                            }).show();
                }
            });
        } else { // 未登录状态
            // 昵称
            tvNickname.setText("点击登录");
            tvNickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
            });

            // 切换账号
            tvLogout.setVisibility(View.GONE);
            tvLogout.setOnClickListener(null);
        }

        // 头像
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("头像");
            }
        });

        // 升级成会员
        llMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("功能暂未开放");
//                startActivity(new Intent(mContext, MemberActivity.class));
            }
        });

        // 意见反馈
        llFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, FeedbackActivity.class));
            }
        });
    }

    /**
     * onEvent
     */
    public void onEvent(UserLoginStatusEvent event) {
        initView();
    }
}
