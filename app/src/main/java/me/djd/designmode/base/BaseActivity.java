package me.djd.designmode.base;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dou361.statusbar.StatusBarUtil;

import butterknife.ButterKnife;
import me.djd.designmode.R;

/**
 * Created by dujiande on 2017/2/27.
 * activity 的基类 使用模板设计模式。
 */

public abstract class BaseActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST = 2;

    public Activity aty;

    public boolean checkIfHasPermisions(String[] permissionArr) {
        for (int i = 0; i < permissionArr.length; i++) {
            if (ContextCompat.checkSelfPermission(aty, permissionArr[i])
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void requestPermision(String[] permissionArr) {
        ActivityCompat.requestPermissions(this, permissionArr, MY_PERMISSIONS_REQUEST);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                grantedPermissionCallBack();
            } else {
                // Permission Denied
                DeniedPermissionCallBack();

            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void DeniedPermissionCallBack() {
        Toast.makeText(aty, "Permission Denied", Toast.LENGTH_SHORT).show();
    }

    public void grantedPermissionCallBack(){
        Toast.makeText(aty, "Permission granted", Toast.LENGTH_SHORT).show();
    }

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
