package me.djd.designmode.ui;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.djd.designmode.R;
import me.djd.designmode.adapter.MainAdapter;
import me.djd.designmode.base.BaseActivity;
import me.djd.designmode.model.MainItemVo;

public class MainActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listView;

    List<MainItemVo> datalist;
    MainAdapter adapter;
    @Override
    public int setRootView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        datalist = new ArrayList<MainItemVo>();
        adapter = new MainAdapter(this);
        String[] itemstr = {
                "普通列表",
                "网络请求",
                "策略模式",
                "单例模式",
                "模板方法模式"
        };
        datalist.clear();
        for(int i=0;i<itemstr.length;i++){
            MainItemVo temp = new MainItemVo();
            temp.setName(itemstr[i]);
            datalist.add(temp);
        }
        adapter.refresh(datalist);
        listView.setAdapter(adapter);


    }

    @Override
    public void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainItemVo itemdata = (MainItemVo) parent.getAdapter().getItem(position);
                switch (itemdata.getName()){
                    case "普通列表":
                        NormalListActivity.appJump(aty);
                        break;
                    case "网络请求":
                        RetrofitActivity.appJump(aty);
                        break;
                    default:
                        Toast.makeText(aty,itemdata.getName(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
