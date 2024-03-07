        package com.curso.android.app.practica.takenote;

        import static androidx.test.espresso.Espresso.onView;
        import static androidx.test.espresso.action.ViewActions.click;
        import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
        import static androidx.test.espresso.action.ViewActions.replaceText;
        import static androidx.test.espresso.assertion.ViewAssertions.matches;
        import static androidx.test.espresso.matcher.ViewMatchers.withId;
        import static androidx.test.espresso.matcher.ViewMatchers.withText;


        import androidx.test.ext.junit.rules.ActivityScenarioRule;

        import com.google.firebase.auth.FirebaseAuth;

        import org.junit.Rule;
        import org.junit.Test;

        public class EditarNotaTest {

            @Rule
            public ActivityScenarioRule<Editar> AnotherRule =
                    new ActivityScenarioRule<>(Editar.class);

            @Test
            public void testEditarNota() {


                // Paso 0: Iniciar sesión con Firebase Authentication
                FirebaseAuth.getInstance().signInWithEmailAndPassword("tommiramonee@gmail.com", "ramone1996");

                // Esperar a que se complete el inicio de sesión (puedes utilizar IdlingResource o esperas explícitas)

                // Paso 1: Escribir texto en el campo de título
                onView(withId(R.id.editTituloNota)).perform(replaceText("Nuevo título"), closeSoftKeyboard());

                onView(withId(R.id.editTituloNota)).check(matches(withText("Nuevo título")));

                // Paso 2: Escribir texto en el campo de cuerpo
                onView(withId(R.id.editCuerpoNota)).perform(replaceText("Nuevo cuerpo"), closeSoftKeyboard());

                onView(withId(R.id.editCuerpoNota)).check(matches(withText("Nuevo cuerpo")));

                // Paso 3: Hacer clic en el botón de guardar edición
                onView(withId(R.id.btnGuardar)).perform(click());






            }
        }
