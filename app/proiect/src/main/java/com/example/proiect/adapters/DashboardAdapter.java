package com.example.proiect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.proiect.R;
import java.util.List;

public class DashboardAdapter extends BaseAdapter {

    public static class DashboardItem {
        public String label;
        public int iconRes;

        public DashboardItem(String label, int iconRes) {
            this.label = label;
            this.iconRes = iconRes;
        }
    }

    private final Context context;
    private final List<DashboardItem> items;

    public DashboardAdapter(Context context, List<DashboardItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false);
        }

        DashboardItem item = items.get(position);

        ImageView ivIcon = view.findViewById(R.id.ivDashIcon);
        TextView tvLabel = view.findViewById(R.id.tvDashLabel);

        ivIcon.setImageResource(item.iconRes);
        tvLabel.setText(item.label);

        return view;
    }
}
