package com.curso.android.app.practica.takenote;

import static com.curso.android.app.practica.takenote.utils.temaUtils.loadThemeState;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.android.app.practica.takenote.adapters.NotaAdapter;
import com.curso.android.app.practica.takenote.database.DatabaseHelper;
import com.curso.android.app.practica.takenote.utils.temaUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Home extends AppCompatActivity implements NotaAdapter.OnItemClickListener {

    private NotaAdapter mNotaAdapter;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            dirigirALogin();
        }

        boolean isDarkTheme = loadThemeState(this);
        temaUtils.applyThemeToActivity(this, isDarkTheme);

        dbHelper = DatabaseHelper.getInstance(this);

        String userId = obtenerUsuarioActual();
        List<DatabaseHelper.Nota> notas = dbHelper.obtenerNotas("notas", userId);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        mNotaAdapter = new NotaAdapter(notas, this, false, userId);

        if(isDarkTheme){
            mNotaAdapter.setTextColor(Color.WHITE);
            mNotaAdapter.setImageColor(Color.WHITE);
        } else {
            mNotaAdapter.setTextColor(Color.BLACK);
            mNotaAdapter.setImageColor(Color.BLACK);
        }

        recyclerView.setAdapter(mNotaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNotaAdapter.setNotas(notas);
        mNotaAdapter.setOnItemClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);


        SearchView searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    actualizarListaDeNotas();
                } else {
                    mNotaAdapter.filter(newText);
                }
                return true;
            }
        });

        //Importacion y funcionalidad de imageView de Toolbar.
        ImageView iconoConfig = findViewById(R.id.iconoConfig);
        ImageView iconoTrash = findViewById(R.id.iconoTrash);
        ImageView logOut = findViewById(R.id.iconoCerrarSesion);

        iconoConfig.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Config.class);
            startActivity(intent);
        });

        iconoTrash.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Papelera.class);
            startActivity(intent);
        });

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, Nueva.class);
            startActivity(intent);
        });

        logOut.setOnClickListener(v -> cerrarSesion());
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarListaDeNotas();

        boolean isDarkTheme = loadThemeState(this);
        temaUtils.applyThemeToActivity(this, isDarkTheme);

        if(isDarkTheme){
            mNotaAdapter.setTextColor(Color.WHITE);
            mNotaAdapter.setImageColor(Color.WHITE);
        } else {
            mNotaAdapter.setTextColor(Color.BLACK);
            mNotaAdapter.setImageColor(Color.BLACK);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        actualizarListaDeNotas();
    }

    @Override
    public void onItemClick(String userId, int position) {
    }

    //Funcionalidad de editar nota.
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

    //Funcionalidad de eliminar nota.
    @Override
    public void onEliminarClick(String userId, int position) {
        Log.d("Home", "Eliminar click: Position=" + position);
        mNotaAdapter.enviarNotaAPapelera(obtenerUsuarioActual(),position);
        mNotaAdapter.notifyItemRemoved(position);
    }

    //Metodo para obtener el usuario que esta logueado.
    public String obtenerUsuarioActual() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }

    //Metodo para cerrar sesion.
    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Home.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //Metodo para actualizar la lista de notas actual.
    private void actualizarListaDeNotas() {
        String userId = obtenerUsuarioActual();
        List<DatabaseHelper.Nota> notas = dbHelper.obtenerNotas("notas", userId);

        mNotaAdapter.setNotas(notas);
        mNotaAdapter.notifyDataSetChanged();
    }

    //Funcion para redirigir al login.
    private void dirigirALogin() {
        Intent intent = new Intent(Home.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Finalizar la actividad actual
    }
}
