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
    private static final int HIGHLIGHT_CITATION_THRESHOLD = 50;

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
            holder.llRating = convertView.findViewById(R.id.llUserRating);
            holder.tvRating = convertView.findViewById(R.id.tvUserRating);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PaperItem paper = papers.get(position);

        holder.tvTitle.setText(paper.getTitle());
        holder.tvAuthors.setText(paper.getAuthors());
        
        String source = paper.getSource() != null ? paper.getSource() : "Unknown Source";
        holder.tvSourceYear.setText(source + " | " + paper.getYear());

        holder.tvCitations.setText(String.valueOf(paper.getCitationCount()));
        if (paper.getCitationCount() > HIGHLIGHT_CITATION_THRESHOLD) {
            convertView.setBackgroundResource(R.drawable.bg_card_highlighted);
        } else {
            convertView.setBackgroundResource(R.drawable.bg_card_academic);
        }

        if (paper.getUserRating() > 0) {
            holder.llRating.setVisibility(View.VISIBLE);
            holder.tvRating.setText(String.valueOf((int)paper.getUserRating()));
        } else {
            holder.llRating.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvAuthors;
        TextView tvSourceYear;
        TextView tvCitations;
        View llRating;
        TextView tvRating;
    }
}
