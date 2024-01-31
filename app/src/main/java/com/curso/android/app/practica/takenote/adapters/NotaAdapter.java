package com.curso.android.app.practica.takenote.adapters;

import android.content.Context;
import android.util.Log;
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
    private DatabaseHelper dbHelper;
    private List<DatabaseHelper.Nota> listaDeNotas;
    private Context context;
    private OnItemClickListener mListener;
    private boolean isPapelera;

    public NotaAdapter(List<DatabaseHelper.Nota> listaDeNotas, Context context, boolean isPapelera) {
        this.listaDeNotas = listaDeNotas;
        this.context = context;
        this.isPapelera = isPapelera;
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onEditarClick(int position);
        void onEliminarClick(int position);
        void onEnviarAPapeleraClick(int position);

        void onRestaurarClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño correspondiente según la Papelera o la pantalla principal
        View itemView;
        if (isPapelera) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota_papelera, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        }
        return new NotaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        Log.d("NotaAdapter", "onBindViewHolder called for position: " + position);
        DatabaseHelper.Nota nota = listaDeNotas.get(position);
        holder.bind(nota);
    }

    @Override
    public int getItemCount() {
        return listaDeNotas.size();
    }

    public class NotaViewHolder extends RecyclerView.ViewHolder {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        private TextView tituloTextView;
        private TextView cuerpoTextView;
        private ImageView editarImageView;
        private ImageView eliminarImageView;
        private ImageView restaurarImageView;



        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloNota);
            cuerpoTextView = itemView.findViewById(R.id.cuerpoNota);
            editarImageView = itemView.findViewById(R.id.iconoEditar);
            eliminarImageView = itemView.findViewById(R.id.iconoEliminar);
            restaurarImageView = itemView.findViewById(R.id.iconoRestaurar);

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

            eliminarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onEliminarClick(position);
                        }
                    }
                }
            });

            if (restaurarImageView != null) {
                restaurarImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                mListener.onRestaurarClick(position);
                            }
                        }
                    }
                });
            }

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

    public void eliminarNota(int position) {
        DatabaseHelper.Nota nota = listaDeNotas.get(position);
        int notaId = nota.getId();
        dbHelper.eliminarNota(notaId);
        listaDeNotas.remove(position);
        notifyItemRemoved(position);
    }

    public void restaurarNotaDesdePapelera(int position) {
        DatabaseHelper.Nota nota = listaDeNotas.get(position);
        int notaId = nota.getId();
        dbHelper.restaurarNotaDesdePapelera(notaId); // Llama al método de restaurarNotaDesdePapelera en tu helper
        listaDeNotas.remove(position);
        notifyItemRemoved(position);

        Log.d("NotaAdapter", "Nota restaurada desde la papelera: " + nota.getTitulo());
    }


    public void enviarNotaAPapelera(int position) {
        DatabaseHelper.Nota nota = listaDeNotas.get(position);
        int notaId = nota.getId();
        dbHelper.enviarNotaAPapelera(notaId);
        listaDeNotas.remove(position);
        notifyItemRemoved(position);
    }


}

