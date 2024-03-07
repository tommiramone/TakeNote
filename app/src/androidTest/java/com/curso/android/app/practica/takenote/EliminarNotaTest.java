package com.curso.android.app.practica.takenote;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EliminarNotaTest {

    @Rule
    public ActivityScenarioRule<Home> activityRule =
            new ActivityScenarioRule<>(Home.class);

    @Test
    public void testEliminarNota() {
        // Paso 1: Desplazarse hasta la vista que contiene el botón iconoEliminar
        onView(withId(R.id.iconoEliminar)).perform(scrollTo());

        // Paso 2: Hacer clic en el botón iconoEliminar
        onView(withId(R.id.iconoEliminar)).perform(click());

        // Esperar a que aparezca el mensaje de confirmación
        onView(withText("Nota eliminada")).check(matches(isDisplayed()));

        // Opcional: Verificar que el mensaje desaparece después de un tiempo
        // onView(withText("Nota eliminada")).check(doesNotExist());
    }
}

