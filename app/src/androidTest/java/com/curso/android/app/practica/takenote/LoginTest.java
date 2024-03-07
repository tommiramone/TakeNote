package com.curso.android.app.practica.takenote;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;

public class LoginTest {

    private FirebaseAuth mAuth;

    @Before
    public void setUp() {
        // Inicializamos FirebaseAuth para que esté listo para ser utilizado en los tests
        mAuth = FirebaseAuth.getInstance();
    }

    // Definimos un caso de prueba para el inicio de sesión exitoso
    @Test
    public void testLoginSuccess() {
        // Intentamos iniciar sesión con credenciales válidas
        String email = "test@example.com";
        String password = "password123";

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Si el inicio de sesión fue exitoso, el usuario actual no debe ser nulo
                        FirebaseUser user = mAuth.getCurrentUser();
                        assertNotNull(user);
                    } else {
                        // Si el inicio de sesión falla, mostramos el mensaje de error
                        fail("Error al iniciar sesión: " + task.getException().getMessage());
                    }
                });
    }

    @Test
    public void testLoginFailureInvalidCredentials() {
        // Intentamos iniciar sesión con credenciales inválidas
        String email = "usuario_invalido@example.com";
        String password = "contraseña_invalida";

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener(e -> {
                    // Verificamos que la excepción lanzada sea del tipo esperado
                    assertTrue(e instanceof FirebaseAuthInvalidCredentialsException);
                });
    }
}
