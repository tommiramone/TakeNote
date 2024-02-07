package com.curso.android.app.practica.takenote.utils;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.curso.android.app.practica.takenote.R;
import com.curso.android.app.practica.takenote.adapters.NotaAdapter;

public class temaUtils {

    public static void applyTheme(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("config_theme", MODE_PRIVATE);
        String theme = sharedPreferences.getString("theme", "DEFAULT");

        if (theme.equals("DARK")) {
            applyDarkTheme(activity);
        } else {
            applyLightTheme(activity);
        }
    }

    public static void applyThemeToActivity(Activity activity, boolean isDarkTheme) {
        if (isDarkTheme) {
            applyDarkTheme(activity);
        } else {
            applyLightTheme(activity);
        }
    }

    public static void applyDarkTheme(Activity activity) {
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        ImageView iconoConfig = activity.findViewById(R.id.iconoConfig);
        ImageView iconoCerrar = activity.findViewById(R.id.iconoCerrarSesion);
        ImageView iconoTrash = activity.findViewById(R.id.iconoTrash);

        TextView textoOscuro = activity.findViewById(R.id.textoModoOscuro);
        TextView home = activity.findViewById(R.id.textViewHome);
        TextView gestion =  activity.findViewById(R.id.textGestionCuenta);

        EditText userName = activity.findViewById(R.id.editTextUsername);
        EditText password = activity.findViewById(R.id.editTextPassword);

        EditText editTitulo = activity.findViewById(R.id.editTituloNota);
        EditText editCuerpo = activity.findViewById(R.id.editCuerpoNota);


        activity.findViewById(R.id.mainCoordinatorLayout).setBackgroundColor(Color.parseColor("#212121"));

        if (  activity.findViewById(R.id.mainLinearLayout) != null ){
            activity.findViewById(R.id.mainLinearLayout).setBackgroundColor(Color.parseColor("#212121"));
        }

        if (recyclerView != null) {
            NotaAdapter adapter = (NotaAdapter) recyclerView.getAdapter();
            if (adapter != null) {
                adapter.setTextColor(Color.WHITE);
            }
        }

        if (iconoConfig != null && iconoCerrar != null && iconoTrash != null) {
            iconoConfig.setColorFilter(Color.WHITE);
            iconoCerrar.setColorFilter(Color.WHITE);
            iconoTrash.setColorFilter(Color.WHITE);
        }

        if(textoOscuro != null && home != null && gestion!=null ){
            textoOscuro.setTextColor(Color.WHITE);
            home.setTextColor(Color.WHITE);
            gestion.setTextColor(Color.WHITE);
        }


        if (userName != null && password != null) {
            userName.setTextColor(Color.WHITE);
            password.setTextColor(Color.WHITE);
        }

        if (editCuerpo != null && editTitulo != null) {
            editCuerpo.setTextColor(Color.WHITE);
            editTitulo.setTextColor(Color.WHITE);
            editCuerpo.setBackgroundColor(Color.parseColor("#212121"));
            editTitulo.setBackgroundColor(Color.parseColor("#212121"));
            editCuerpo.setHintTextColor(Color.WHITE);
            editTitulo.setHintTextColor(Color.WHITE);
        }

    }

    public static void applyLightTheme(Activity activity) {
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        ImageView iconoConfig = activity.findViewById(R.id.iconoConfig);
        ImageView iconoCerrar = activity.findViewById(R.id.iconoCerrarSesion);
        ImageView iconoTrash = activity.findViewById(R.id.iconoTrash);

        TextView textoOscuro = activity.findViewById(R.id.textoModoOscuro);
        TextView home = activity.findViewById(R.id.textViewHome);
        TextView gestion =  activity.findViewById(R.id.textGestionCuenta);

        EditText userName = activity.findViewById(R.id.editTextUsername);
        EditText password = activity.findViewById(R.id.editTextPassword);

        EditText email = activity.findViewById(R.id.editTextEmail);

        EditText editTitulo = activity.findViewById(R.id.editTituloNota);
        EditText editCuerpo = activity.findViewById(R.id.editCuerpoNota);


        activity.findViewById(R.id.mainCoordinatorLayout).setBackgroundColor(Color.WHITE);

        if (  activity.findViewById(R.id.mainLinearLayout) != null ){
            activity.findViewById(R.id.mainLinearLayout).setBackgroundColor(Color.WHITE);
        }

        if (recyclerView != null) {
            NotaAdapter adapter = (NotaAdapter) recyclerView.getAdapter();
            if (adapter != null) {
                adapter.setTextColor(Color.BLACK);
                Log.d("TemaUtils", "Color del texto establecido correctamente en el adaptador de notas.");
            } else {
                Log.e("TemaUtils", "El adaptador de notas es nulo.");
            }
        } else {
            Log.e("TemaUtils", "El RecyclerView es nulo.");
        }


        if (iconoConfig != null && iconoCerrar != null && iconoTrash != null) {
            iconoConfig.setColorFilter(Color.BLACK);
            iconoCerrar.setColorFilter(Color.BLACK);
            iconoTrash.setColorFilter(Color.BLACK);
        }


        if(textoOscuro != null && home != null && gestion!=null ){
            textoOscuro.setTextColor(Color.BLACK);
            home.setTextColor(Color.BLACK);
            gestion.setTextColor(Color.BLACK);
        }


        if (userName != null && password != null) {
            userName.setTextColor(Color.BLACK);
            password.setTextColor(Color.BLACK);
            userName.setBackgroundColor(Color.parseColor("#ededed"));
            password.setBackgroundColor(Color.parseColor("#ededed"));
            userName.setHintTextColor(Color.BLACK);
            password.setHintTextColor(Color.BLACK);

        }

        if (userName != null && password != null) {
            userName.setTextColor(Color.BLACK);
            password.setTextColor(Color.BLACK);
            userName.setBackgroundColor(Color.parseColor("#ededed"));
            password.setBackgroundColor(Color.parseColor("#ededed"));
            userName.setHintTextColor(Color.BLACK);
            password.setHintTextColor(Color.BLACK);

        }

        if (editCuerpo != null && editTitulo != null) {
            editCuerpo.setTextColor(Color.BLACK);
            editTitulo.setTextColor(Color.BLACK);
            editCuerpo.setBackgroundColor(Color.parseColor("#ededed"));
            editTitulo.setBackgroundColor(Color.parseColor("#ededed"));
            editCuerpo.setHintTextColor(Color.BLACK);
            editTitulo.setHintTextColor(Color.BLACK);

        }

        if (email != null){
            email.setHintTextColor(Color.BLACK);
            email.setTextColor(Color.BLACK);
            email.setBackgroundColor(Color.parseColor("#ededed"));
        }


    }

    public static boolean loadThemeState(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("config_theme", MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_dark_theme", false);
    }

}
