package com.gxz.bindview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.BindView;
import com.gxz.bindview_api.ViewInjector;

import java.util.ArrayList;
import java.util.List;

public class MainActivityList extends AppCompatActivity {


    @BindView(R.id.id_listview)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_list);

        ViewInjector.inject(this);


        List<String> list = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            list.add("ViewInjector--" + i);
        }

        MyAdapter myAdapter = new MyAdapter(this, list);
        listView.setAdapter(myAdapter);

    }
}
