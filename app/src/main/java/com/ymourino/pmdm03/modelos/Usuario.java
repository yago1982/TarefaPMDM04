package com.ymourino.pmdm03.modelos;

import java.io.Serializable;

/**
 * Clase utilizada para almacenar los datos de un usuario, ya sea para almacenarlos
 * luego en la base de datos o cuando se recuperan de ella.
 */
public class Usuario implements Serializable {

    /*
     * Datos utilizados para la creación de la tabla de los usuarios: nombre de la tabla,
     * nombre del campo clave, etc...
     */
    public static final String TABLE_NAME = "usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_APELLIDOS = "apellidos";
    public static final String COLUMN_ADMIN = "admin";

    /*
     * Se construye la sentencia SQL que creará la tabla usando los datos antes definidos.
     * No es necesario añadir "IF NOT EXISTS" porque la creación de la tabla solo
     * se intentará si no existe, ya que así es como está diseñado Android.
     */
    public static final String CREATE_USUARIOS_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USERNAME + " TEXT NOT NULL UNIQUE,"
                    + COLUMN_PASSWORD + " TEXT NOT NULL,"
                    + COLUMN_NOMBRE + " TEXT NOT NULL,"
                    + COLUMN_APELLIDOS + " TEXT NOT NULL,"
                    + COLUMN_ADMIN + " NUMERIC NOT NULL DEFAULT 0"
                    + ")";

    /*
     * Datos del usuario.
     */
    private long id;
    private String username;
    private String password;
    private String nombre;
    private String apellidos;
    private boolean admin;


    /**
     * Constructor sin parámetros.
     */
    public Usuario() {}

    /**
     * Constructor con parámetros.
     *
     * @param id Identificador del usuario.
     * @param username Nombre de usuario.
     * @param password Contraseña del usuario.
     * @param nombre Nombre real del usuario.
     * @param apellidos Apellidos del usuario.
     * @param admin Define si el usuario es o no es administrador.
     */
    public Usuario(long id, String username, String password, String nombre, String apellidos, boolean admin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.admin = admin;
    }


    /*
     * A partir de aquí se encuentran los getters y los setters de las propiedades
     * del usuario.
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
