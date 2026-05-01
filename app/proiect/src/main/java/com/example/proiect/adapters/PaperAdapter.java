package com.example.proiect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.proiect.R;
import com.example.proiect.models.PaperItem;
import java.util.List;

public class PaperAdapter extends BaseAdapter {

    private Context context;
    private List<PaperItem> papers;

    public PaperAdapter(Context context, List<PaperItem> papers) {
        this.context = context;
        this.papers = papers;
    }

    @Override
    public int getCount() {
        return papers.size();
    }

    @Override
    public Object getItem(int position) {
        return papers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_paper, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tvPaperTitle);
            holder.tvAuthors = convertView.findViewById(R.id.tvPaperAuthors);
            holder.tvSourceYear = convertView.findViewById(R.id.tvPaperSourceYear);
            holder.tvCitations = convertView.findViewById(R.id.tvPaperCitations);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PaperItem paper = papers.get(position);

        holder.tvTitle.setText(paper.getTitle());
        holder.tvAuthors.setText(paper.getAuthors());
        holder.tvSourceYear.setText(paper.getSource() + " | " + paper.getYear());
        holder.tvCitations.setText(String.valueOf(paper.getCitationCount()));

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvAuthors;
        TextView tvSourceYear;
        TextView tvCitations;
    }
}
