package com.gxz.bindview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.BindLayout;
import com.example.BindView;
import com.example.OnClick;
import com.gxz.bindview_api.ViewInjector;

@BindLayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        ViewInjector.inject(this);
        mTextView.setText("11111111");
    }

    @OnClick({R.id.id_btn0, R.id.id_btn1})
    public void ok() {
        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.id_btn2)
    public void goList() {
        startActivity(new Intent(this, MainActivityList.class));
    }

}
