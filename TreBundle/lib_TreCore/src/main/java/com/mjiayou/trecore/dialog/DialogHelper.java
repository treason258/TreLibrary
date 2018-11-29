package com.mjiayou.trecore.dialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import com.mjiayou.trecore.bean.entity.TCMenu;
import com.mjiayou.trecore.util.MenuUtil;
import com.mjiayou.trecorelib.util.ToastUtil;
import com.mjiayou.trecore.util.UserUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by treason on 16/5/26.
 */
public class DialogHelper {

    private static final String TAG = "DialogHelper";

    // ******************************** createAlertBuilder ********************************

    /**
     * 创建 AlertDialog
     */
    public static AlertDialog.Builder createAlertBuilder(Context context, String title, String message,
                                                         String posiText, DialogInterface.OnClickListener onPosiClickListener,
                                                         String negaText, DialogInterface.OnClickListener onNegaClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (!TextUtils.isEmpty(posiText)) {
            builder.setPositiveButton(posiText, onPosiClickListener);
        }
        if (!TextUtils.isEmpty(negaText)) {
            builder.setNegativeButton(negaText, onNegaClickListener);
        }
        return builder;
    }

    // ******************************** createAlertMenuBuilder ********************************

    /**
     * 创建 Item 样式的 AlertDialog
     */
    public static AlertDialog.Builder createAlertMenuBuilder(Context context, String title, String[] items,
                                                             DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (null != items) {
            builder.setItems(items, onClickListener);
        }
        return builder;
    }

    // ******************************** createProgressDialog ********************************

