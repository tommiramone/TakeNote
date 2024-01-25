package com.curso.android.app.practica.takenote;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.android.app.practica.takenote.adapters.NotaAdapter;
import com.curso.android.app.practica.takenote.database.DatabaseHelper;
import java.util.List;


public class Papelera extends AppCompatActivity {
    private NotaAdapter mNotaAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.papelera);

        // Inicializa dbHelper antes de utilizarlo
        dbHelper = DatabaseHelper.getInstance(this);

        // No es necesario borrar el contenido de la papelera aquí
        // dbHelper.borrarContenidoBaseDatos("papelera");

        // Obtén las notas de la papelera después de inicializar dbHelper
//        mNotaAdapter = new NotaAdapter(dbHelper.obtenerNotas("papelera"), "papelera", this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setAdapter(mNotaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // La referencia a obtenerNotas debería ir después de la inicialización de dbHelper
        mNotaAdapter.setNotas(dbHelper.obtenerNotas("papelera"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Actualiza la lista de notas de la papelera
        List<DatabaseHelper.Nota> notasPapelera = dbHelper.obtenerNotas("papelera");
        mNotaAdapter.setNotas(notasPapelera);
        mNotaAdapter.notifyDataSetChanged();
    }
}
