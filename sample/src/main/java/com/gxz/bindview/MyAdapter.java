package com.gxz.bindview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.BindView;
import com.gxz.bindview_api.ViewInjector;

import java.util.List;

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/7/21-上午10:06
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MyAdapter extends ArrayAdapter<String> {

    public MyAdapter(Context context, List<String> objects) {
        super(context, -1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(getItem(position));
        return convertView;
    }

    public static final class ViewHolder {

        @BindView(R.id.id_tv)
        TextView textView;

        public ViewHolder(View convertView) {
            ViewInjector.inject(this, convertView);
        }
    }
}
