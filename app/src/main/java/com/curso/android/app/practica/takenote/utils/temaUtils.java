package com.curso.android.app.practica.takenote.utils;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.curso.android.app.practica.takenote.R;
import com.curso.android.app.practica.takenote.adapters.NotaAdapter;

public class temaUtils {

//    public static void applyTheme(Activity activity) {
//        SharedPreferences sharedPreferences = activity.getSharedPreferences("config_theme", MODE_PRIVATE);
//        String theme = sharedPreferences.getString("theme", "DEFAULT");
//
//        if (theme.equals("DARK")) {
//            applyDarkTheme(activity);
//        } else {
//            applyLightTheme(activity);
//        }
//    }

    public static void applyThemeToActivity(Activity activity, boolean isDarkTheme) {
        if (isDarkTheme) {
            applyDarkTheme(activity);
        } else {
            applyLightTheme(activity);
        }
    }

    public static void applyDarkTheme(Activity activity) {
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);

        TextView textoOscuro = activity.findViewById(R.id.textoModoOscuro);
        TextView gestion =  activity.findViewById(R.id.textGestionCuenta);

        EditText userName = activity.findViewById(R.id.editTextUsername);
        EditText password = activity.findViewById(R.id.editTextPassword);

        TextView passwordForgot = activity.findViewById(R.id.textViewPasswordForgot);
        TextView register = activity.findViewById(R.id.textViewRegister);

        EditText editTitulo = activity.findViewById(R.id.editTituloNota);
        EditText editCuerpo = activity.findViewById(R.id.editCuerpoNota);

        TextView textoEmail = activity.findViewById(R.id.textViewEmail);
        EditText editTextNewEmail = activity.findViewById(R.id.editTextNewEmail);
        TextView contrasena = activity.findViewById(R.id.textViewContrasena);
        EditText editTextcontrasenaActual = activity.findViewById(R.id.editTextCurrentPassword);
        EditText editTextcontrasenaNueva = activity.findViewById(R.id.editTextNewPassword);

        TextView email = activity.findViewById(R.id.textViewCambiarEmail);
        TextView contra = activity.findViewById(R.id.textViewCambiarContrasena);

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

        if(textoOscuro != null && gestion!=null ){
            textoOscuro.setTextColor(Color.WHITE);
            gestion.setTextColor(Color.WHITE);
        }

        if (userName != null && password != null) {
            userName.setTextColor(Color.WHITE);
            password.setTextColor(Color.WHITE);
            userName.setBackgroundColor(Color.parseColor("#212121"));
            password.setBackgroundColor(Color.parseColor("#212121"));
            userName.setHintTextColor(Color.WHITE);
            password.setHintTextColor(Color.WHITE);

        }

        if(passwordForgot != null && register != null){
            passwordForgot.setTextColor(Color.WHITE);
            register.setTextColor(Color.WHITE);
        }

        if (editCuerpo != null && editTitulo != null) {
            editCuerpo.setTextColor(Color.WHITE);
            editTitulo.setTextColor(Color.WHITE);
            editCuerpo.setBackgroundColor(Color.parseColor("#212121"));
            editTitulo.setBackgroundColor(Color.parseColor("#212121"));
            editCuerpo.setHintTextColor(Color.WHITE);
            editTitulo.setHintTextColor(Color.WHITE);
        }

        if (textoEmail != null && editTextNewEmail != null){
            textoEmail.setTextColor(Color.WHITE);
            editTextNewEmail.setTextColor(Color.WHITE);
            editTextNewEmail.setBackgroundColor(Color.parseColor("#212121"));
        }

        if (contrasena != null && editTextcontrasenaNueva != null && editTextcontrasenaActual != null){
            contrasena.setTextColor(Color.WHITE);
            editTextcontrasenaNueva.setTextColor(Color.WHITE);
            editTextcontrasenaActual.setTextColor(Color.WHITE);
            editTextcontrasenaNueva.setBackgroundColor(Color.parseColor("#212121"));
            editTextcontrasenaActual.setBackgroundColor(Color.parseColor("#212121"));
        }

