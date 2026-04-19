package com.example.lab8;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ImageItemAdapter extends BaseAdapter {

    private final Context context;
    private final List<ImageItem> items;
    private final LayoutInflater inflater;

    public ImageItemAdapter(Context context, List<ImageItem> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
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

    public Context getContext() {
        return context;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView tvTitle;
        TextView tvDescription;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_image_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.ivRowImage);
            holder.tvTitle = convertView.findViewById(R.id.tvRowTitle);
            holder.tvDescription = convertView.findViewById(R.id.tvRowDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageItem item = items.get(position);

        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());

        if (item.getBitmap() != null) {
            holder.imageView.setImageBitmap(item.getBitmap());
        } else {
            holder.imageView.setImageResource(android.R.drawable.ic_menu_report_image);
        }

        return convertView;
    }
}