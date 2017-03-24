package me.djd.designmode.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dujiande on 2017/1/17.
 * adapter 基类
 */
public abstract class BasicAdapter<T> extends android.widget.BaseAdapter {

    public LayoutInflater layoutInflater = null;
    public List<T> datalist = null;
    public Context context;

    public BasicAdapter(Context context) {
        this.context = context;
        datalist = new ArrayList<T>();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(List<T> datalist){
        this.datalist = datalist;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(datalist == null){
            return 0;
        }
        return datalist.size();
    }

    @Override
    public T getItem(int i) {
        if(datalist == null){
            return null;
        }
        return datalist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try{
             BaseHolder<T> baseHolder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(setLayout(), null);
                baseHolder = getHolder(convertView);
                convertView.setTag(baseHolder);
            } else {
                baseHolder = (BaseHolder) convertView.getTag();
            }
            baseHolder.bindData(datalist.get(position));

        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

    //设置布局
    public abstract int setLayout();

    //获取一个holder,子类必须实现的方法,子类返回一个holder
    public abstract BaseHolder<T> getHolder(View convertView);

}