    /**
     * createProgressDialog
     */
    public static ProgressDialog createProgressDialog(Context context, String title, String message, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            dialog.setMessage(message);
        }
        dialog.setCancelable(cancelable);
        return dialog;
    }

    // ******************************** createTCAlertDialog ********************************

    /**
     * 创建 TCAlertDialog
     */
    public static TCAlertDialog createTCAlertDialog(Context context, String title, String message,
                                                    String okStr, String cancelStr, boolean cancelable,
                                                    final TCAlertDialog.OnTCActionListener onTCActionListener) {
        final TCAlertDialog dialog = new TCAlertDialog(context);

        // title
        dialog.setTitle(title);
        // message
        dialog.setMessage(message);
        // menu
        dialog.setOkMenu(okStr);
        dialog.setCancelMenu(cancelStr);
        dialog.setTCActionListener(onTCActionListener);
        // cancelable
        dialog.setCancelable(cancelable);

        return dialog;
    }

    // ******************************** createTCAlertMenuDialog ********************************

    /**
     * 创建 TCAlertMenuDialog
     */
    public static TCAlertMenuDialog createTCAlertMenuDialog(Context context, String title, String message,
                                                            boolean cancelable, List<TCMenu> tcMenus) {
        final TCAlertMenuDialog dialog = new TCAlertMenuDialog(context);

        // title
        dialog.setTitle(title);
        // message
        dialog.setMessage(message);
        // menu
        dialog.setMenu(tcMenus);
        // cancelable
        dialog.setCancelable(cancelable);

        return dialog;
    }

    // ******************************** createTCBottomMenuDialog ********************************

    /**
     * 创建 TCBottomMenuDialog
     */
    public static TCBottomMenuDialog createTCBottomMenuDialog(final Context context, TCBottomMenuDialog.LayoutType layoutType,
                                                              String title, String message, boolean cancelable, List<TCMenu> tcMenus) {
        final TCBottomMenuDialog dialog = new TCBottomMenuDialog(context, layoutType);

        // title
        dialog.setTitle(title);
        // message
        dialog.setMessage(message);
        // menu
        dialog.setMenu(tcMenus);
        // cancelable
        dialog.setCancelable(cancelable);

        return dialog;
    }

    public static TCBottomMenuDialog createTCBottomMenuDialog(final Context context, String title, String message,
                                                              boolean cancelable, List<TCMenu> tcMenus) {
        return createTCBottomMenuDialog(context, TCBottomMenuDialog.LayoutType.DEFAULT, title, message, cancelable, tcMenus);
    }

    // ******************************** createTCBirthdayDialog ********************************

    public static TCBirthdayDialog createTCBirthdayDialog(final Context context,
                                                          TCBirthdayDialog.OnTimeSelectedListener onTimeSelectedListener,
                                                          View.OnClickListener onSubmitClickListener) {
        final TCBirthdayDialog dialog = new TCBirthdayDialog(context);

        // listener
        dialog.setOnTimeSelectListener(onTimeSelectedListener);
        dialog.setOnSubmitClickListener(onSubmitClickListener);

        return dialog;
    }

    // ******************************** showDialogDemo ********************************

    private static final long TIME_PROGRESS = 5 * 1000;
    private static final long TIME_PROGRESS_INTERVAL = 1000;

    private static CountDownTimer mCountDownTimer;

    private static String getOnTickMessage(long millisUntilFinished) {
        return "请稍后... | onTick -> " + millisUntilFinished;
    }

    /**
     * showTCAlertDialogDemo
     */
    public static void showDialogDemo(final Context context) {
        List<TCMenu> tcMenus = new ArrayList<>();
        tcMenus.add(new TCMenu("1-1 - createAlertBuilder", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // createAlertBuilder
                DialogHelper.createAlertBuilder(context, "title", "message",
                        "ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtil.show("ok - onClick");
                            }
                        },
                        "cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtil.show("cancel - onClick");
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(true)
                        .show();
            }
        }));
        tcMenus.add(new TCMenu("1-2 - createAlertMenuBuilder", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // createAlertMenuBuilder
                DialogHelper.createAlertMenuBuilder(context, "title",
                        new String[]{"菜单0", "菜单1", "菜单2"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        ToastUtil.show("菜单0 - onClick");
                                        break;
                                    case 1:
                                        ToastUtil.show("菜单1 - onClick");
                                        break;
                                    case 2:
                                        ToastUtil.show("菜单2 - onClick");
                                        break;
                                }
                            }
                        })
                        .show();
            }
        }));
        tcMenus.add(new TCMenu("1-3 - createProgressDialog", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // createProgressDialog
                final ProgressDialog progressDialog = DialogHelper.createProgressDialog(context, "title", "message", true);
                progressDialog.show();

                // CountDownTimer
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                    mCountDownTimer = null;
                }
                mCountDownTimer = new CountDownTimer(TIME_PROGRESS, TIME_PROGRESS_INTERVAL) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        progressDialog.setMessage(getOnTickMessage(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        progressDialog.dismiss();
                    }
                };
                mCountDownTimer.start();
            }
        }));
        tcMenus.add(new TCMenu("2-1 - createTCAlertDialog", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // createTCAlertDialog
                DialogHelper.createTCAlertDialog(context, "title", "message", "ok", "cancel", true,
                        new TCAlertDialog.OnTCActionListener() {
                            @Override
                            public void onOkAction() {
                                ToastUtil.show("onOkAction");
                            }

                            @Override
                            public void onCancelAction() {
                                ToastUtil.show("onCancelAction");
                            }
                        }).show();
            }
        }));
        tcMenus.add(new TCMenu("2-2 - createTCAlertMenuDialog", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // createTCAlertMenuDialog
                DialogHelper.createTCAlertMenuDialog(context, "title", "message", true, MenuUtil.getTCMenus(context)).show();
            }
        }));
        tcMenus.add(new TCMenu("2-3 - createTCBottomMenuDialog", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // createTCBottomMenuDialog
                DialogHelper.createTCBottomMenuDialog(context, "title", "message", true, MenuUtil.getTCMenus(context)).show();
            }
        }));
        tcMenus.add(new TCMenu("2-4 - createTCBirthdayDialog", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // createTCBirthdayDialog
                DialogHelper.createTCBirthdayDialog(context,
                        new TCBirthdayDialog.OnTimeSelectedListener() {
                            @Override
                            public void onTimeSelected(Calendar calendar, int age, String constellation) {
                                ToastUtil.show(age + " -> " + constellation);
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.show("onClick");
                            }
                        }).show();
            }
        }));
        tcMenus.add(new TCMenu("3-1 - DefaultProgressDialog", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DefaultProgressDialog
                DefaultProgressDialog.createDialog(context).show();

                // CountDownTimer
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                    mCountDownTimer = null;
                }
                mCountDownTimer = new CountDownTimer(TIME_PROGRESS, TIME_PROGRESS_INTERVAL) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        DefaultProgressDialog.updateDialog(getOnTickMessage(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        DefaultProgressDialog.dismissDialog();
                    }
                };
                mCountDownTimer.start();
            }
        }));
        tcMenus.add(new TCMenu("3-2 - TCProgressDialog", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TCProgressDialog
                TCProgressDialog.createDialog(context).show();

                // CountDownTimer
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                    mCountDownTimer = null;
                }
                mCountDownTimer = new CountDownTimer(TIME_PROGRESS, TIME_PROGRESS_INTERVAL) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        TCProgressDialog.updateDialog(getOnTickMessage(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        TCProgressDialog.dismissDialog();
                    }
                };
                mCountDownTimer.start();
            }
        }));
        tcMenus.add(new TCMenu("3-3 - TCLoadingDialog", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TCLoadingDialog
                TCLoadingDialog.createDialog(context).show();

                // CountDownTimer
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                    mCountDownTimer = null;
                }
                mCountDownTimer = new CountDownTimer(TIME_PROGRESS, TIME_PROGRESS_INTERVAL) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        TCLoadingDialog.updateDialog(getOnTickMessage(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        TCLoadingDialog.dismissDialog();
                    }
                };
                mCountDownTimer.start();
            }
        }));
        tcMenus.add(new TCMenu("4-1 - TCUserInfoDialog", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TCUserInfoDialog
                TCUserInfoDialog.createDialog(context, "张三").show();
            }
        }));
        tcMenus.add(new TCMenu("4-2 - TCUserListDialog", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TCUserListDialog
                TCUserListDialog.createDialog(context, UserUtil.getUUID()).show();
            }
        }));
        DialogHelper.createTCAlertMenuDialog(context, "DialogDemo", "DialogHelper.showDialogDemo()", true, tcMenus).show();
    }
}
