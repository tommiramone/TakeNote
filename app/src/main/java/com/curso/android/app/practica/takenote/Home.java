package com.curso.android.app.practica.takenote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.android.app.practica.takenote.adapters.NotaAdapter;
import com.curso.android.app.practica.takenote.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Home extends AppCompatActivity implements NotaAdapter.OnItemClickListener {

    private NotaAdapter mNotaAdapter;
    private DatabaseHelper dbHelper;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        dbHelper = DatabaseHelper.getInstance(this);
//        dbHelper.eliminarYRecrearBaseDeDatos();

        String userId = obtenerUsuarioActual();
        List<DatabaseHelper.Nota> notas = dbHelper.obtenerNotas("notas", userId);

        Log.d("Home", "Usuario actual: " + userId);

        List<DatabaseHelper.Nota> notas2 = dbHelper.obtenerNotas("notas", userId);
        for (DatabaseHelper.Nota nota : notas2) {
            Log.d("Home", "ID de la nota: " + nota.getId() + ", Título: " + nota.getTitulo() + ", Cuerpo: " + nota.getCuerpo());
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Log.d("Home", "RecyclerView initialized");


        mNotaAdapter = new NotaAdapter(notas, this, false, userId);
        Log.d("Home", "Adapter created");

        recyclerView.setAdapter(mNotaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNotaAdapter.setNotas(notas);
        Log.d("Home", "Notas set to adapter");
        mNotaAdapter.setOnItemClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        ImageView logOut = findViewById(R.id.iconoCerrarSesion);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Nueva.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
    }

    @Override
    public void onItemClick(String userId, int position) {
    }

    @Override
    public void onEditarClick(String userId, int position) {
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
    public void onEliminarClick(String userId, int position) {
        Log.d("Home", "Eliminar click: Position=" + position);
        mNotaAdapter.enviarNotaAPapelera(obtenerUsuarioActual(),position);
        mNotaAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEnviarAPapeleraClick(String userId, int position) {
        mNotaAdapter.enviarNotaAPapelera(userId, position);
    }

    @Override
    public void onRestaurarClick(String userId, int position) {

    }

    public String obtenerUsuarioActual() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }

    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Home.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
