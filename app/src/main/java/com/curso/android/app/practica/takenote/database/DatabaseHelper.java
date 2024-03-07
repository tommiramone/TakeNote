package com.curso.android.app.practica.takenote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TakeNote";
    private static final int DATABASE_VERSION = 1;



    private static DatabaseHelper instance;
    private String userId;

    private SQLiteDatabase database;

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE notas (_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id TEXT, titulo TEXT, cuerpo TEXT)";
        db.execSQL(createTableQuery);

        String createUsuariosTableQuery = "CREATE TABLE usuarios (_id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, email TEXT, contraseña TEXT, token TEXT)";
        db.execSQL(createUsuariosTableQuery);

        String createPapeleraTableQuery = "CREATE TABLE papelera (_id INTEGER PRIMARY KEY AUTOINCREMENT, idNota INTEGER, titulo TEXT, cuerpo TEXT, user_id TEXT)";
        db.execSQL(createPapeleraTableQuery);

//        Log.d("DatabaseHelper", "Tabla 'papelera' creada con éxito");
        Cursor cursor = db.rawQuery("PRAGMA table_info(papelera)", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String columnName = cursor.getString(cursor.getColumnIndex("name"));
                String columnType = cursor.getString(cursor.getColumnIndex("type"));
//                Log.d("DatabaseHelper", "Columna: " + columnName + ", Tipo: " + columnType);
            }
            cursor.close();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

//    public void reiniciarPapelera() {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        try {
//            db.delete("notas", null, null);
//            Log.d("DatabaseHelper", "Papelera reiniciada exitosamente");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (db.isOpen()) {
//                db.close();
//            }
//        }
//    }

    public void insertarNota(String user_id, String titulo, String cuerpo) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("user_id", user_id);
            values.put("titulo", titulo);
            values.put("cuerpo", cuerpo);

            db.insertOrThrow("notas", null, values);
//            Log.d("MyApp", "Nuevo registro insertado en la base de datos");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }


    public class Nota {
        private int id;
        private String titulo;
        private String cuerpo;

        private String user_id;

        public Nota(int id, String titulo, String cuerpo) {
            this.id = id;
            this.titulo = titulo;
            this.cuerpo = cuerpo;
        }

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

    public class Usuario {
        private String userId;
        private String nombre;
        private String email;
        private String contrasenia;

        public Usuario(String nombre, String contrasenia, String email) {
            this.nombre = nombre;
            this.email = email;
            this.contrasenia = contrasenia;
        }

        public String getId() {
            return userId;
        }

        public String setId() {
            return obtenerUsuarioActual();
        }

        public String getNombre() {
            return nombre;
        }

        public String getEmail() {
            return email;
        }

        public String getContrasenia() {
            return contrasenia;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setContrasenia(String contrasenia) {
            this.contrasenia = contrasenia;
        }


    }

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


    public void actualizarNota(int idNota, String nuevoTitulo, String nuevoCuerpo) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("titulo", nuevoTitulo);
            values.put("cuerpo", nuevoCuerpo);

            db.update("notas", values, "_id=?", new String[]{String.valueOf(idNota)});
//            Log.d("DatabaseHelper", "Nota actualizada correctamente en la base de datos");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    private Nota obtenerNota(SQLiteDatabase db, String tableName, int notaId) {
        Cursor cursor = null;
        Nota nota = null;

        try {
            cursor = db.query(tableName, null, "_id=? AND user_id=?", new String[]{String.valueOf(notaId), obtenerUsuarioActual()}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                String cuerpo = cursor.getString(cursor.getColumnIndex("cuerpo"));

//                Log.d("DatabaseHelper", "Nota encontrada - ID: " + id + ", Título: " + titulo + ", Cuerpo: " + cuerpo + "Usuario id:" + obtenerUsuarioActual());

                nota = new Nota(id, titulo, cuerpo);
            } else {
//                Log.d("DatabaseHelper", "No se encontró ninguna nota con ID: " + notaId + " para el usuario con ID: " + obtenerUsuarioActual());
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


    public String obtenerUsuarioActual() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }

    public void eliminarNota(String userId, int notaId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete("notas", "_id=? AND user_id=?", new String[]{String.valueOf(notaId), userId});
//            Log.d("DatabaseHelper", "Nota eliminada exitosamente: ID=" + notaId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

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
//                    Log.d("DatabaseHelper", "Nota insertada en la papelera con éxito y eliminada de la tabla de notas");
                } else {
//                    Log.e("DatabaseHelper", "Error al insertar la nota en la papelera");
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

    public void restaurarNotaDesdePapelera(String userId, int notaId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Obtener información de la nota antes de restaurarla
            Cursor cursor = db.rawQuery("SELECT * FROM papelera WHERE _id = ? AND user_id = ?",
                    new String[]{String.valueOf(notaId), userId});

            if (cursor.moveToFirst()) {
                String tituloAntes = cursor.getString(cursor.getColumnIndex("titulo"));
                String cuerpoAntes = cursor.getString(cursor.getColumnIndex("cuerpo"));
                String userIdAntes = cursor.getString(cursor.getColumnIndex("user_id"));

//                Log.d("DatabaseHelper", "Información de la nota antes de restaurarla:");
//                Log.d("DatabaseHelper", "Título: " + tituloAntes);
//                Log.d("DatabaseHelper", "Cuerpo: " + cuerpoAntes);
//                Log.d("DatabaseHelper", "User ID: " + userIdAntes);

                cursor.close();
            }

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
//                    Log.d("DatabaseHelper", "Nota restaurada en la tabla de notas con éxito. Nuevo ID=" + result);

                    // Obtener información de la nota después de restaurarla
                    Cursor cursorDespues = db.rawQuery("SELECT * FROM notas WHERE _id = ?",
                            new String[]{String.valueOf(result)});

                    if (cursorDespues.moveToFirst()) {
                        String tituloDespues = cursorDespues.getString(cursorDespues.getColumnIndex("titulo"));
                        String cuerpoDespues = cursorDespues.getString(cursorDespues.getColumnIndex("cuerpo"));
                        String userIdDespues = cursorDespues.getString(cursorDespues.getColumnIndex("user_id"));

//                        Log.d("DatabaseHelper", "Información de la nota después de restaurarla:");
//                        Log.d("DatabaseHelper", "Título: " + tituloDespues);
//                        Log.d("DatabaseHelper", "Cuerpo: " + cuerpoDespues);
//                        Log.d("DatabaseHelper", "User ID: " + userIdDespues);

                        cursorDespues.close();
                    }
                } else {
//                    Log.e("DatabaseHelper", "Error al restaurar la nota en la tabla de notas.");
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


//    public void eliminarYRecrearBaseDeDatos() {
//        SQLiteDatabase db = getWritableDatabase();
//        try {
//            // Elimina las tablas existentes
//            db.execSQL("DROP TABLE IF EXISTS notas");
//            db.execSQL("DROP TABLE IF EXISTS usuarios");
//            db.execSQL("DROP TABLE IF EXISTS papelera");
//
//            // Recrea la base de datos
//            onCreate(db);
//
//            Log.d("DatabaseHelper", "Base de datos eliminada y recreada exitosamente");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            db.close();
//        }
//    }






