package com.curso.android.app.practica.takenote;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class AgregarNotaTest {

    private static final String TAG = "AgregarNotaTest";

    @Rule
    public ActivityScenarioRule<Nueva> rule =
            new ActivityScenarioRule<>(Nueva.class);

    @Test
    public void testAgregarNota() {
        Log.d(TAG, "Iniciando prueba de agregar nota...");
        // Ingresar texto en el campo de título
        onView(withId(R.id.editTituloNota)).perform(replaceText("Título de prueba"), closeSoftKeyboard());

        onView(withId(R.id.editTituloNota)).check(matches(withText("Título de prueba")));

        // Ingresar texto en el campo de cuerpo
        onView(withId(R.id.editCuerpoNota)).perform(replaceText("Cuerpo de prueba"), closeSoftKeyboard());

        onView(withId(R.id.editCuerpoNota)).check(matches(withText("Cuerpo de prueba")));

        // Hacer clic en el botón de guardar
        onView(withId(R.id.btnGuardar)).perform(click());

        Log.d(TAG, "La prueba de agregar nota ha finalizado.");
    }
}
