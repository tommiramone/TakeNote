package com.curso.android.app.practica.takenote.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.android.app.practica.takenote.Detalle;
import com.curso.android.app.practica.takenote.R;
import com.curso.android.app.practica.takenote.database.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {


    private List<DatabaseHelper.Nota> listaDeNotas; // Lista original de todas las notas
    private final List<DatabaseHelper.Nota> mFilteredNotas; // Lista filtrada de notas para mostrar
    private final List<DatabaseHelper.Nota> listaDeNotasOriginales; // Lista original de todas las notas
    private final DatabaseHelper dbHelper;
    private final Context context;
    private OnItemClickListener mListener;
    private final boolean isPapelera;
    private Integer textColor;
    private final String userId;
    private Integer imageColor;

    public NotaAdapter(List<DatabaseHelper.Nota> listaDeNotas, Context context, boolean isPapelera, String userId) {
        this.userId = userId;
        this.listaDeNotas = listaDeNotas;
        this.context = context;
        this.textColor = null;
        this.imageColor = null;
        this.isPapelera = isPapelera;
        dbHelper = new DatabaseHelper(context);

        mFilteredNotas = new ArrayList<>(listaDeNotas);

        listaDeNotasOriginales = new ArrayList<>(listaDeNotas);
    }


    //Setea el color que tomaran las letras
    public void setTextColor(int color) {
        this.textColor = color;
        notifyDataSetChanged();
    }

    //Setea el color que tomaran los imageView
    public void setImageColor(int color){
        this.imageColor = color;
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        default void onItemClick(String userId, int position) {}
        default void onEditarClick(String userId, int position) {}
        default void onEliminarClick(String userId, int position) {}
        default void onRestaurarClick(String userId, int position) {}
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
            holder.bind(nota, textColor);
        } else {
            holder.bind(nota, Color.BLACK); //
        }

        //setOnClick que permite abrir la vista en detalle de cada nota
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                int position1 = holder.getAdapterPosition();
                if (position1 != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(userId, position1);

                    // Obtener la nota seleccionada
                    DatabaseHelper.Nota notaSeleccionada = listaDeNotas.get(position1);

                    // Crear un Intent para iniciar la actividad Detalle
                    Intent intent = new Intent(context, Detalle.class);

                    // Pasar el título y el cuerpo de la nota como extras en el Intent
                    intent.putExtra("titulo", notaSeleccionada.getTitulo());
                    intent.putExtra("cuerpo", notaSeleccionada.getCuerpo());

                    // Iniciar la actividad Detalle
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("NotaAdapter", "getItemCount: " + listaDeNotas.size() + " notas en la lista");
        return listaDeNotas.size();
    }

    public class NotaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tituloTextView;
        private final TextView cuerpoTextView;
        private final ImageView editarImageView;
        private final ImageView eliminarImageView;
        private final ImageView restaurarImageView;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloNota);
            cuerpoTextView = itemView.findViewById(R.id.cuerpoNota);
            editarImageView = itemView.findViewById(R.id.iconoEditar);
            eliminarImageView = itemView.findViewById(R.id.iconoEliminar);
            restaurarImageView = itemView.findViewById(R.id.iconoRestaurar);
            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(userId, position);
                    }
                }
            });

            //Funcionalidad al hacer click en el imageView de editar
           if(editarImageView!=null){
               editarImageView.setOnClickListener(v -> {
                   if (mListener != null) {
                       int position = getAdapterPosition();
                       if (position != RecyclerView.NO_POSITION) {
                           mListener.onEditarClick(userId, position);
                       }
                   }
               });
           }

            //Funcionalidad al hacer click en el imageView de eliminar
            if(eliminarImageView != null){
                eliminarImageView.setOnClickListener(v -> {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onEliminarClick(userId, position);
                        }
                    }
                });
            }

            //Funcionalidad al hacer click en el imageView de restaurar en Papelera, no sin antes corroborar que existe.
            if (restaurarImageView != null) {
                restaurarImageView.setOnClickListener(v -> {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onRestaurarClick(userId, position);
                        }
                    }
                });
            }
        }

        //Bind es el encargado de asignar valores de una nota especifica a los elementos de la interfaz de usuario dentro del ViewHolder
        public void bind(DatabaseHelper.Nota nota, int textColor) {
            tituloTextView.setText(nota.getTitulo());
            String cuerpo = nota.getCuerpo();

            if (cuerpo.length() > 45){
                cuerpo = cuerpo.substring(0,45) + "...";
            }

            cuerpoTextView.setText(cuerpo);

            tituloTextView.setTextColor(textColor);
            cuerpoTextView.setTextColor(textColor);

            if (editarImageView != null && eliminarImageView != null) {
                if (imageColor != null) {
                    eliminarImageView.setColorFilter(imageColor);
                    editarImageView.setColorFilter(imageColor);
                }
            }

            if (restaurarImageView != null) { // Verifica si restaurarImageView no es nulo
                if (imageColor != null) {
                    restaurarImageView.setColorFilter(imageColor);
                }
            }
        }
    }

    //Funcion para actualizar las notas que se muestran.
    public void setNotas(List<DatabaseHelper.Nota> notas) {
        this.listaDeNotas = notas;
        notifyDataSetChanged();
    }

    //Funcion para lograr obtener el usuario que esta logueado.
    public String obtenerUsuarioActual() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }

    //Funcion utilizada para obtener una nota especifica de la lista de notas.
    public DatabaseHelper.Nota getNota(int position) {
        if (listaDeNotas.isEmpty() || position < 0 || position >= listaDeNotas.size()) {
            return null;
        }
        return listaDeNotas.get(position);
    }

    //Funcion para traer una nota de la tabla Nota a la tabla Papelera.
    public void enviarNotaAPapelera(String userId, int position) {
        DatabaseHelper.Nota nota = listaDeNotas.get(position);
        int notaId = nota.getId();
        Log.d("NotaAdapter", "Nota eliminada: ID=" + notaId + ", Título=" + nota.getTitulo());
        dbHelper.enviarNotaAPapelera(userId, notaId);
        listaDeNotas.remove(position);
        notifyItemRemoved(position);
    }

    //Funcion para traer una nota de la tabla Papelera a la tabla Notas.
    public void restaurarNotaDesdePapelera(String userId, int position) {
        DatabaseHelper.Nota nota = listaDeNotas.get(position);
        int notaId = nota.getId();
        Log.d("NotaAdapter", "Restaurando nota desde la papelera: ID=" + notaId + ", Título=" + nota.getTitulo());
        dbHelper.restaurarNotaDesdePapelera(context, obtenerUsuarioActual(), notaId);
        listaDeNotas.remove(position);
        notifyItemRemoved(position);
    }

    //Funcion de filtrado de las listas de notas.
    public void filter(String searchText) {
        searchText = searchText.toLowerCase(Locale.getDefault());
        mFilteredNotas.clear();

        if (searchText.isEmpty()) {
            mFilteredNotas.addAll(listaDeNotasOriginales);
        } else {
            for (DatabaseHelper.Nota nota : listaDeNotasOriginales) {
                if (nota.getTitulo().toLowerCase(Locale.getDefault()).contains(searchText)) {
                    mFilteredNotas.add(nota);
                }
            }
        }

        listaDeNotas.clear();
        listaDeNotas.addAll(mFilteredNotas);
        notifyDataSetChanged();
    }

    //Funcion de filtrado de notas en la Papelera.
    public void filterPapelera(String searchText) {
        searchText = searchText.toLowerCase(Locale.getDefault());
        mFilteredNotas.clear();

        if (searchText.isEmpty()) {
            mFilteredNotas.addAll(listaDeNotasOriginales);
        } else {
            for (DatabaseHelper.Nota nota : listaDeNotasOriginales) {
                if (nota.getTitulo().toLowerCase(Locale.getDefault()).contains(searchText)) {
                    mFilteredNotas.add(nota);
                }
            }
        }

        listaDeNotas.clear();
        listaDeNotas.addAll(mFilteredNotas);
        notifyDataSetChanged();
    }

}
