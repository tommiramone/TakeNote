package com.curso.android.app.practica.takenote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE notas (_id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, cuerpo TEXT)";
        db.execSQL(createTableQuery);

        String createUsuariosTableQuery = "CREATE TABLE usuarios (_id INTEGER PRIMARY KEY, nombre TEXT, email TEXT, contraseña TEXT)";
        db.execSQL(createUsuariosTableQuery);

        String createPapeleraTableQuery = "CREATE TABLE papelera (_id INTEGER PRIMARY KEY AUTOINCREMENT, idNota INTEGER, titulo TEXT, cuerpo TEXT)";
        db.execSQL(createPapeleraTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void reiniciarPapelera() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete("papelera", null, null);
            Log.d("DatabaseHelper", "Papelera reiniciada exitosamente");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public void insertarNota(String titulo, String cuerpo) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("titulo", titulo);
            values.put("cuerpo", cuerpo);

            db.insertOrThrow("notas", null, values);
            Log.d("MyApp", "Nuevo registro insertado en la base de datos");
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

    public List<Nota> obtenerNotas(String tipo) {
        List<Nota> listaDeNotas = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String tableName = (tipo.equals("notas")) ? "notas" : "papelera";
            cursor = db.query(tableName, null, null, null, null, null, null);

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
            Log.d("DatabaseHelper", "Nota actualizada correctamente en la base de datos");
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

        try {
            cursor = db.query(tableName, null, "_id=?", new String[]{String.valueOf(notaId)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                String cuerpo = cursor.getString(cursor.getColumnIndex("cuerpo"));

                return new Nota(id, titulo, cuerpo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;
    }

    public void eliminarNota(int notaId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete("notas", "_id=?", new String[]{String.valueOf(notaId)});
            Log.d("DatabaseHelper", "Nota eliminada exitosamente");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public void enviarNotaAPapelera(int notaId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Obtener la nota de la tabla de notas
            Nota nota = obtenerNota(db, "notas", notaId);

            if (nota != null) {
                ContentValues values = new ContentValues();
                values.put("idNota", notaId);
                values.put("titulo", nota.getTitulo());
                values.put("cuerpo", nota.getCuerpo());

                long result = db.insertOrThrow("papelera", null, values);
                if (result != -1) {
                    Log.d("DatabaseHelper", "Nota insertada en la papelera con éxito");
                } else {
                    Log.e("DatabaseHelper", "Error al insertar la nota en la papelera");
                }
                eliminarNota(notaId); //
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public void restaurarNotaDesdePapelera(int notaId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Obtener la nota de la tabla de papelera
            Nota nota = obtenerNota(db, "papelera", notaId);

            if (nota != null) {
                ContentValues values = nota.toContentValues();

                // Eliminar la nota de la Papelera
                db.delete("papelera", "_id=?", new String[]{String.valueOf(notaId)});

                // Insertar la nota en la tabla de Notas
                long result = db.insertOrThrow("notas", null, values);
                if (result != -1) {
                    Log.d("DatabaseHelper", "Nota restaurada en la tabla de notas con éxito");
                } else {
                    Log.e("DatabaseHelper", "Error al restaurar la nota en la tabla de notas");
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
