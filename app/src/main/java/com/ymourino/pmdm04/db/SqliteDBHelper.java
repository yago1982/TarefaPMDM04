package com.ymourino.pmdm04.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ymourino.pmdm04.modelos.Pedido;
import com.ymourino.pmdm04.modelos.Usuario;

import java.util.ArrayList;

/**
 * Clase utilizada para trabajar con la base de datos: crear tablas, registros,
 * búsquedas, etc...
 */
public class SqliteDBHelper extends SQLiteOpenHelper {
    // Versión de la base de datos
    private static final int DATABASE_VERSION = 1;

    // Nombre de la base de datos
    private static final String DATABASE_NAME = "tienda.sqlite";


    /**
     * Constructor.
     *
     * @param context
     */
    public SqliteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Este método crea las tablas de la base de datos. Solo es llamado en caso
     * de que la base de datos no exista.
     *
     * @param db Base de datos.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Usuario.CREATE_USUARIOS_TABLE);
        db.execSQL(Pedido.CREATE_PEDIDOS_TABLE);
    }

    /**
     * Si la base de datos existe en el sistema pero es una versión anterior
     * a la definida en esta clase, se llama a este método para actualizarla.
     *
     * @param db Base de datos.
     * @param oldVersion Versión de la base de datos existentes.
     * @param newVersion Nueva versión para la base de datos.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* NOTA IMPORTANTE: eliminar las tablas existentes para volverlas a crear
         *                  NO ES LO CORRECTO. Haciéndolo de esta forma se eliminan
         *                  los datos existentes que ya haya introducido el usuario.
         *                  Se hace así en esta ocasión por simplicidad, pero en una
         *                  aplicación real que se tenga que actualizar en un futuro,
         *                  este método debería ser muy diferente.
         */

        // Se eliminan las tablas existentes
        db.execSQL("DROP TABLE IF EXISTS " + Usuario.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Pedido.TABLE_NAME);

        // Y se vuelven a crear
        onCreate(db);
    }


    /**
     * Insertar nuevo usuario en la base de datos.
     *
     * @param usuario El nuevo usuario.
     * @return El id del nuevo usuario.
     */
    public Long createUser(Usuario usuario) {
        // Instancia de la base de datos en la que se pueda escribir
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Usuario.COLUMN_USERNAME, usuario.getUsername());
        values.put(Usuario.COLUMN_PASSWORD, usuario.getPassword());
        values.put(Usuario.COLUMN_NOMBRE, usuario.getNombre());
        values.put(Usuario.COLUMN_APELLIDOS, usuario.getApellidos());
        values.put(Usuario.COLUMN_ADMIN, usuario.isAdmin());

        // Guardar los datos
        long id = db.insert(Usuario.TABLE_NAME, null, values);

        // Se cierra la conexión con la base de datos
        db.close();

        return id;
    }

    /**
     * Modifica un usuario existente en la base de datos.
     *
     * @param usuario El usuario a modificar.
     * @return True si se ha hecho la modificación.
     */
    public boolean updateUser(Usuario usuario) {
        if (getUserByID(usuario.getId()) != null) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(Usuario.COLUMN_PASSWORD, usuario.getPassword());
            values.put(Usuario.COLUMN_NOMBRE, usuario.getNombre());
            values.put(Usuario.COLUMN_APELLIDOS, usuario.getApellidos());
            values.put(Usuario.COLUMN_ADMIN, usuario.isAdmin());

            db.update(Usuario.TABLE_NAME, values, Usuario.COLUMN_ID + "=" + usuario.getId(), null);
            db.close();

            return true;
        } else {
            return false;
        }
    }

    /**
     * Dado un id de usuario, devuelve dicho usuario si existe.
     *
     * @param id Identificador del usuario solicitado.
     * @return El usuario solicitado si existe, o null si no existe.
     */
    public Usuario getUserByID(long id) {
        // Instancia de la base de datos de la que se pueda leer
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Usuario.TABLE_NAME,
                new String[] {
                        Usuario.COLUMN_ID,
                        Usuario.COLUMN_USERNAME,
                        Usuario.COLUMN_PASSWORD,
                        Usuario.COLUMN_NOMBRE,
                        Usuario.COLUMN_APELLIDOS,
                        Usuario.COLUMN_ADMIN
                },
                Usuario.COLUMN_ID + "=?",
                new String[] {
                        String.valueOf(id)
                },
                null, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                Usuario usuario = new Usuario(
                        Long.parseLong(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_ID))),
                        cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_USERNAME)),
                        cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_APELLIDOS)),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_ADMIN))) == 1);

                cursor.close();
                db.close();
                return usuario;
            } else {
                cursor.close();
                db.close();
                return null;
            }
        } else {
            db.close();
            return null;
        }
    }

    /**
     * Dado un nombre de usuario, devuelve dicho usuario si existe.
     *
     * @param username Nombre de usuario solicitado.
     * @return El usuario solicitado si existe, o null si no existe.
     */
    public Usuario getUserByName(String username) {
        // Instancia de la base de datos de la que se pueda leer
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Usuario.TABLE_NAME,
                new String[] {
                        Usuario.COLUMN_ID,
                        Usuario.COLUMN_USERNAME,
                        Usuario.COLUMN_PASSWORD,
                        Usuario.COLUMN_NOMBRE,
                        Usuario.COLUMN_APELLIDOS,
                        Usuario.COLUMN_ADMIN
                },
                Usuario.COLUMN_USERNAME + "=?",
                new String[] {
                        username
                },
                null, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                Usuario usuario = new Usuario(
                        Long.parseLong(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_ID))),
                        cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_USERNAME)),
                        cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_APELLIDOS)),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_ADMIN))) == 1);

                cursor.close();
                db.close();
                return usuario;
            } else {
                cursor.close();
                db.close();
                return null;
            }
        } else {
            db.close();
            return null;
        }
    }

    /**
     * Dado un pedido, lo introduce en la base de datos.
     *
     * @param pedido Objeto de la clase Pedido completamente inicializado.
     * @return El id del pedido una vez introducido en la base de datos.
     */
    public Long createPedido(Pedido pedido) {
        // Instancia de la base de datos en la que se pueda escribir
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Pedido.COLUMN_USER_ID, pedido.getUser_id());
        values.put(Pedido.COLUMN_CATEGORIA, pedido.getCategoria());
        values.put(Pedido.COLUMN_PRODUCTO, pedido.getProducto());
        values.put(Pedido.COLUMN_CANTIDAD, pedido.getCantidad());
        values.put(Pedido.COLUMN_DIRECCION, pedido.getDireccion());
        values.put(Pedido.COLUMN_LOCALIDAD, pedido.getLocalidad());
        values.put(Pedido.COLUMN_CP, pedido.getCp());
        values.put(Pedido.COLUMN_ESTADO, pedido.getEstado());

        // Guardar los datos
        long id = db.insert(Pedido.TABLE_NAME, null, values);

        // Se cierra la conexión con la base de datos
        db.close();

        return id;
    }

    /**
     * Dado un usuario, devuelve todos los pedidos en trámite que le pertenezcan.
     *
     * @param usuario Objeto de la clase Usuario completamente inicializado.
     * @return Una lista con todos los pedidos en trámite del usuario proporcionado.
     */
    public ArrayList<Pedido> getPedidosTramite(Usuario usuario) {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        // Instancia de la base de datos de la que se puede leer
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Pedido.TABLE_NAME,
                new String[] {
                        Pedido.COLUMN_ID,
                        Pedido.COLUMN_USER_ID,
                        Pedido.COLUMN_CATEGORIA,
                        Pedido.COLUMN_PRODUCTO,
                        Pedido.COLUMN_CANTIDAD,
                        Pedido.COLUMN_DIRECCION,
                        Pedido.COLUMN_LOCALIDAD,
                        Pedido.COLUMN_CP,
                        Pedido.COLUMN_ESTADO
                },
                Pedido.COLUMN_USER_ID + "=? AND " + Pedido.COLUMN_ESTADO + "=?",
                new String[] {
                        String.valueOf(usuario.getId()),
                        String.valueOf(0)
                },
                null, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    Pedido pedido = new Pedido();
                    pedido.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_ID))));
                    pedido.setUser_id(Long.parseLong(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_USER_ID))));
                    pedido.setCategoria(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CATEGORIA)));
                    pedido.setProducto(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_PRODUCTO)));
                    pedido.setCantidad(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CANTIDAD))));
                    pedido.setDireccion(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_DIRECCION)));
                    pedido.setLocalidad(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_LOCALIDAD)));
                    pedido.setCp(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CP))));
                    pedido.setEstado(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_ESTADO))));
                    pedidos.add(pedido);
                } while (cursor.moveToNext());

                return pedidos;
            } else {
                cursor.close();
                db.close();
                return null;
            }
        } else {
            db.close();
            return null;
        }
    }

    /**
     * Dado un usuario, devuelve todos los pedidos aceptados que le pertenezcan.
     *
     * @param usuario Objeto de la clase Usuario completamente inicializado.
     * @return Una lista con todos los pedidos aceptados del usuario proporcionado.
     */
    public ArrayList<Pedido> getPedidosAceptados(Usuario usuario) {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        // Instancia de la base de datos de la que se puede leer
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Pedido.TABLE_NAME,
                new String[] {
                        Pedido.COLUMN_ID,
                        Pedido.COLUMN_USER_ID,
                        Pedido.COLUMN_CATEGORIA,
                        Pedido.COLUMN_PRODUCTO,
                        Pedido.COLUMN_CANTIDAD,
                        Pedido.COLUMN_DIRECCION,
                        Pedido.COLUMN_LOCALIDAD,
                        Pedido.COLUMN_CP,
                        Pedido.COLUMN_ESTADO
                },
                Pedido.COLUMN_USER_ID + "=? AND " + Pedido.COLUMN_ESTADO + "=?",
                new String[] {
                        String.valueOf(usuario.getId()),
                        String.valueOf(1)
                },
                null, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    Pedido pedido = new Pedido();
                    pedido.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_ID))));
                    pedido.setUser_id(Long.parseLong(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_USER_ID))));
                    pedido.setCategoria(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CATEGORIA)));
                    pedido.setProducto(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_PRODUCTO)));
                    pedido.setCantidad(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CANTIDAD))));
                    pedido.setDireccion(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_DIRECCION)));
                    pedido.setLocalidad(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_LOCALIDAD)));
                    pedido.setCp(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CP))));
                    pedido.setEstado(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_ESTADO))));
                    pedidos.add(pedido);
                } while (cursor.moveToNext());

                return pedidos;
            } else {
                cursor.close();
                db.close();
                return null;
            }
        } else {
            db.close();
            return null;
        }
    }

    /**
     * Devuelve todos los pedidos en trámite de la base de datos.
     *
     * @return Una lista con todos los pedidos en trámite.
     */
    public ArrayList<Pedido> getPedidosTramiteAll() {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        // Instancia de la base de datos de la que se puede leer
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Pedido.TABLE_NAME,
                new String[] {
                        Pedido.COLUMN_ID,
                        Pedido.COLUMN_USER_ID,
                        Pedido.COLUMN_CATEGORIA,
                        Pedido.COLUMN_PRODUCTO,
                        Pedido.COLUMN_CANTIDAD,
                        Pedido.COLUMN_DIRECCION,
                        Pedido.COLUMN_LOCALIDAD,
                        Pedido.COLUMN_CP,
                        Pedido.COLUMN_ESTADO
                },
                Pedido.COLUMN_ESTADO + "=?",
                new String[] {
                        String.valueOf(0)
                },
                null, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    Pedido pedido = new Pedido();
                    pedido.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_ID))));
                    pedido.setUser_id(Long.parseLong(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_USER_ID))));
                    pedido.setCategoria(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CATEGORIA)));
                    pedido.setProducto(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_PRODUCTO)));
                    pedido.setCantidad(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CANTIDAD))));
                    pedido.setDireccion(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_DIRECCION)));
                    pedido.setLocalidad(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_LOCALIDAD)));
                    pedido.setCp(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CP))));
                    pedido.setEstado(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_ESTADO))));
                    pedidos.add(pedido);
                } while (cursor.moveToNext());

                return pedidos;
            } else {
                cursor.close();
                db.close();
                return null;
            }
        } else {
            db.close();
            return null;
        }
    }

    /**
     * Devuelve todos los pedidos aceptados de la base de datos.
     *
     * @return Una lista con todos los pedidos aceptados.
     */
    public ArrayList<Pedido> getPedidosAceptadosAll() {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        // Instancia de la base de datos de la que se puede leer
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Pedido.TABLE_NAME,
                new String[] {
                        Pedido.COLUMN_ID,
                        Pedido.COLUMN_USER_ID,
                        Pedido.COLUMN_CATEGORIA,
                        Pedido.COLUMN_PRODUCTO,
                        Pedido.COLUMN_CANTIDAD,
                        Pedido.COLUMN_DIRECCION,
                        Pedido.COLUMN_LOCALIDAD,
                        Pedido.COLUMN_CP,
                        Pedido.COLUMN_ESTADO
                },
                Pedido.COLUMN_ESTADO + "=?",
                new String[] {
                        String.valueOf(1)
                },
                null, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    Pedido pedido = new Pedido();
                    pedido.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_ID))));
                    pedido.setUser_id(Long.parseLong(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_USER_ID))));
                    pedido.setCategoria(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CATEGORIA)));
                    pedido.setProducto(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_PRODUCTO)));
                    pedido.setCantidad(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CANTIDAD))));
                    pedido.setDireccion(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_DIRECCION)));
                    pedido.setLocalidad(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_LOCALIDAD)));
                    pedido.setCp(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CP))));
                    pedido.setEstado(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_ESTADO))));
                    pedidos.add(pedido);
                } while (cursor.moveToNext());

                return pedidos;
            } else {
                cursor.close();
                db.close();
                return null;
            }
        } else {
            db.close();
            return null;
        }
    }

    /**
     * Devuelve todos los pedidos rechazados de la base de datos.
     *
     * @return Una lista con todos los pedidos rechazados.
     */
    public ArrayList<Pedido> getPedidosRechazados() {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        // Instancia de la base de datos de la que se puede leer
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Pedido.TABLE_NAME,
                new String[] {
                        Pedido.COLUMN_ID,
                        Pedido.COLUMN_USER_ID,
                        Pedido.COLUMN_CATEGORIA,
                        Pedido.COLUMN_PRODUCTO,
                        Pedido.COLUMN_CANTIDAD,
                        Pedido.COLUMN_DIRECCION,
                        Pedido.COLUMN_LOCALIDAD,
                        Pedido.COLUMN_CP,
                        Pedido.COLUMN_ESTADO
                },
                Pedido.COLUMN_ESTADO + "=?",
                new String[] {
                        String.valueOf(2)
                },
                null, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    Pedido pedido = new Pedido();
                    pedido.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_ID))));
                    pedido.setUser_id(Long.parseLong(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_USER_ID))));
                    pedido.setCategoria(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CATEGORIA)));
                    pedido.setProducto(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_PRODUCTO)));
                    pedido.setCantidad(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CANTIDAD))));
                    pedido.setDireccion(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_DIRECCION)));
                    pedido.setLocalidad(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_LOCALIDAD)));
                    pedido.setCp(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_CP))));
                    pedido.setEstado(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Pedido.COLUMN_ESTADO))));
                    pedidos.add(pedido);
                } while (cursor.moveToNext());

                return pedidos;
            } else {
                cursor.close();
                db.close();
                return null;
            }
        } else {
            db.close();
            return null;
        }
    }

    /**
     * Dado el identificador de un pedido, cambia su estado al de aceptado.
     * NOTA: este método habría que desarrollarlo más, añadiendo código que compruebe
     * que el pedido exista, pero ahora mismo es suficiente con este código sencillo.
     *
     * @param id Identificador del pedido que se quiere aceptar.
     * @return True.
     */
    public boolean aceptarPedidoByID(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Pedido.COLUMN_ESTADO, 1);

        db.update(Pedido.TABLE_NAME, values, Pedido.COLUMN_ID + "=" + String.valueOf(id), null);
        db.close();

        return true;
    }

    /**
     * Dado el identificador de un pedido, cambia su estado al de rechazado.
     * NOTA: este método habría que desarrollarlo más, añadiendo código que compruebe
     * que el pedido exista, pero ahora mismo es suficiente con este código sencillo.
     *
     * @param id Identificador del pedido que se quiere rechazar.
     * @return True.
     */
    public boolean rechazarPedidoByID(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Pedido.COLUMN_ESTADO, 2);

        db.update(Pedido.TABLE_NAME, values, Pedido.COLUMN_ID + "=" + String.valueOf(id), null);
        db.close();

        return true;
    }
}
