package com.gxz.bindview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.BindView;
import com.example.OnClick;
import com.gxz.bindview_api.ViewInjector;

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/7/20-下午5:26
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MyFragment extends Fragment {

    @BindView(R.id.ok)
    Button ok;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_layout, container, false);
        ViewInjector.inject(this, view);
        return view;
    }

    @OnClick(R.id.ok)
    public void clickOk() {
        Toast.makeText(getContext(), "clickOk", Toast.LENGTH_LONG).show();
    }
}
