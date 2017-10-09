package com.feng.com.api_test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.feng.com.api_test.bean.ResBean;
import com.mkit.libmkit.api.Mkit;
import com.mkit.libmkit.bean.DataCallBackBody;
import com.mkit.libmkit.ui.MkitActivity;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_get;
    private Button btn_setting;
    private Button btn_jump;
    private TextView tv_data;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        Mkit.Initialization(this);
        inItView();
    }

    private void inItView() {
        btn_get = (Button) findViewById(R.id.btn_get);
        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_jump = (Button) findViewById(R.id.btn_jump);
        tv_data = (TextView) findViewById(R.id.tv_show);
        btn_get.setOnClickListener(this);
        btn_jump.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                getData();
                break;
            case R.id.btn_setting:
                Intent in = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                startActivity(in);
                break;
            case R.id.btn_jump:
                Intent intent=new Intent(mContext, MkitActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getData() {
        Mkit.getInstance().getData(new DataCallBackBody() {
            @Override
            public void dataCall(Response<ResponseBody> response) {
                try {
                    tv_data.setText("");
                    String string = response.body().string();
                    if (string == null) {
                        return;
                    }
                    ResBean resBean = JSONObject.parseObject(string, ResBean.class);
                    for (ResBean.DataBean.ItemsBean itemsBean : resBean.getData().getItems()) {
                        tv_data.append(itemsBean.getUuid()+"\n");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }
}
