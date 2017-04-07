package me.djd.designmode.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import me.djd.designmode.R;
import me.djd.designmode.base.BaseActivity;

/**
 * Created by dujiande on 2017/4/5.
 */

public class ProPertyAnimationActivity extends BaseActivity {

    @BindView(R.id.action_btn)
    Button actionBtn;
    @BindView(R.id.show_iv)
    ImageView showIv;
    Toast toast;

    public static void appJump(Context context) {
        Intent intent = new Intent(context, ProPertyAnimationActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int setRootView() {
        return R.layout.activity_property_animation;
    }

    @Override
    public void initData() {

    }

    private void runPropertyAnimation(){
        ObjectAnimator a1 = ObjectAnimator.ofFloat(showIv, "alpha", 0.0f, 1.0f);
        ObjectAnimator a2 = ObjectAnimator.ofFloat(showIv, "translationY", 0f, 300f);
        ObjectAnimator a3 = ObjectAnimator.ofFloat(showIv, "translationX", 0f, 300f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(5000);
        animSet.setInterpolator(new LinearInterpolator());
        animSet.playTogether(a1, a2, a3); //两个动画同时执行
        //animSet.play(a1).after(a2); //先后执行
        //animSet.playSequentially(a1,a2,a3);
        animSet.start();
    }

    private void runViewAnimation(){
        TranslateAnimation animation = new TranslateAnimation(0,300,0,300);
        animation.setDuration(5000);
        animation.setFillAfter(true);
        showIv.startAnimation(animation);
    }

    @Override
    public void initListener() {
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runPropertyAnimation();
                //runViewAnimation();

            }
        });

        showIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toast != null){
                    toast.cancel();

                }
                toast = Toast.makeText(aty,"you click me",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
