package me.djd.designmode.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dou361.statusbar.StatusBarUtil;

import butterknife.ButterKnife;
import me.djd.designmode.R;

/**
 * Created by dujiande on 2017/2/27.
 * activity 的基类 使用模板设计模式。
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Activity aty;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(setRootView());
        BaseApplication.getInst().addActivity(this);
        aty = this;
        ButterKnife.bind(this);
        if (openStatus()) {
            StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.colorPrimary));
        }
        initData();
        initListener();
    }

    /**
     * 是否使用沉浸式状态栏
     */
    protected boolean openStatus() {
        return true;
    }


    @Override
    protected void onDestroy() {
        BaseApplication.getInst().remove(this);
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    public abstract int setRootView();
    public abstract void initData();
    public abstract void initListener();

}
