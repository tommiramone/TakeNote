package com.curso.android.app.practica.takenote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.android.app.practica.takenote.adapters.NotaAdapter;
import com.curso.android.app.practica.takenote.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Home extends AppCompatActivity implements NotaAdapter.OnItemClickListener {

    private NotaAdapter mNotaAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        dbHelper = DatabaseHelper.getInstance(this);

        List<DatabaseHelper.Nota> notas = dbHelper.obtenerNotas("notas");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        mNotaAdapter = new NotaAdapter(notas, this, false);

        recyclerView.setAdapter(mNotaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNotaAdapter.setNotas(notas);
        mNotaAdapter.setOnItemClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Nueva.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
    }

    @Override
    public void onEditarClick(int position) {
        DatabaseHelper.Nota nota = mNotaAdapter.getNota(position);

        if (nota != null) {
            Intent intent = new Intent(Home.this, Editar.class);
            intent.putExtra("ID_NOTA", nota.getId());
            intent.putExtra("TITULO_NOTA", nota.getTitulo());
            intent.putExtra("CUERPO_NOTA", nota.getCuerpo());
            startActivity(intent);
        }
    }

    @Override
    public void onEliminarClick(int position) {
        mNotaAdapter.enviarNotaAPapelera(position);
    }

    @Override
    public void onEnviarAPapeleraClick(int position) {
        mNotaAdapter.enviarNotaAPapelera(position);
    }

    @Override
    public void onRestaurarClick(int position) {

    }


}

