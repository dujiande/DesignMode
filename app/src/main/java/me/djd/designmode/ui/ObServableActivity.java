package me.djd.designmode.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import me.djd.designmode.R;
import me.djd.designmode.base.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by dujiande on 2017/3/29.
 */

public class ObServableActivity extends BaseActivity {

    @BindView(R.id.observable_btn)
    Button observableBtn;
    @BindView(R.id.result_tv)
    TextView resultTv;

    StringBuilder sb;
    Observable<Long> myObservable;

    public static void appJump(Context context) {
        Intent intent = new Intent(context, ObServableActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int setRootView() {
        return R.layout.activity_observable;
    }

    @Override
    public void initData() {
        sb = new StringBuilder("");
    }

    /**
     * 1.使用create( ),最基本的创建方式：
     */
    private void actionCreate(){
        Observable.create(new Observable.OnSubscribe<String>(){

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("one");
                subscriber.onNext("two");
                subscriber.onNext("three");
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                sb.append(s+"\n");
                resultTv.setText(sb.toString());
            }
        });

    }

    /**
     * 2使用just( )，将为你创建一个Observable并自动为你调用onNext( )发射数据
     */
    private void actionJust(){
        Observable.just("one","two","three")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        sb.append(s+"\n");
                        resultTv.setText(sb.toString());
                    }
                });
    }

    /**
     * 3.使用from( )，遍历集合，发送每个item
     */
    private void actionFrom(){
        List<String> datalist = new ArrayList<String>();
        datalist.add("one");
        datalist.add("two");
        datalist.add("three");
        Observable.from(datalist).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                sb.append(s+"\n");
                resultTv.setText(sb.toString());
            }
        });
    }

    /**
     * 4.使用defer( )，有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable
     */
    private void actionDefer(){
        Observable<String> deferObservable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just("one");
            }
        });

        deferObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                sb.append(s+"\n");
                resultTv.setText(sb.toString());
            }
        });
    }

    /**
     * 5.使用interval( ),创建一个按固定时间间隔发射整数序列的Observable，可用作定时器
     */
    private void actionInterval(){
        myObservable = Observable.interval(1, TimeUnit.SECONDS);
        myObservable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                sb.append(aLong+"\n");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultTv.setText(sb.toString());
                    }
                });

            }
        });
    }

    @Override
    public void initListener() {
        observableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionInterval();
            }
        });
    }
}
