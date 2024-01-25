package com.curso.android.app.practica.takenote.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.android.app.practica.takenote.R;
import com.curso.android.app.practica.takenote.database.DatabaseHelper;

import java.util.List;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {

    private List<DatabaseHelper.Nota> listaDeNotas;
    private Context context;
    private OnItemClickListener mListener;

    public NotaAdapter(List<DatabaseHelper.Nota> listaDeNotas, Context context) {
        this.listaDeNotas = listaDeNotas;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onEditarClick(int position); // Agregar método para editar
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        DatabaseHelper.Nota nota = listaDeNotas.get(position);
        holder.bind(nota);
    }

    @Override
    public int getItemCount() {
        return listaDeNotas.size();
    }

    public class NotaViewHolder extends RecyclerView.ViewHolder {

        private TextView tituloTextView;
        private TextView cuerpoTextView;
        private ImageView editarImageView;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloNota);
            cuerpoTextView = itemView.findViewById(R.id.cuerpoNota);
            editarImageView = itemView.findViewById(R.id.iconoEditar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

            // Configurar clic en el ícono de editar
            editarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onEditarClick(position);
                        }
                    }
                }
            });
        }

        public void bind(DatabaseHelper.Nota nota) {
            tituloTextView.setText(nota.getTitulo());
            cuerpoTextView.setText(nota.getCuerpo());
        }
    }

    public void setNotas(List<DatabaseHelper.Nota> notas) {
        this.listaDeNotas = notas;
        notifyDataSetChanged();
    }

    public DatabaseHelper.Nota getNota(int position) {
        if (listaDeNotas.isEmpty() || position < 0 || position >= listaDeNotas.size()) {
            return null;
        }
        return listaDeNotas.get(position);
    }
}