        if (email!= null && contra != null){
            email.setTextColor(Color.WHITE);
            contra.setTextColor(Color.WHITE);
        }

    }

    public static void applyLightTheme(Activity activity) {
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);

        TextView textoOscuro = activity.findViewById(R.id.textoModoOscuro);
        TextView gestion =  activity.findViewById(R.id.textGestionCuenta);

        EditText userName = activity.findViewById(R.id.editTextUsername);
        EditText password = activity.findViewById(R.id.editTextPassword);

        EditText email = activity.findViewById(R.id.editTextEmail);

        EditText editTitulo = activity.findViewById(R.id.editTituloNota);
        EditText editCuerpo = activity.findViewById(R.id.editCuerpoNota);

        TextView detalleTitulo = activity.findViewById(R.id.tituloNota);
        TextView detalleCuerpo = activity.findViewById(R.id.cuerpoNota);

        TextView passwordForgot = activity.findViewById(R.id.textViewPasswordForgot);
        TextView register = activity.findViewById(R.id.textViewRegister);

        TextView textoEmail = activity.findViewById(R.id.textViewEmail);
        EditText editTextNewEmail = activity.findViewById(R.id.editTextNewEmail);
        TextView contrasena = activity.findViewById(R.id.textViewContrasena);
        EditText editTextcontrasenaActual = activity.findViewById(R.id.editTextCurrentPassword);
        EditText editTextcontrasenaNueva = activity.findViewById(R.id.editTextNewPassword);

        TextView contrasenaRegi = activity.findViewById(R.id.editTextPasswordRegister);

        TextView emailCambiar = activity.findViewById(R.id.textViewCambiarEmail);
        TextView contra = activity.findViewById(R.id.textViewCambiarContrasena);




        activity.findViewById(R.id.mainCoordinatorLayout).setBackgroundColor(Color.WHITE);

        if (  activity.findViewById(R.id.mainLinearLayout) != null ){
            activity.findViewById(R.id.mainLinearLayout).setBackgroundColor(Color.WHITE);
        }

        if (recyclerView != null) {
            NotaAdapter adapter = (NotaAdapter) recyclerView.getAdapter();
            if (adapter != null) {
                adapter.setTextColor(Color.BLACK);
            }
        }

        if(textoOscuro != null && gestion!=null ){
            textoOscuro.setTextColor(Color.BLACK);
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

        if(passwordForgot != null && register != null){
            passwordForgot.setTextColor(Color.BLACK);
            register.setTextColor(Color.BLACK);
        }

        if(contrasenaRegi != null){
            contrasenaRegi.setTextColor(Color.BLACK);
            contrasenaRegi.setHintTextColor(Color.BLACK);
            contrasenaRegi.setBackgroundColor(Color.parseColor("#ededed"));
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

        if(detalleCuerpo != null && detalleTitulo != null) {
            detalleTitulo.setTextColor(Color.BLACK);
            detalleCuerpo.setTextColor(Color.BLACK);
        }

        if (textoEmail != null && editTextNewEmail != null){
            textoEmail.setTextColor(Color.BLACK);
            editTextNewEmail.setHintTextColor(Color.BLACK);
            editTextNewEmail.setBackgroundColor(Color.parseColor("#ededed"));
        }

        if (contrasena != null && editTextcontrasenaNueva != null && editTextcontrasenaActual != null){
            contrasena.setTextColor(Color.BLACK);
            editTextcontrasenaNueva.setHintTextColor(Color.BLACK);
            editTextcontrasenaActual.setHintTextColor(Color.BLACK);
            editTextcontrasenaNueva.setBackgroundColor(Color.parseColor("#ededed"));
            editTextcontrasenaActual.setBackgroundColor(Color.parseColor("#ededed"));

        }

        if (emailCambiar!= null && contra != null){
            emailCambiar.setTextColor(Color.BLACK);
            contra.setTextColor(Color.BLACK);
        }
    }

    public static boolean loadThemeState(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("config_theme", MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_dark_theme", false);
    }

}
