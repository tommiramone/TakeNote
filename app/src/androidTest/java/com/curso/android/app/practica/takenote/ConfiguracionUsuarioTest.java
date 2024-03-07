package com.curso.android.app.practica.takenote;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfiguracionUsuarioTest {

    private FirebaseAuth mAuth;

    @Before
    public void setUp() {
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "setUp: Iniciando sesión con el usuario de prueba");
    }

    private void signInWithTestUser(String email, String password) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithTestUser:success");
                    } else {
                        Log.e(TAG, "signInWithTestUser:failure", task.getException());
                        // Aquí puedes manejar el fallo de inicio de sesión según sea necesario
                    }
                    latch.countDown();
                });

        latch.await(10, TimeUnit.SECONDS); // Espera hasta que la tarea se complete o hasta 10 segundos
    }

    @Test
    public void testChangePassword() throws InterruptedException {
        signInWithTestUser("test@example.com", "password123");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assertNotNull(currentUser);

        String newPassword = "nuevacontraseña123";

        // Guardamos el email actual del usuario
        String email = currentUser.getEmail();

        currentUser.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // La contraseña se actualizó correctamente, intentamos iniciar sesión con las nuevas credenciales
                        mAuth.signOut(); // Cerramos sesión para probar el inicio de sesión con la nueva contraseña

                        // Iniciamos sesión con las nuevas credenciales
                        mAuth.signInWithEmailAndPassword(email, newPassword)
                                .addOnCompleteListener(signInTask -> {
                                    if (signInTask.isSuccessful()) {
                                        // Inicio de sesión exitoso, la contraseña se cambió correctamente
                                        assertTrue(true);
                                    } else {
                                        // Fallo al iniciar sesión con la nueva contraseña
                                        fail("Fallo al iniciar sesión con la nueva contraseña: " + signInTask.getException().getMessage());
                                    }
                                });
                    } else {
                        // Fallo al actualizar la contraseña
                        fail("Fallo al cambiar la contraseña: " + task.getException().getMessage());
                    }
                });
    }

    @Test
    public void testUserProfileManagement() throws InterruptedException {
        signInWithTestUser("test@example.com", "password123");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assertNotNull(currentUser);

        currentUser.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // El usuario se eliminó correctamente
                        assertNull(mAuth.getCurrentUser());
                    } else {
                        // Fallo al eliminar el usuario
                        fail("Fallo al borrar el usuario: " + task.getException().getMessage());
                    }
                });
    }
}
