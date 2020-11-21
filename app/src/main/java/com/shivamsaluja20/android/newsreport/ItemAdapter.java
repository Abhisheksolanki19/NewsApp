package com.shivamsaluja20.android.newsreport;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<News> news;


    ItemAdapter(Context context) {
        news = new ArrayList<>();
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.sectionName.setText(news.get(position).getSectionName());
        holder.title.setText(news.get(position).getTitle());
        holder.time.setText(news.get(position).getTime());
        holder.date.setText(news.get(position).getDate());
        holder.author.setText(news.get(position).getAuthor());
        holder.base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webUrl = Uri.parse(news.get(position).getUrls());
                Intent openBrowser = new Intent(Intent.ACTION_VIEW, webUrl);
                context.startActivity(openBrowser);
            }
        });

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    void setContents(ArrayList<News> news) {

        this.news = news;

    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionName, title, time, date, author;
        private CardView base;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.sectionName);
            base = itemView.findViewById(R.id.base);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            author = itemView.findViewById(R.id.author);
        }
    }
}
