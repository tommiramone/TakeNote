package com.curso.android.app.practica.takenote;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.android.app.practica.takenote.adapters.NotaAdapter;
import com.curso.android.app.practica.takenote.database.DatabaseHelper;

import java.util.List;

public class Papelera extends AppCompatActivity implements NotaAdapter.OnItemClickListener {

    private NotaAdapter mNotaAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.papelera);

        dbHelper = DatabaseHelper.getInstance(this);

        List<DatabaseHelper.Nota> notasEnPapelera = obtenerNotasEnPapelera();
        Log.d("PapeleraActivity", "Notas en Papelera: " + notasEnPapelera.size());

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPapelera);

        mNotaAdapter = new NotaAdapter(notasEnPapelera, this);

        recyclerView.setAdapter(mNotaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNotaAdapter.setNotas(notasEnPapelera);
        mNotaAdapter.setOnItemClickListener(this);
    }

    private List<DatabaseHelper.Nota> obtenerNotasEnPapelera() {
        return dbHelper.obtenerNotas("papelera");
    }

    @Override
    public void onItemClick(int position) {
        Log.d("PapeleraActivity", "onItemClick called for position: " + position);
    }

    @Override
    public void onEditarClick(int position) {
        // Implementa el comportamiento cuando se hace clic en editar en un elemento en la Papelera
        // Por ejemplo, podrías abrir una vista de edición para el elemento seleccionado
    }

    @Override
    public void onEliminarClick(int position) {
        // Implementa el comportamiento cuando se hace clic en eliminar en un elemento en la Papelera
        // Por ejemplo, podrías eliminar el elemento de la Papelera y actualizar la lista
    }

    @Override
    public void onEnviarAPapeleraClick(int position) {
        // Implementa el comportamiento cuando se hace clic en enviar a papelera en un elemento en la Papelera
        // Por ejemplo, podrías mover el elemento de nuevo a la lista principal de notas
    }
}
