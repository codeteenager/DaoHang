package com.shuaijie.jiang.daohang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.shuaijie.jiang.daohang.R;
import com.shuaijie.jiang.daohang.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class RouteInfoActivity extends BaseActivity {
    private AutoCompleteTextView et_st, et_en;
    private SuggestionSearch mSuggestionSearch = null;
    private List<String> suggestSt;
    private ArrayAdapter<String> sugAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);
        actionBar.setTitle("路线信息");
        et_en = (AutoCompleteTextView) findViewById(R.id.et_en);
        et_st = (AutoCompleteTextView) findViewById(R.id.et_st);
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult res) {
                if (res == null || res.getAllSuggestions() == null) {
                    return;
                }
                suggestSt = new ArrayList<String>();
                for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
                    if (info.key != null) {
                        suggestSt.add(info.key);
                    }
                }
                sugAdapter = new ArrayAdapter<String>(RouteInfoActivity.this, android.R.layout.simple_dropdown_item_1line, suggestSt);
                et_st.setAdapter(sugAdapter);
                et_en.setAdapter(sugAdapter);
                sugAdapter.notifyDataSetChanged();
            }
        });
        et_st.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line));
        et_st.setThreshold(1);
        et_st.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() <= 0) {
                    return;
                }

                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                String currentCity = CommonUtils.getSpStr(getApplicationContext(), "currentCity", "");
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(charSequence.toString()).city(currentCity));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_en.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line));
        et_en.setThreshold(1);
        et_en.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() <= 0) {
                    return;
                }

                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                String currentCity = CommonUtils.getSpStr(getApplicationContext(), "currentCity", "");
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(charSequence.toString()).city(currentCity));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void search(View view) {
        String st = et_st.getText().toString().trim();
        String en = et_en.getText().toString().trim();
        if (TextUtils.isEmpty(en)) {
            Toast.makeText(getApplicationContext(), "终点不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(RouteInfoActivity.this, RoutePlanActivity.class);
        intent.putExtra("st", st);
        intent.putExtra("en", en);
        startActivity(intent);
    }
}
