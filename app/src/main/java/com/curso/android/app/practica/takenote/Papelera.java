package com.curso.android.app.practica.takenote;

import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

        //instancia de la DatabaseHelper.
        dbHelper = DatabaseHelper.getInstance(this);

        //Armado de la lista de notas que contiene la tabla Papelera.
        List<DatabaseHelper.Nota> notasEnPapelera = obtenerNotasEnPapelera();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPapelera);

        //Instancia del NotaAdapter.
        mNotaAdapter = new NotaAdapter(notasEnPapelera, this, true, userId);

        recyclerView.setAdapter(mNotaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Configuracion del tema oscuro.
        if(isDarkTheme){
            mNotaAdapter.setTextColor(Color.WHITE);
            mNotaAdapter.setImageColor(Color.WHITE);
        } else {
            mNotaAdapter.setTextColor(Color.BLACK);
            mNotaAdapter.setImageColor(Color.BLACK);
        }

        //Importacion y funcionalidad del cuadro de busqueda.
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

        //Importación y funcionalidad de imageView de toolbar.
        ImageView iconoHome = findViewById(R.id.iconoHome);
        ImageView iconoConfig = findViewById(R.id.iconoConfig);
        ImageView logOut = findViewById(R.id.iconoCerrarSesion);

        iconoHome.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        });

        iconoConfig.setOnClickListener(v -> {
            Intent intent = new Intent(Papelera.this, Config.class);
            startActivity(intent);
        });

        logOut.setOnClickListener(v -> cerrarSesion());

        mNotaAdapter.setNotas(notasEnPapelera);
        mNotaAdapter.setOnItemClickListener(this);
    }

    String userId = obtenerUsuarioActual();

    //Funcionalidad de cerrar sesión
    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Papelera.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //Metodo para obtener el usuario logueado
    private String obtenerUsuarioActual() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }

    private void actualizarListaDeNotasPapelera() {
        List<DatabaseHelper.Nota> notasPapelera = obtenerNotasEnPapelera();
        mNotaAdapter.setNotas(notasPapelera);
        mNotaAdapter.notifyDataSetChanged();
    }

    private List<DatabaseHelper.Nota> obtenerNotasEnPapelera() {
        return dbHelper.obtenerNotas("papelera", obtenerUsuarioActual());
    }

    //Metodo para actualizar la lista de notas en caso de volver para atras en las pantallas evitando problemas u errores de carga.
    public void onBackPressed() {
        super.onBackPressed();
        actualizarListaDeNotasPapelera();
    }

    //Funcionalidad al restaurar.
    public void onRestaurarClick(String userId, int position) {
        if (mNotaAdapter != null) {
            mNotaAdapter.restaurarNotaDesdePapelera(obtenerUsuarioActual(), position);
        }
    }


}
