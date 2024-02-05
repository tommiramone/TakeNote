package com.curso.android.app.practica.takenote.adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {


    private List<DatabaseHelper.Nota> listaDeNotas;
    private DatabaseHelper dbHelper;
    private Context context;
    private OnItemClickListener mListener;
    private boolean isPapelera;
    private Integer textColor;

    private String userId;
    private Integer imageColor;

    public NotaAdapter(List<DatabaseHelper.Nota> listaDeNotas, Context context, boolean isPapelera, String userId) {
        this.userId = userId;
        this.listaDeNotas = listaDeNotas;
        this.context = context;
        this.textColor = null;
        this.imageColor = null;
        this.isPapelera = isPapelera;
        dbHelper = new DatabaseHelper(context);
    }


    public void setTextColor(int color) {
        this.textColor = color;
        notifyDataSetChanged();
    }

    public void setImageColor(int color){
        this.imageColor = color;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(String userId, int position);
        void onEditarClick(String userId, int position);
        void onEliminarClick(String userId, int position);
        void onEnviarAPapeleraClick(String userId, int position);
        void onRestaurarClick(String userId, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (isPapelera) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota_papelera, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        }
        Log.d("NotaAdapter", "onCreateViewHolder: ViewHolder creado");
        return new NotaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        Log.d("NotaAdapter", "onBindViewHolder: Llamado para la posición " + position);
        Log.d("NotaAdapter", "onBindViewHolder called for position: " + position);
        DatabaseHelper.Nota nota = listaDeNotas.get(position);

        if (textColor != null) {
            holder.bind(nota, textColor.intValue());
        } else {
            holder.bind(nota, Color.BLACK); //
        }


    }

    @Override
    public int getItemCount() {
        Log.d("NotaAdapter", "getItemCount: " + listaDeNotas.size() + " notas en la lista");
        return listaDeNotas.size();
    }

    public class NotaViewHolder extends RecyclerView.ViewHolder {
        private TextView tituloTextView;
        private TextView cuerpoTextView;
        private ImageView editarImageView;
        private ImageView eliminarImageView;
        private ImageView restaurarImageView;

        private String userId; // Agregar userId como campo en el adapter

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
                            mListener.onItemClick(userId, position);
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
                            mListener.onEditarClick(userId, position);
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
                            mListener.onEliminarClick(userId, position);
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
                                mListener.onRestaurarClick(userId, position);
                            }
                        }
                    }
                });
            }
        }

        public void bind(DatabaseHelper.Nota nota, int textColor) {
            tituloTextView.setText(nota.getTitulo());
            cuerpoTextView.setText(nota.getCuerpo());

            tituloTextView.setTextColor(textColor);
            cuerpoTextView.setTextColor(textColor);

            if (imageColor != null) {
                eliminarImageView.setColorFilter(imageColor.intValue());
                editarImageView.setColorFilter(imageColor.intValue());
            }

            if (restaurarImageView != null && imageColor != null) {
                restaurarImageView.setColorFilter(imageColor.intValue());
            }
        }


    }



    public void setNotas(List<DatabaseHelper.Nota> notas) {
        this.listaDeNotas = notas;
        notifyDataSetChanged();
    }

    public String obtenerUsuarioActual() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }

    public DatabaseHelper.Nota getNota(int position) {
        if (listaDeNotas.isEmpty() || position < 0 || position >= listaDeNotas.size()) {
            return null;
        }
        return listaDeNotas.get(position);
    }

    public void enviarNotaAPapelera(String userId, int position) {
        DatabaseHelper.Nota nota = listaDeNotas.get(position);
        int notaId = nota.getId();
        Log.d("NotaAdapter", "Nota eliminada: ID=" + notaId + ", Título=" + nota.getTitulo());
        dbHelper.enviarNotaAPapelera(userId, notaId);
        listaDeNotas.remove(position);
        notifyItemRemoved(position);
    }

    public void restaurarNotaDesdePapelera(String userId, int position) {
        DatabaseHelper.Nota nota = listaDeNotas.get(position);
        int notaId = nota.getId();
        Log.d("NotaAdapter", "Restaurando nota desde la papelera: ID=" + notaId + ", Título=" + nota.getTitulo());
        dbHelper.restaurarNotaDesdePapelera(obtenerUsuarioActual(), notaId);
        listaDeNotas.remove(position);
        notifyItemRemoved(position);
    }




}
