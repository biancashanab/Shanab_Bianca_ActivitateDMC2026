package com.example.proiect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.proiect.R;
import com.example.proiect.models.ResearchThread;
import java.util.List;

public class ThreadAdapter extends BaseAdapter {

    private Context context;
    private List<ResearchThread> threads;

    public ThreadAdapter(Context context, List<ResearchThread> threads) {
        this.context = context;
        this.threads = threads;
    }

    @Override
    public int getCount() {
        return threads.size();
    }

    @Override
    public Object getItem(int position) {
        return threads.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_thread, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tvThreadTitle);
            holder.tvQuery = convertView.findViewById(R.id.tvThreadQuery);
            holder.tvMode = convertView.findViewById(R.id.tvThreadMode);
            holder.tvDate = convertView.findViewById(R.id.tvThreadDate);
            holder.tvCount = convertView.findViewById(R.id.tvThreadCount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ResearchThread thread = threads.get(position);

        holder.tvTitle.setText(thread.getTitle());
        holder.tvQuery.setText(thread.getQuery());
        holder.tvMode.setText("Mode: " + thread.getMode());
        holder.tvDate.setText("Updated: " + thread.getUpdatedAt());
        
        int count = thread.getPapers() != null ? thread.getPapers().size() : 0;
        holder.tvCount.setText(count + " papers");

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvQuery;
        TextView tvMode;
        TextView tvDate;
        TextView tvCount;
    }
}
