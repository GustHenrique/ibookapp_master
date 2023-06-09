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
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MyComentarioAdapter extends RecyclerView.Adapter<MyComentarioAdapter.ComentarioViewHolder> {
    private List<ComentarioDTO> comentariosList;
    private List<obrasDTO> obrasNames;

    private OnItemClickListener listener;
    private obrasDTO obraDTO;

    public MyComentarioAdapter(List<ComentarioDTO> comentariosList, List<obrasDTO> obrasNames) {
        this.comentariosList = comentariosList;
        this.obrasNames = obrasNames;
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
        ComentarioDTO comentario = new ComentarioDTO(null, null, null, null, null);
        comentario.setDataComentario(comentariosList.get(position).getDataComentario());
        comentario.setCobcomentario(comentariosList.get(position).getCobcomentario());
        comentario.setCobid(comentariosList.get(position).getCobid());
        comentario.setUsuid(comentariosList.get(position).getUsuid());
        comentario.setObid(comentariosList.get(position).getObid());

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
        return comentariosList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, obrasDTO obraDTO);
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

            for (obrasDTO obra : obrasNames) {
                if (comentario.getObid().equals(obra.getId())) {
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
                }
            }

            UsuarioPorIdApiClient.UsuarioPorIdAsyncTask task = new UsuarioPorIdApiClient.UsuarioPorIdAsyncTask(comentario.getUsuid(), new UsuarioPorIdApiClient.UsuarioPorIdApiListener() {
                @Override
                public void onUsuarioPorIdApiReceived(UsuarioDTO usuario) {
                    if (usuario != null){
                        txtNomePessoa.setText(usuario.getNome() + " - ");
                    }
                }
            });
            task.execute();
        }
    }
}

