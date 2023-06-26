package com.example.ibookApp.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ibookApp.APIs.AuthApiClient;
import com.example.ibookApp.APIs.DesfavoritarApi;
import com.example.ibookApp.APIs.FavoritarApi;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.DTOs.favoritosDTO;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.FavoritosListSingleton;
import com.example.ibookApp.functions.UserSingleton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<obrasDTO> originalItemList; // Lista original
    private List<obrasDTO> filteredItemList; // Lista filtrada (resultados da pesquisa)
    private ArrayList<favoritosDTO> favoritosList;
    private OnItemClickListener listener;
    public SearchAdapter(ArrayList<obrasDTO> itemList, ArrayList<favoritosDTO> favoritosList) {
        this.originalItemList = itemList;
        this.favoritosList = favoritosList;
        this.filteredItemList = new ArrayList<>(originalItemList);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        obrasDTO item = filteredItemList.get(position);
        if (favoritosList != null && !favoritosList.isEmpty()) {
            for (favoritosDTO objeto : favoritosList) {
                if (objeto.getObid().equals(item.getId())) {
                    holder.imgFavorite.setSelected(true);
                    holder.imgFavorite.setBackgroundResource(R.drawable.ic_save_foreground);
                    break;
                }
                else{
                    holder.imgFavorite.setBackgroundResource(R.drawable.ic_unsave_foreground);
                }
            }
        }
        String imagePath = item.getImage();
        holder.textView.setText(item.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(imagePath)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
        UsuarioDTO userLogado = UserSingleton.getInstance().getUser();
        holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obrasDTO obra = filteredItemList.get(holder.getAdapterPosition());
                boolean salvo = false;
                if (favoritosList != null && !favoritosList.isEmpty()) {
                    for (favoritosDTO objeto : favoritosList) {
                        if (objeto.getObid().equals(obra.getId())) {
                            salvo = true;
                            break;
                        }
                        else{
                            salvo = false;
                        }
                    }
                }

                if (salvo) {
                    DesfavoritarApi.DesavoritarAsyncTask task = new DesfavoritarApi.DesavoritarAsyncTask(userLogado.getId(), obra.getId(), new DesfavoritarApi.DesfavoritarListener() {
                        @Override
                        public void onDeleteFavReceived(boolean success) {
                            if (success) {
                                FavoritosListSingleton.getInstance().removerFav(userLogado.getId(), obra.getId());
                                holder.imgFavorite.setBackgroundResource(R.drawable.ic_unsave_foreground);
                            } else {
                                holder.imgFavorite.setBackgroundResource(R.drawable.ic_save_foreground);
                            }
                        }
                    });
                    task.execute();
                } else {
                    FavoritarApi.FavoritarAsyncTask task = new FavoritarApi.FavoritarAsyncTask(userLogado.getId(), obra.getId(), new FavoritarApi.FavoritarListener() {
                        @Override
                        public void onInsertFavReceived(boolean success) {
                            if (success) {
                                favoritosDTO newFav = new favoritosDTO(null, userLogado.getId(), obra.getId());
                                FavoritosListSingleton.getInstance().adicionarFav(newFav);
                                holder.imgFavorite.setBackgroundResource(R.drawable.ic_save_foreground);
                            } else {
                                holder.imgFavorite.setBackgroundResource(R.drawable.ic_unsave_foreground);
                            }
                        }
                    });
                    task.execute();
                }
            }
        });
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
        public Button imgFavorite;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.bookName);
            imageView = itemView.findViewById(R.id.bookImage);
            imgFavorite = itemView.findViewById(R.id.favoritebook);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}



