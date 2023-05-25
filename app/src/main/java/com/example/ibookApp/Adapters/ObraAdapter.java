package com.example.ibookApp.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ibookApp.DTOs.ObraDTO;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ObraAdapter extends RecyclerView.Adapter<ObraAdapter.ObraViewHolder> {
    private ArrayList<obrasDTO> obrasList;

    public ObraAdapter(ArrayList<obrasDTO> obrasList) {
        this.obrasList = obrasList;
    }

    @Override
    public ObraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_contact_item, parent, false);

        return new ObraViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ObraViewHolder holder, int position) {
        obrasDTO obra = obrasList.get(position);
        String imagePath = obra.getImage(); // obtém o caminho da imagem como string
        Uri imageUri = Uri.parse(imagePath); // converte para URI
        //holder.txtImage.setImageURI(imageUri);
        Glide.with(holder.itemView.getContext())
                .load(imagePath)
                .into(holder.txtImage);

        holder.txtTitulo.setText(obra.getTitle());
    }

    @Override
    public int getItemCount() {
        return obrasList.size();
    }

    public static class ObraViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitulo;
        public CircleImageView txtImage;

        public ObraViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.bookName);
            txtImage = itemView.findViewById(R.id.bookImage);
        }
    }
}


