package com.example.ibookApp.Adapters;

import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ObraAdapter extends RecyclerView.Adapter<ObraAdapter.ObraViewHolder> {
    private ArrayList<obrasDTO> obrasList;
    private OnItemClickListener listener; // Adicione esta linha
    private OnFavoriteClickListener favoriteClickListener;

    public ObraAdapter(ArrayList<obrasDTO> obrasList) {
        this.obrasList = obrasList;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.favoriteClickListener = listener;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ObraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_contact_item, parent, false);

        return new ObraViewHolder(itemView);
    }

    public void onBindViewHolder(ObraViewHolder holder, int position) {
        obrasDTO obra = obrasList.get(holder.getAdapterPosition());
        String imagePath = obra.getImage();
        Uri imageUri = Uri.parse(imagePath);
        Glide.with(holder.itemView.getContext())
                .load(imagePath)
                .into(holder.txtImage);

        holder.txtTitulo.setText(obra.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
            }
        });

        /*holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return obrasList.size();
    }

    public static class ObraViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitulo;
        public CircleImageView txtImage;
        public Button imgFavorite;

        public ObraViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.bookName);
            txtImage = itemView.findViewById(R.id.bookImage);
            imgFavorite = itemView.findViewById(R.id.favoritebook);
        }
    }

    // Adicione esta interface
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(int position);
    }

}
