package com.curso.android.app.practica.takenote;

import static junit.framework.TestCase.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RegisterTest {

    @Test
    public void testRegistrationSuccess() throws InterruptedException {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email = "test@example.com";
        String password = "password123";

        // Realizar el registro de usuario
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // El registro de usuario fue exitoso
                            assertTrue(task.isSuccessful());
                        } else {
                            // El registro de usuario falló
                            assertTrue(task.getException() == null);
                        }
                    }
                });

        // Esperar un poco para que la tarea de registro se complete
        Thread.sleep(5000); // Esperamos 5 segundos (esto puede variar según la red y el dispositivo)
    }
}
