package me.djd.designmode.base;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by dujiande on 2017/1/17.
 * holder 基类
 */
public abstract class BaseHolder<T> {

    public BaseHolder(View holderView) {
        ButterKnife.bind(this, holderView);
    }

    public abstract void bindData(T data);

}
