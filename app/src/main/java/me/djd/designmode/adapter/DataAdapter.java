package me.djd.designmode.adapter;

import android.content.Context;
import android.widget.TextView;

import me.djd.designmode.R;
import me.djd.designmode.base.ListBaseAdapter;
import me.djd.designmode.base.SuperViewHolder;
import me.djd.designmode.model.ItemModelVo;

/**
 * Created by dujiande on 2017/3/20.
 */

public class DataAdapter extends ListBaseAdapter<ItemModelVo> {

    public DataAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_main;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ItemModelVo item = mDataList.get(position);

        TextView titleText = holder.getView(R.id.tv_name);
        titleText.setText(item.getTitle());
    }
}
