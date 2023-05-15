package com.example.ibookApp.Adapters;

        import android.net.Uri;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.recyclerview.widget.RecyclerView;

        import com.example.ibookApp.DTOs.ObraDTO;
        import com.example.ibookApp.R;

        import java.util.ArrayList;

        import de.hdodenhof.circleimageview.CircleImageView;

public class ObraMaisComentadasAdapter extends RecyclerView.Adapter<ObraMaisComentadasAdapter.ObraMaisViewHolder> {
    private ArrayList<ObraDTO> obrasList;

    public ObraMaisComentadasAdapter(ArrayList<ObraDTO> obrasList) {
        this.obrasList = obrasList;
    }

    @Override
    public ObraMaisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.maiscomentados_item, parent, false);

        return new ObraMaisViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ObraMaisViewHolder holder, int position) {
        ObraDTO obra = obrasList.get(position);
        String imagePath = obra.getObimage(); // obtém o caminho da imagem como string
        Uri imageUri = Uri.parse("file://" + imagePath); // converte para URI
        holder.txtImage.setImageURI(imageUri);
        holder.txtTitulo.setText(obra.getObtitulo());
    }

    @Override
    public int getItemCount() {
        return obrasList.size();
    }

    public static class ObraMaisViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitulo;
        public CircleImageView txtImage;

        public ObraMaisViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.bookName);
            txtImage = itemView.findViewById(R.id.bookImage);
        }
    }
}


