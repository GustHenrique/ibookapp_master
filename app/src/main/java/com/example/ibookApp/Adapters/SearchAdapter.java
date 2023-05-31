package com.example.ibookApp.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<obrasDTO> originalItemList; // Lista original
    private List<obrasDTO> filteredItemList; // Lista filtrada (resultados da pesquisa)

    public SearchAdapter(ArrayList<obrasDTO> itemList) {
        this.originalItemList = itemList;
        this.filteredItemList = new ArrayList<>(originalItemList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item do RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Atualizar as visualizações do item do RecyclerView com os dados apropriados
        obrasDTO item = filteredItemList.get(position);
        String imagePath = item.getImage();
        holder.textView.setText(item.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(imagePath)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return filteredItemList.size();
    }

    public void filterData(String searchText) {
        filteredItemList.clear();

        if (searchText.isEmpty()) {
            filteredItemList.addAll(originalItemList);
        } else {
            for (obrasDTO item : originalItemList) {
                if (item.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredItemList.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CircleImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.bookName);
            imageView = itemView.findViewById(R.id.bookImage);
        }
    }
}



