package com.example.applicationtest.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.example.applicationtest.R;
import com.example.applicationtest.model.Articles;

import java.util.List;

public class ArticlesAdaptor extends RecyclerView.Adapter<ArticlesAdaptor.MyViewHolder> {

    private Context context;
    private List<Articles> articles;
    private ArticlesAdaptorListener listener;

    public ArticlesAdaptor(Context context, List<Articles> articles, ArticlesAdaptorListener listener) {
        this.articles = articles;
        this.context = context;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ArticlesAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.articles_list, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ArticlesAdaptor.MyViewHolder holder, int position) {

        Articles articleslist = articles.get(position);

        holder.tv_title.setText("Title: "+articleslist.getTitle());
        holder.tv_published_date.setText("Published Date: "+articleslist.getPublished_date());
        if(articleslist.getImage_url().isEmpty()){
            holder.iv_image.setVisibility(View.GONE);
        }else {
            Glide.with(context).load(articleslist.getImage_url()).centerInside().placeholder(R.drawable.loading_spinner).into(holder.iv_image);

        }


    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


    public interface ArticlesAdaptorListener {

        void onSeleted(Articles articles);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_published_date;
        ImageView iv_image;

        public MyViewHolder( View itemView) {
            super(itemView);

            tv_title=itemView.findViewById(R.id.tv_title);
            tv_published_date=itemView.findViewById(R.id.tv_published_date);
            iv_image=itemView.findViewById(R.id.iv_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onSeleted(articles.get(getAdapterPosition()));
                }
            });

        }
    }
}
