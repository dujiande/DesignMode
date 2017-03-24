package me.djd.designmode.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;

import butterknife.BindView;
import me.djd.designmode.R;
import me.djd.designmode.adapter.DataAdapter;
import me.djd.designmode.base.BaseActivity;
import me.djd.designmode.model.ItemModelVo;
import me.djd.designmode.utils.AppToast;
import me.djd.designmode.utils.NetworkUtils;

/**
 * Created by dujiande on 2017/3/20.
 */

public class NormalListActivity extends BaseActivity {

    /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 34;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.list)
    LRecyclerView mRecyclerView;

    private DataAdapter mDataAdapter = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (aty == null || aty.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case -1:
                    int currentSize = mDataAdapter.getItemCount();
                    //模拟组装10个数据
                    ArrayList<ItemModelVo> newList = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
                            break;
                        }

                        ItemModelVo item = new ItemModelVo();
                        item.id = currentSize + i;
                        item.setTitle("item" + (item.id));

                        newList.add(item);
                    }
                    mDataAdapter.addAll(newList);
                    mCurrentCounter += newList.size();
                    mRecyclerView.refreshComplete(REQUEST_COUNT);
                    break;
                case -3:
                    mRecyclerView.refreshComplete(REQUEST_COUNT);
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                    mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            requestData();
                        }
                    });

                    break;
                default:
                    break;
            }
        }
    };

    public static void appJump(Context context){
        Intent intent = new Intent(context,NormalListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int setRootView() {
        return R.layout.activity_normal_list;
    }

    @Override
    public void initData() {
        toolbar.setTitle("普通列表demo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDataAdapter = new DataAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        //add a HeaderView
//        final View header = LayoutInflater.from(this).inflate(R.layout.sample_header,(ViewGroup)findViewById(android.R.id.content), false);
//        mLRecyclerViewAdapter.addHeaderView(header);
    }

    @Override
    public void initListener() {
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                mDataAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                requestData();

            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (mCurrentCounter < TOTAL_COUNTER) {
                    // loading more
                    requestData();
                } else {
                    //the end
                    mRecyclerView.setNoMore(true);
                }
            }
        });

        mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {

            @Override
            public void onScrollUp() {
                Log.d("test","onScrollUp");
            }

            @Override
            public void onScrollDown() {
                Log.d("test","onScrollDown");
            }


            @Override
            public void onScrolled(int distanceX, int distanceY) {
                Log.d("test",String.format(" onScrolled distanceX=%d,distanceY=%d",distanceX,distanceY));
            }

            @Override
            public void onScrollStateChanged(int state) {
                Log.d("test","onScrollStateChanged "+state);
            }

        });

        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.colorAccent, R.color.dark ,android.R.color.white);
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.dark ,android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中","已经全部为你呈现了","网络不给力啊，点击再试一次吧");

        mRecyclerView.refresh();

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mDataAdapter.getDataList().size() > position) {
                    ItemModelVo item = mDataAdapter.getDataList().get(position);
                    AppToast.showShortText(getApplicationContext(), item.getTitle());
                    mDataAdapter.remove(position);
                }

            }

        });

        mLRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                ItemModelVo item = mDataAdapter.getDataList().get(position);
                AppToast.showShortText(getApplicationContext(), "onItemLongClick - " + item.getTitle());
            }
        });
    }

    /**
     * 模拟请求网络
     */
    private void requestData() {
        new Thread() {

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //模拟一下网络请求失败的情况
                if(NetworkUtils.isNetAvailable(aty)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(menu.size() == 0){
            getMenuInflater().inflate(R.menu.menu_title_refresh, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_refresh:
                mRecyclerView.forceToRefresh();
                break;
        }
        return true;
    }
}
