package com.shuaijie.jiang.daohang.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.shuaijie.jiang.daohang.R;
import com.shuaijie.jiang.daohang.utils.CommonUtils;

public class SettingActivity extends BaseActivity {
    private Switch switch_show_zoom;
    private TextView tv_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        actionBar.setTitle("设置");
        tv_theme = (TextView) findViewById(R.id.tv_theme);
        switch_show_zoom = (Switch) findViewById(R.id.switch_show_zoom);
        if (CommonUtils.getSpBool(getApplicationContext(), "showZoom", true)) {
            switch_show_zoom.setChecked(true);
        } else {
            switch_show_zoom.setChecked(false);
        }
        switch_show_zoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    CommonUtils.setSpBool(getApplicationContext(), "showZoom", true);
                } else {
                    CommonUtils.setSpBool(getApplicationContext(), "showZoom", false);
                }
            }
        });
        tv_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("请选择主题");
                String[] data = {"普通模式", "黑夜模式", "清新蓝色", "午夜蓝色"};
                builder.setItems(data, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                CommonUtils.setSpInt(getApplicationContext(), "currentTheme", 0);
                                break;
                            case 1:
                                CommonUtils.setSpInt(getApplicationContext(), "currentTheme", 1);
                                break;
                            case 2:
                                CommonUtils.setSpInt(getApplicationContext(), "currentTheme", 2);
                                break;
                            case 3:
                                CommonUtils.setSpInt(getApplicationContext(), "currentTheme", 3);
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }
}
