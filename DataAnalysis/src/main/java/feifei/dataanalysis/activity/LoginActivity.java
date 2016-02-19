package feifei.dataanalysis.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import feifei.dataanalysis.R;
import feifei.dataanalysis.base.BaseActivity;
import feifei.dataanalysis.base.MyApplication;
import feifei.project.util.AnimUtil;
import feifei.project.util.Tools;
import feifei.project.view.ClearEditText;

//检查更新 初始化数据统计 异常信息 检查登录
public class LoginActivity extends BaseActivity {

    public final String p = "666";

    @InjectView(R.id.edit)
    ClearEditText edit;

    @InjectView(R.id.button)
    Button button;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        tint();
        SharedPreferences sp = getSharedPreferences(MyApplication.KEY_LOGIN_FILE, MODE_PRIVATE);
        editor = sp.edit();
        boolean login = sp.getBoolean(MyApplication.KEY_LOGIN, false);
        if (login) {
            startActivity(new Intent(activity, MainActivity.class));
            AnimUtil.animToFinish (activity);
            return;
        }
        initOther();

    }

    @OnClick(R.id.button)
    public void veryLogin() {
        if (edit.getText().toString().trim().equals(p)) {
            data(activity, "登录进入");
            editor.putBoolean(MyApplication.KEY_LOGIN, true).apply();
            startActivity(new Intent(activity, MainActivity.class));
            AnimUtil.animToFinish(activity);
        } else {
            AnimUtil.animShakeText(edit);
            Tools.toast (activity, "密钥错误");
        }
    }

    //这里只是和登录界面相关的一些东西
    private void initOther() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setSwipeBackEnable(false);
        }
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }
        });
        if (edit.getText().toString().trim().length() > 0) {
            button.setEnabled(true);
        } else {
            button.setEnabled(false);
        }
    }

//    private void initUpdate() {
//        UmengUpdateAgent.update(this);
//        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//            @Override
//            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
//                switch (updateStatus) {
//                    case UpdateStatus.Yes:
//                        UmengUpdateAgent.showUpdateDialog(LoginActivity.this, updateInfo);
//                        break;
//                    //没有更新
//                    case UpdateStatus.No:
//                        //没有wifi连接， 只在wifi下更新
//                    case UpdateStatus.NoneWifi:
//                        //超时
//                    case UpdateStatus.Timeout:
//                        jumpUpdate();
//                        break;
//                    default:
//                        jumpUpdate();
//                }
//            }
//        });
//
//        UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
//
//            @Override
//            public void onClick(int status) {
//                switch (status) {
//                    case UpdateStatus.Update:
//                        break;
//                    case UpdateStatus.Ignore:
//                        break;
//                    case UpdateStatus.NotNow:
//                        jumpUpdate();
//                        break;
//                }
//            }
//        });
//    }


    //    public void jumpUpdate() {
    //        startActivity(new Intent(activity, MainActivity.class));
    //        AnimUtil.animToFinish(activity);
    //    }

}
