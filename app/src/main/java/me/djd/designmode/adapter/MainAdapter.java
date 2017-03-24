package me.djd.designmode.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import me.djd.designmode.R;
import me.djd.designmode.base.BasicAdapter;
import me.djd.designmode.base.BaseHolder;
import me.djd.designmode.model.MainItemVo;

/**
 * Created by dujiande on 2017/3/10.
 */

public class MainAdapter extends BasicAdapter<MainItemVo> {

    public MainAdapter(Context context) {
        super(context);
    }

    @Override
    public int setLayout() {
        return R.layout.item_main;
    }

    @Override
    public BaseHolder<MainItemVo> getHolder(View convertView) {
        return new MyHolder(convertView);
    }

    class MyHolder extends BaseHolder<MainItemVo> {
        @BindView(R.id.tv_name)
        TextView nameTv;

        public MyHolder(View holderView) {
            super(holderView);
        }

        @Override
        public void bindData(MainItemVo data) {
            nameTv.setText(data.getName());
        }
    }
}
