package com.example.ibookApp.Adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ibookApp.APIs.UsuarioPorIdApiClient;
import com.example.ibookApp.DTOs.ComentarioDTO;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.DTOs.favoritosDTO;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MyComentarioSearchAdapter extends RecyclerView.Adapter<MyComentarioSearchAdapter.ComentarioViewHolder> {
    private List<ComentarioDTO> comentariosList;
    private List<obrasDTO> obrasNames;
    private List<ComentarioDTO> filteredList; // Lista filtrada (resultados da pesquisa)

    private OnItemClickListener listener;
    private obrasDTO obraDTO;

    public MyComentarioSearchAdapter(List<ComentarioDTO> comentariosList, List<obrasDTO> obrasNames) {
        this.comentariosList = comentariosList;
        this.obrasNames = obrasNames;
        this.filteredList = new ArrayList<>(comentariosList);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_comment_item, parent, false);
        return new ComentarioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        ComentarioDTO comentario = filteredList.get(position);

        holder.bind(comentario);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    obrasDTO obraDTO = obrasNames.get(position);
                    listener.onItemClick(position, obraDTO);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, obrasDTO obraDTO);
    }

    public void filterData(String searchText) {
        filteredList.clear();

        if (searchText.isEmpty()) {
            filteredList.addAll(comentariosList);
        } else {
            for (ComentarioDTO comentario : comentariosList) {
                obrasDTO obra = getObraById(comentario.getObid());
                if (obra != null && obra.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(comentario);
                }
            }
        }

        notifyDataSetChanged();
    }

    private obrasDTO getObraById(String obid) {
        for (obrasDTO obra : obrasNames) {
            if (obid.equals(obra.getId())) {
                return obra;
            }
        }
        return null;
    }

    public class ComentarioViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNomePessoa, txtComentario, txtDataComentario, nomeObra;
        private ImageView personImage;

        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeObra = itemView.findViewById(R.id.nomeObra);
            txtNomePessoa = itemView.findViewById(R.id.nomePessoa);
            txtComentario = itemView.findViewById(R.id.comentarioPessoa);
            txtDataComentario = itemView.findViewById(R.id.dataComentario);
            personImage = itemView.findViewById(R.id.personImage);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(ComentarioDTO comentario) {
            txtComentario.setText(comentario.getCobcomentario());
            LocalDate data = LocalDate.parse(comentario.getDataComentario().toString());
            long diferencaEmDias = ChronoUnit.DAYS.between(data, LocalDate.now());
            if (diferencaEmDias == 0) {
                txtDataComentario.setText("hoje");
            } else if (diferencaEmDias == 1) {
                txtDataComentario.setText("ontem");
            } else if ((diferencaEmDias > 1 && diferencaEmDias < 7) || (diferencaEmDias > 7 && diferencaEmDias < 30)) {
                txtDataComentario.setText("Há " + diferencaEmDias + " dias");
            } else if (diferencaEmDias == 7) {
                txtDataComentario.setText("Há semana");
            } else if (diferencaEmDias == 30) {
                txtDataComentario.setText("Há mês");
            } else {
                txtDataComentario.setText(comentario.getDataComentario().toString());
            }

            obrasDTO obra = getObraById(comentario.getObid());
            if (obra != null) {
                nomeObra.setText(obra.getTitle());
                String imagePath = obra.getImage();
                if (imagePath != null && !imagePath.isEmpty()) {
                    Glide.with(itemView.getContext())
                            .load(imagePath)
                            .into(personImage);
                } else {
                    Glide.with(itemView.getContext())
                            .load(R.drawable.baseline_person_black)
                            .into(personImage);
                }
            } else {
                nomeObra.setText("");
                Glide.with(itemView.getContext())
                        .load(R.drawable.baseline_person_black)
                        .into(personImage);
            }

            UsuarioPorIdApiClient.UsuarioPorIdAsyncTask task = new UsuarioPorIdApiClient.UsuarioPorIdAsyncTask(comentario.getUsuid(), new UsuarioPorIdApiClient.UsuarioPorIdApiListener() {
                @Override
                public void onUsuarioPorIdApiReceived(UsuarioDTO usuario) {
                    if (usuario != null && usuario.getNome() != null) {
                        txtNomePessoa.setText(usuario.getNome() + " - ");
                    } else {
                        txtNomePessoa.setText("");
                    }
                }
            });
            task.execute();
        }
    }
}
