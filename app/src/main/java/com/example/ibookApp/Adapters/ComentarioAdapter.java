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
import com.example.ibookApp.R;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder> {
    private List<ComentarioDTO> comentariosList;

    public ComentarioAdapter(List<ComentarioDTO> comentariosList) {
        this.comentariosList = comentariosList;
    }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_comment_item, parent, false);
        return new ComentarioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        ComentarioDTO comentario = new ComentarioDTO(null,null,null,null,null);
        comentario.setDataComentario(comentariosList.get(position).getDataComentario());
        comentario.setCobcomentario(comentariosList.get(position).getCobcomentario());
        comentario.setCobid(comentariosList.get(position).getCobid());
        comentario.setUsuid(comentariosList.get(position).getUsuid());
        comentario.setObid(comentariosList.get(position).getObid());
        try {
            holder.bind(comentario);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return comentariosList.size();
    }

    public class ComentarioViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNomePessoa, txtComentario, txtDataComentario;
        private ImageView personImage;

        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNomePessoa = itemView.findViewById(R.id.nomePessoa);
            txtComentario = itemView.findViewById(R.id.comentarioPessoa);
            txtDataComentario = itemView.findViewById(R.id.dataComentario);
            personImage = itemView.findViewById(R.id.personImage);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(ComentarioDTO comentario) throws IOException {
            txtComentario.setText(comentario.getCobcomentario());
            LocalDate data = LocalDate.parse(comentario.getDataComentario().toString());
            long diferencaEmDias = ChronoUnit.DAYS.between(data, LocalDate.now());
            if (diferencaEmDias == 0){
                txtDataComentario.setText("hoje");
            }else if (diferencaEmDias == 1){
                txtDataComentario.setText("ontem");
            }else if ((diferencaEmDias > 1 && diferencaEmDias < 7) || (diferencaEmDias > 7 && diferencaEmDias < 30)){
                txtDataComentario.setText("Fazem " + diferencaEmDias + " dias");
            }else if (diferencaEmDias == 7){
                txtDataComentario.setText("Faz uma semana");
            }else if (diferencaEmDias == 30){
                txtDataComentario.setText("Faz um mês");
            }
            else{
                txtDataComentario.setText(comentario.getDataComentario().toString());
            }

            UsuarioPorIdApiClient.UsuarioPorIdAsyncTask task = new UsuarioPorIdApiClient.UsuarioPorIdAsyncTask(comentario.getUsuid(), new UsuarioPorIdApiClient.UsuarioPorIdApiListener() {

                @Override
                public void onUsuarioPorIdApiReceived(UsuarioDTO usuario) {
                    txtNomePessoa.setText(usuario.getNome() + " - ");
                    String imagePath = usuario.getImagem();
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
            });
            task.execute();
        }
    }
}

