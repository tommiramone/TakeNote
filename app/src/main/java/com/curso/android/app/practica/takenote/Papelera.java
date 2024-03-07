package com.curso.android.app.practica.takenote;

import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.android.app.practica.takenote.adapters.NotaAdapter;
import com.curso.android.app.practica.takenote.database.DatabaseHelper;
import com.curso.android.app.practica.takenote.utils.temaUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Papelera extends AppCompatActivity implements NotaAdapter.OnItemClickListener {
    private NotaAdapter mNotaAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.papelera);

        boolean isDarkTheme = loadThemeState(this);
        temaUtils.applyThemeToActivity(this, isDarkTheme);

        dbHelper = DatabaseHelper.getInstance(this);
//        dbHelper.eliminarYRecrearBaseDeDatos();

        List<DatabaseHelper.Nota> notasEnPapelera = obtenerNotasEnPapelera();
        Log.d("PapeleraActivity", "Notas en Papelera: " + notasEnPapelera.size());

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPapelera);

        mNotaAdapter = new NotaAdapter(notasEnPapelera, this, true, userId);

        recyclerView.setAdapter(mNotaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(isDarkTheme == true){
            mNotaAdapter.setTextColor(Color.WHITE);
            mNotaAdapter.setImageColor(Color.WHITE);
        } else {
            mNotaAdapter.setTextColor(Color.BLACK);
            mNotaAdapter.setImageColor(Color.BLACK);
        }

        SearchView searchViewPapelera = findViewById(R.id.searchViewPapelera);

        searchViewPapelera.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    actualizarListaDeNotasPapelera();
                } else {
                    mNotaAdapter.filterPapelera(newText);
                }
                return true;
            }
        });

        ImageView iconoHome = findViewById(R.id.iconoHome);
        iconoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        ImageView iconoConfig = findViewById(R.id.iconoConfig);
        iconoConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Papelera.this, Config.class);
                startActivity(intent);
            }
        });

        ImageView logOut = findViewById(R.id.iconoCerrarSesion);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        mNotaAdapter.setNotas(notasEnPapelera);
        mNotaAdapter.setOnItemClickListener(this);
    }

    String userId = obtenerUsuarioActual();

    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Papelera.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private String obtenerUsuarioActual() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }

    private void actualizarListaDeNotasPapelera() {
        String userId = obtenerUsuarioActual();
        List<DatabaseHelper.Nota> notasPapelera = obtenerNotasEnPapelera();

        mNotaAdapter.setNotas(notasPapelera);
        mNotaAdapter.notifyDataSetChanged();
    }

    private List<DatabaseHelper.Nota> obtenerNotasEnPapelera() {
        return dbHelper.obtenerNotas("papelera", obtenerUsuarioActual());
    }

    public void onBackPressed() {
        super.onBackPressed();
        actualizarListaDeNotasPapelera();
    }

    @Override
    public void onItemClick(String userId, int position) {
        Log.d("PapeleraActivity", "onItemClick called for position: " + position);
    }

    @Override
    public void onEditarClick(String userId, int position ) {
    }

    @Override
    public void onEliminarClick(String userId, int position) {
        // Implementa el comportamiento cuando se hace clic en eliminar en un elemento en la Papelera
        // Por ejemplo, podrías eliminar el elemento de la Papelera y actualizar la lista
    }

    @Override
    public void onEnviarAPapeleraClick(String userId, int position) {
        // Implementa el comportamiento cuando se hace clic en enviar a papelera en un elemento en la Papelera
        // Por ejemplo, podrías mover el elemento de nuevo a la lista principal de notas
    }

    public void onRestaurarClick(String userId, int position) {
        if (mNotaAdapter != null) {
            mNotaAdapter.restaurarNotaDesdePapelera(obtenerUsuarioActual(), position);
            Log.d("PapeleraActivity", "Nota restauradasa desde la papelera en posición: " + position);
        } else {
            Log.d("PapeleraActivity", "Error en metodo");
        }
    }


}
