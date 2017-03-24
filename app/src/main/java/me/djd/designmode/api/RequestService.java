package me.djd.designmode.api;

import java.util.List;
import java.util.Map;

import me.djd.designmode.api.Result.ResultBase;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by dujiande on 2017/3/21.
 */

public interface RequestService {
    @GET("data/repos")
    Call<List<String>> listRepos();

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("data/testjson")
    Call<ResultBase> testJson(@Body Map parms);

    @Multipart
    @POST("data/upload")
    Call<ResultBase> upload(@Part MultipartBody.Part body, @Part("mark") RequestBody mark);
}
