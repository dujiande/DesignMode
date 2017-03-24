package me.djd.designmode.api;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dujiande on 2017/3/21.
 */

public abstract class ApiCallBack<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        onSuccess(call,response);
        onFinish(call);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFail(call,t);
        onFinish(call);
    }

    public abstract void  onSuccess(Call<T> call, Response<T> response);

    public void onFail(Call<T> call, Throwable t){

    }

    public void onFinish(Call<T> call){

    }
}
