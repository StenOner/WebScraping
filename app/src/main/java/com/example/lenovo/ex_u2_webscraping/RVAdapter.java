package com.example.lenovo.ex_u2_webscraping;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.NoticiaViewHolder> {

    List<Noticias> noticias;
    Context context;

    RVAdapter(List<Noticias> noticias, Context context){
        this.noticias = noticias;
        this.context = context;
    }

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView noticiaTitle, noticiaContent;
        ImageView noticiaPhoto;
        RelativeLayout parentLayout;
        NoticiaViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            noticiaTitle = (TextView)itemView.findViewById(R.id.txt_title);
            noticiaContent = (TextView)itemView.findViewById(R.id.txt_content);
            noticiaPhoto = (ImageView)itemView.findViewById(R.id.iv_photo);
            parentLayout = (RelativeLayout)itemView.findViewById(R.id.parent_layout);
        }
    }
    @Override
    public int getItemCount(){
        return noticias.size();
    }
    @Override
    public NoticiaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item, viewGroup, false);
        NoticiaViewHolder cvh = new NoticiaViewHolder(view);
        return cvh;
    }
    @Override
    public void onBindViewHolder(@NonNull final NoticiaViewHolder contactViewHolder, int i){

        String title = noticias.get(i).titulo.toUpperCase();
        contactViewHolder.noticiaTitle.setText(title);
        contactViewHolder.noticiaContent.setText(noticias.get(i).contenido);

        final Bitmap originalBitmap = noticias.get(i).photo;
        RoundedBitmapDrawable roundedBitmapDrawable =
                RoundedBitmapDrawableFactory.create(context.getResources(), originalBitmap);

        roundedBitmapDrawable.setCornerRadius(originalBitmap.getHeight());

        contactViewHolder.noticiaPhoto.setImageDrawable(roundedBitmapDrawable);
        final int position = contactViewHolder.getAdapterPosition();
        contactViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailNewsActivity.class);
                intent.putExtra("EXTRA_POSITION", position);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
