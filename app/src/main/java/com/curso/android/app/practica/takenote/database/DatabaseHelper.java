package com.curso.android.app.practica.takenote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TakeNote";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper instance;


    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //Creacion de la base de datos.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE notas (_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id TEXT, titulo TEXT, cuerpo TEXT)";
        db.execSQL(createTableQuery);

        String createPapeleraTableQuery = "CREATE TABLE papelera (_id INTEGER PRIMARY KEY AUTOINCREMENT, idNota INTEGER, titulo TEXT, cuerpo TEXT, user_id TEXT)";
        db.execSQL(createPapeleraTableQuery);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //Metodo para insertar notas en la base de datos.
    public void insertarNota(String user_id, String titulo, String cuerpo) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("user_id", user_id);
            values.put("titulo", titulo);
            values.put("cuerpo", cuerpo);

            db.insertOrThrow("notas", null, values);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DatabaseHelper", "Error al insertar la nota: " + e.getMessage());
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    //Definicion de la clase Nota con sus respectivos getters y setters
    public static class Nota {
        private final int id;
        private final String titulo;
        private final String cuerpo;

        //Constructor de la clase Nota
        public Nota(int id, String titulo, String cuerpo) {
            this.id = id;
            this.titulo = titulo;
            this.cuerpo = cuerpo;
        }

        //Metodo utilizado para transformar los valores en ContentValues e insertarlos en la BD
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put("_id", id);
            values.put("titulo", titulo);
            values.put("cuerpo", cuerpo);
            return values;
        }

        public int getId() {
            return id;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getCuerpo() {
            return cuerpo;
        }

    }

    //Metodo para obtener las notas de la base de datos.
    public List<Nota> obtenerNotas(String tipo, String userId) {
        List<Nota> listaDeNotas = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        if (userId == null || userId.isEmpty()) {
            return listaDeNotas;
        }

        try {
            db = this.getReadableDatabase();
            String tableName = (tipo.equals("notas")) ? "notas" : "papelera";
            String selection = "user_id=?";
            String[] selectionArgs = {userId};
            cursor = db.query(tableName, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("_id"));
                    String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                    String cuerpo = cursor.getString(cursor.getColumnIndex("cuerpo"));

                    Nota nota = new Nota(id, titulo, cuerpo);
                    listaDeNotas.add(nota);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return listaDeNotas;
    }


    //Metodo para actualizar las notas en la DB con la funcion de editar nota
    public void actualizarNota(int idNota, String nuevoTitulo, String nuevoCuerpo) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("titulo", nuevoTitulo);
            values.put("cuerpo", nuevoCuerpo);

            db.update("notas", values, "_id=?", new String[]{String.valueOf(idNota)});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    //Metodo para obtener una nota especifica segun un id.
    private Nota obtenerNota(SQLiteDatabase db, String tableName, int notaId) {
        Cursor cursor = null;
        Nota nota = null;

        try {
            cursor = db.query(tableName, null, "_id=? AND user_id=?", new String[]{String.valueOf(notaId), obtenerUsuarioActual()}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                String cuerpo = cursor.getString(cursor.getColumnIndex("cuerpo"));


                nota = new Nota(id, titulo, cuerpo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return nota;
    }

    //Metodo para obtener el usuario que actualmente esta logueado
    public String obtenerUsuarioActual() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }

    //Metodo para eliminar una nota de la base de datos.
    public void eliminarNota(String userId, int notaId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete("notas", "_id=? AND user_id=?", new String[]{String.valueOf(notaId), userId});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    //Metodo para enviar la nota de la tabla Nota a la tabla Papelera.
    public void enviarNotaAPapelera(String userId, int notaId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Nota nota = obtenerNota(db, "notas", notaId);

            if (nota != null) {
                ContentValues values = new ContentValues();
                values.put("user_id", obtenerUsuarioActual());
                values.put("idNota", notaId);
                values.put("titulo", nota.getTitulo());
                values.put("cuerpo", nota.getCuerpo());

                long result = db.insertOrThrow("papelera", null, values);
                if (result != -1) {
                    eliminarNota(userId, notaId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    //Metodo para enviar la nota de la tabla Papelera a la tabla Nota.
    public void restaurarNotaDesdePapelera(Context context, String userId, int notaId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Nota nota = obtenerNota(db, "papelera", notaId);

            if (nota != null) {
                ContentValues values = nota.toContentValues();

                // Establecer el user_id en los valores
                values.put("user_id", userId);

                // Elimina la asignación del ID
                values.remove("_id");

                db.delete("papelera", "_id=? AND user_id=?", new String[]{String.valueOf(notaId), userId});

                // Inserta la nota en la tabla de notas sin especificar el ID
                long result = db.insertOrThrow("notas", null, values);
                if (result != -1) {
                    // La inserción fue exitosa
                    Toast.makeText(context, "Nota restaurada con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    // La inserción falló
                    Toast.makeText(context, "Error al restaurar la nota", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

}







