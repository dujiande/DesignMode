package me.djd.designmode.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import me.djd.designmode.R;
import me.djd.designmode.api.ApiCallBack;
import me.djd.designmode.api.ApiController;
import me.djd.designmode.api.ProgressRequestBody;
import me.djd.designmode.api.ProgressRequestListener;
import me.djd.designmode.api.RequestService;
import me.djd.designmode.api.Result.ResultBase;
import me.djd.designmode.api.ServiceGenerator;
import me.djd.designmode.base.BaseActivity;
import me.djd.designmode.utils.FilePathResolver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dujiande on 2017/3/21.
 */

public class RetrofitActivity extends BaseActivity {

    private static final int MY_PERMISSIONS_REQUEST = 2;

    @BindView(R.id.button)
    Button btn;
    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.post_btn)
    Button postBtn;
    @BindView(R.id.upload_btn)
    Button uploadBtn;

    String[] permissionArr = {Manifest.permission.READ_EXTERNAL_STORAGE};

    public static void appJump(Context context) {
        Intent intent = new Intent(context, RetrofitActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int setRootView() {
        return R.layout.activity_retrofit;
    }

    @Override
    public void initData() {

    }

    private boolean checkIfHasPermisions(String[] permissionArr) {
        for (int i = 0; i < permissionArr.length; i++) {
            if (ContextCompat.checkSelfPermission(aty, permissionArr[i])
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermision(String[] permissionArr) {
        ActivityCompat.requestPermissions(this, permissionArr, MY_PERMISSIONS_REQUEST);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseFile();
            } else {
                // Permission Denied
                Toast.makeText(aty, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void initListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestHttp();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPost();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
    }

    private void chooseFile() {
        if (checkIfHasPermisions(permissionArr)) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "选择文件"), 1);
        } else {
            requestPermision(permissionArr);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String fileUrl = FilePathResolver.getPath(aty, uri);
                String mark = "我是备注";
                Toast.makeText(this, "文件路径：" + fileUrl, Toast.LENGTH_SHORT).show();
                upload(fileUrl, mark);
            }
        }
    }

    private void requestPost() {
        Map hashMap = new HashMap();
        hashMap.put("account", "千里不留痕");
        hashMap.put("pwd", "hahahhahah");
        Call<ResultBase> call = ApiController.getInstance().getRequestService().testJson(hashMap);
        call.enqueue(new ApiCallBack<ResultBase>() {
            @Override
            public void onSuccess(Call<ResultBase> call, Response<ResultBase> response) {
                textView.setText(response.body().getResponseMsg());
            }
        });
    }

    private void requestHttp() {
        RequestService requestService = ApiController.getInstance().getRequestService();
        Call<List<String>> call = requestService.listRepos();
        call.enqueue(new ApiCallBack<List<String>>() {
            @Override
            public void onSuccess(Call<List<String>> call, Response<List<String>> response) {
                if (response != null) {
                    List<String> datalist = response.body();
                    StringBuilder sb = new StringBuilder("");
                    for (int i = 0; i < datalist.size(); i++) {
                        sb.append(datalist.get(i) + "\n");
                    }
                    textView.setText(sb.toString());
                }
            }

            public void onFinish(Call<List<String>> call) {
                Toast.makeText(aty, "onFinish", Toast.LENGTH_SHORT).show();
            }

            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
            }
        });

    }

    private void upload(String fileUri, String markString) {
        //添加进度回调
        RequestService service = ServiceGenerator.createService(RequestService.class);
        File file = new File(fileUri);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//将requestFile封装成ProgressRequestBody传入
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(),
                        new ProgressRequestBody(requestFile, new ProgressRequestListener() {
                            @Override
                            public void onRequestProgress(final long bytesWritten, final long contentLength, final boolean done) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText(
                                                String.format("bytesWritten=%d,contentLength=%d,done=%b", bytesWritten, contentLength, done));
                                    }
                                });

                            }
                        }));
        RequestBody mark =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), markString);
        Call<ResultBase> call = service.upload(body, mark);
        call.enqueue(new ApiCallBack<ResultBase>() {
            @Override
            public void onSuccess(Call<ResultBase> call, Response<ResultBase> response) {
                textView.setText(response.body().getResponseMsg());
            }
        });
    }
}
